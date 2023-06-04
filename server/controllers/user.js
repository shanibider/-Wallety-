const {formatUser} = require("../utils/format-user");
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
            formatUser(loggedInUser, email, '');
            console.log(email + " logged in")
        }
    }

    //-------
    // Part of parent child subscribe (in children choose - sign up)
    // const registrationTokens = [
    //     '<token>',
    // ];

    // admin.messaging().subscribeToTopic(registrationTokens, '<child-uid>')
    //     .then((response) => {
    //         Response is a message ID string.
    // console.log('Successfully sent message:', response);
    // })
    // .catch((error) => {
    //     console.log('Error sending message:', error);
    // });

    //-------


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
    const {email, password, phone, name, isParent} = req.body;

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
                createUser(res, auth, db, name, email, password, phone, isParent);
            }
        }
    });
};

const createUser = (res, auth, db, name, email, password, phone, isParent) => {
    createUserWithEmailAndPassword(auth, email, password)
        .then(async (userCredential) => {
            const id = userCredential.user.uid;
            const currentTimestampInSeconds = Math.round(Date.now() / 1000);
            const user = {
                id,
                name,
                phone,
                isParent,
                lastUpdated: serverTimestamp()
            };
            await setDoc(doc(db, Collections.USERS, id), user);

            // in Android the timestamp is Long type
            user.lastUpdated = currentTimestampInSeconds;

            console.log(email + " signed up")
            res.status(StatusCodes.OK).send({user, existingDetail: ""});
        })
        .catch((error) => {
            console.log(error)
            res.status(StatusCodes.INTERNAL_SERVER_ERROR).send(error);
        });
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
    makeTransaction
};

