const formatUserTimestamp = (user) => {
    user.lastUpdated = user.lastUpdated.seconds;
};

const formatUser = (user, email, password, accessToken) => {
    formatUserTimestamp(user);
    user.email = email;
    user.password = password;
    user.accessToken = accessToken
};

module.exports = {
    formatUserTimestamp,
    formatUser
};