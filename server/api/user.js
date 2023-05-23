const express = require('express');
const router = express.Router();
const {getLoggedInUser, loginUser, signUpUser} = require("../controllers/user");

router.route('/loggedInUser')
    .get(getLoggedInUser);

router.route('/loginUser')
    .post(loginUser);

router.route('/signUpUser')
    .post(signUpUser);

module.exports = router;