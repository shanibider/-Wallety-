const {doc, getDoc} = require("firebase/firestore");
const {Collections, config} = require("../config/config");

const formatUserTimestamp = (user) => {
    user.lastUpdated = user.lastUpdated.seconds;
};

const formatUser = (user, email, password) => {
    formatUserTimestamp(user);
    user.email = email;
    user.password = password;
};

const formatUserChildren = async (user) => {
    const {db} = config;

    if (user.children) {
        const userChildren = [];
        for (const childId of user.children) {
            const docRef = doc(db, Collections.USERS, childId);
            const docSnap = await getDoc(docRef);
            const child = docSnap.data();
            formatUserTimestamp(child);
            userChildren.push(child);
        }
        user.children = userChildren;
    }
}

module.exports = {
    formatUserTimestamp,
    formatUser,
    formatUserChildren
};