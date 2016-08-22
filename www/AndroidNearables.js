var exec = require('cordova/exec');

exports.coolMethod = function(arg0, success, error) {
    exec(success, error, "AndroidNearables", "coolMethod", [arg0]);
};

exports.echo = function(arg0, success, error) {
    exec(success, error, 'AndroidNearables', 'echo', [arg0]);
}

exports.echoText = function(arg0, success, error) {
    console.log('arg0: ', arg0);
    success();
}