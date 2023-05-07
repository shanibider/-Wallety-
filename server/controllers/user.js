const {formatUser} = require("../utils/format-user");
const {
    signInWithEmailAndPassword,
    createUserWithEmailAndPassword,
    fetchSignInMethodsForEmail
} = require('firebase/auth');
const {
    doc, getDoc, serverTimestamp, setDoc,
    collection, query, where, getDocs
} = require("firebase/firestore");
const {StatusCodes} = require('http-status-codes');
const {Collections, config} = require("../config/config");

const getLoggedInUser = async (req, res) => {
    const {auth, db} = config;
    let user = null;
    if (auth.currentUser) {
        const {uid, email} = auth.currentUser;
        const docRef = doc(db, Collections.USERS, uid);
        const docSnap = await getDoc(docRef);
        if (docSnap.exists()) {
            user = docSnap.data();
            formatUser(user, email, '');
            console.log(email + " logged in")
        }
    }
    res.status(StatusCodes.OK).send(user);
};

const loginUser = async (req, res) => {
    const {auth, db} = config;
    const {email, password} = req.body;
    signInWithEmailAndPassword(auth, email, password)
        .then(async (userCredential) => {
            const docRef = doc(db, Collections.USERS, userCredential.user.uid);
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
    const {email, password, phone, name} = req.body;

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
                createUser(res, auth, db, name, email, password, phone);
            }

        }
    });

};

const createUser = (res, auth, db, name, email, password, phone) => {
    createUserWithEmailAndPassword(auth, email, password)
        .then(async (userCredential) => {
            const id = userCredential.user.uid;
            const currentTimestampInSeconds = Math.round(Date.now() / 1000);
            const user = {
                id,
                name,
                phone,
                lastUpdated: serverTimestamp()
            };
            await setDoc(doc(db, Collections.USERS, id), user);
            user.lastUpdated = currentTimestampInSeconds;

            console.log(email + " signed up")
            res.status(StatusCodes.OK).send({user, existingDetail: ""});
        })
        .catch((error) => {
            console.log(error)
            res.status(StatusCodes.INTERNAL_SERVER_ERROR).send(error);
        });
}

module.exports = {
    getLoggedInUser,
    loginUser,
    signUpUser
};

