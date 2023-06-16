const express = require('express');
const router = express.Router();
const {
    getLoggedInUser,
    loginUser,
    signUpUser,
    getChildrenWithoutParent,
    makeTransaction
} = require("../controllers/user");

router.route('/loggedInUser')
    .get(getLoggedInUser);

router.route('/loginUser')
    .post(loginUser);

router.route('/signUpUser')
    .post(signUpUser);

router.route('/childrenWithoutParent')
    .get(getChildrenWithoutParent);

router.route('/makeTransaction')
    .post(makeTransaction);

module.exports = router;