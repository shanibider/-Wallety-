const formatUserTimestamp = (user) => {
    user.lastUpdated = user.lastUpdated.seconds;
};

const formatUser = (user, email, password) => {
    formatUserTimestamp(user);
    user.email = email;
    user.password = password;
};

module.exports = {
    formatUserTimestamp,
    formatUser
};