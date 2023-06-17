const {formatUserChildren, formatUser} = require("../utils/format-user");
const {
    signInWithEmailAndPassword,
    createUserWithEmailAndPassword,
    fetchSignInMethodsForEmail
} = require('firebase/auth');
const {
    doc, getDoc, updateDoc, setDoc, serverTimestamp,
    collection, query, where, getDocs, arrayUnion
} = require("firebase/firestore");
const {StatusCodes} = require('http-status-codes');
const {Collections, config} = require("../config/config");

const getLoggedInUser = async (req, res) => {
    const {auth, db} = config;
    let loggedInUser = null;
    if (auth.currentUser) {
        const {uid, email} = auth.currentUser;
        const docRef = doc(db, Collections.USERS, uid);
        const docSnap = await getDoc(docRef);
        if (docSnap.exists()) {
            loggedInUser = docSnap.data();

            await formatUserChildren(loggedInUser);
            formatUser(loggedInUser, email, '');
            console.log(email + " logged in")
        }
    }

    res.status(StatusCodes.OK).send({loggedInUser});
};

const loginUser = async (req, res) => {
    const {auth, db} = config;
    const {email, password, registrationToken} = req.body;
    signInWithEmailAndPassword(auth, email, password)
        .then(async (userCredential) => {
            const docRef = doc(db, Collections.USERS, userCredential.user.uid);
            await updateDoc(docRef, {
                registrationToken
            });
            const docSnap = await getDoc(docRef);

            if (docSnap.exists()) {
                const user = docSnap.data();

                await formatUserChildren(user);
                formatUser(user, email, password);
                console.log(email + " logged in")
                res.status(StatusCodes.OK).send(user);
            }
        })
        .catch((error) => {
            console.log(error)
            res.status(StatusCodes.INTERNAL_SERVER_ERROR).send(error);
        });
};

const signUpUser = async (req, res) => {
    const {auth, db} = config;
    const {email, password, phone, name, registrationToken, children} = req.body;

    fetchSignInMethodsForEmail(auth, email).then(async (result) => {
        if (result.length > 0) {
            res.status(StatusCodes.OK).send({
                existingDetail: "email",
                user: null
            });
        } else {
            const q = query(collection(db, Collections.USERS), where("phone", "==", phone));
            const querySnapshot = await getDocs(q);
            if (!querySnapshot.empty) {
                res.status(StatusCodes.OK).send({
                    existingDetail: "phone",
                    user: null
                });
            } else {
                createUser(res, auth, db, name, email, password, phone, registrationToken, children);
            }
        }
    });
};

const createUser = (res, auth, db, name, email, password, phone, registrationToken, children) => {
    createUserWithEmailAndPassword(auth, email, password)
        .then(async (userCredential) => {
            const id = userCredential.user.uid;
            const CHILD_INITIAL_BALANCE = 500;
            const PARENT_INITIAL_BALANCE = 5000;
            const balance = children ? PARENT_INITIAL_BALANCE : CHILD_INITIAL_BALANCE;
            const childrenIds = children ? children.map(({id}) => id) : [];
            const user = {
                id,
                name,
                phone,
                balance,
                registrationToken,
                ...(children && {children: childrenIds}),
                lastUpdated: serverTimestamp()
            };
            await setDoc(doc(db, Collections.USERS, id), user);

            // subscribe parent to children unusual expenses
            const {admin} = config;

            if (children) {
                const registrationTokens = [registrationToken];
                childrenIds.forEach(id => {
                    admin.messaging().subscribeToTopic(registrationTokens, id)
                        .then((response) => {
                            // Response is a message ID string.
                            console.log('Successfully sent message:', response);
                        })
                        .catch((error) => {
                            console.log('Error sending message:', error);
                        });
                });
            }

            const currentTimestampInSeconds = Math.round(Date.now() / 1000);

            // in Android the timestamp is Long type
            user.lastUpdated = currentTimestampInSeconds;
            if (children) {
                user.children = children;
            }

            console.log(email + " signed up")
            res.status(StatusCodes.OK).send({user, existingDetail: ""});
        })
        .catch((error) => {
            console.log(error)
            res.status(StatusCodes.INTERNAL_SERVER_ERROR).send(error);
        });
};

const getChildrenWithoutParent = async (req, res) => {
    const {db, admin} = config;
    const allChildren = [];
    const irrelevantUsersIds = new Set();
    const querySnapshot = await getDocs(collection(db, Collections.USERS));
    for (const doc1 of querySnapshot.docs) {
        const user = doc1.data();
        const children = user.children;

        // if parent (has children)
        if (children) {
            children.forEach((userId) => irrelevantUsersIds.add(userId));
        } else {
            try {
                const {email} = await admin.auth().getUser(user.id);
                formatUser(user, email, '');
                allChildren.push(user);
            } catch (e) {
                console.log(`Error: ${e}`);
            }
        }
    }
    const childrenWithoutParent = allChildren.filter(({id}) =>
        !irrelevantUsersIds.has(id)
    );

    res.status(StatusCodes.OK).send(childrenWithoutParent);

};

const makeTransaction = async (req, res) => {
    const {auth, db, admin} = config;
    const {transaction} = req.body;
    const {uid} = auth.currentUser;
    const docRef = doc(db, Collections.USERS, uid);
    await updateDoc(docRef, {
        transactions: arrayUnion(transaction)
    });
    console.log('Transaction made successfully');

    // push notification to parent if unusual
    if (transaction.isUnusual) {
        // setTimeout IS ONLY FOR CHECK IT PUSHED (DELETE IT!).
        setTimeout(() => {
            const message = {
                notification: {
                    title: 'Unusual expense detected',
                    body: 'Your child made unusual expense'
                },
                topic: uid
            };
            admin.messaging().send(message)
                .then((response) => {
                    // Response is a message ID string.
                    console.log('Successfully pushed notification:', response);
                })
                .catch((error) => {
                    console.log('Error pushing notification:', error);
                });
        }, 4000);
    }
    res.status(StatusCodes.OK).send("Transaction succeeded");
};

module.exports = {
    getLoggedInUser,
    loginUser,
    signUpUser,
    getChildrenWithoutParent,
    makeTransaction
};

