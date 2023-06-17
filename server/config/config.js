require('dotenv/config');
const {initializeApp} = require("firebase/app");
const {getFirestore} = require("firebase/firestore");
const admin = require("firebase-admin");
const serviceAccount = require("../service-account.json");

const Collections = {
    USERS: "users"
};

const firebaseConfig = {
    apiKey: process.env.FIREBASE_API_KEY,
    authDomain: process.env.FIREBASE_AUTH_DOMAIN,
    databaseURL: process.env.FIREBASE_DATABASE_URL,
    projectId: process.env.FIREBASE_PROJECT_ID,
    storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.FIREBASE_MESSAGING_SENDER_ID,
    appId: process.env.FIREBASE_APP_ID,
    measurementId: process.env.FIREBASE_MEASUREMENT_ID
};

const config = {
    db: null,
    admin: null
};

const initFirebase = () => {
    const firebaseApp = initializeApp(firebaseConfig);
    config.db = getFirestore(firebaseApp);

    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
        databaseURL: process.env.FIREBASE_DATABASE_URL
    });
    config.admin = admin;
}


module.exports = {
    Collections,
    initFirebase,
    config
};