var exec = require('cordova/exec');

exports.getNearables = function(arg0, success, error) {
    exec(success, error, "AndroidNearables", "getNearables", [arg0]);
};