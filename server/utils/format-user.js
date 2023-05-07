const formatUser = (user, email, password) => {
    user.lastUpdated = user.lastUpdated.seconds;
    user.email = email;
    user.password = password;
};

module.exports = {
    formatUser
};