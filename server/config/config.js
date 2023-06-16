require('dotenv/config');
const {initializeApp} = require("firebase/app");
const {getAuth} = require("firebase/auth");
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
    auth: null,
    db: null,
    admin: null,
    mongo: null
};

const initFirebase = () => {
    const firebaseApp = initializeApp(firebaseConfig);
    config.auth = getAuth(firebaseApp);
    config.db = getFirestore(firebaseApp);

    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
        databaseURL: process.env.FIREBASE_DATABASE_URL
    });
    config.admin = admin;
}

const initMongo = () => {
    const mongoose = require("mongoose");

    mongoose.connect(process.env.MONGO_SERVER, { useUnifiedTopology: true, useNewUrlParser: true });

    const connection = mongoose.connection;

    connection.once("open", function() {
    console.log("MongoDB database connection established successfully");
    });
}


module.exports = {
    Collections,
    initFirebase,
    config,
    initMongo
};