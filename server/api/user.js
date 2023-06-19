const express = require('express');
const router = express.Router();
const {loginUser, signUpUser, getChildrenWithoutParent, makeTransaction, linkCard, linkCardToChild} = require("../controllers/user");

router.route('/loginUser')
    .post(loginUser);

router.route('/signUpUser')
    .post(signUpUser);

router.route('/childrenWithoutParent')
    .get(getChildrenWithoutParent);

router.route('/makeTransaction')
    .post(makeTransaction);

router.route('/linkCard')
    .post(linkCard);

router.route('/linkCardToChild')
    .post(linkCardToChild);

module.exports = router;