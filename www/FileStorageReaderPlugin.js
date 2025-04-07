var exec = require('cordova/exec');

exports.readFileStorage = function(arg0, success, error) {
  exec(success, error, 'FileStorageReaderPlugin', 'readFileStorage', [arg0]);
}