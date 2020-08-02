/**
 * @providesModule AndroidFs
 */

var mime = require('mime-types')
var { NativeModules } = require("react-native");
var RNAndroidFs = NativeModules.AndroidFs || {};

var AndroidFs = {
    openDirectoryPicker(callback) {
        return RNAndroidFs.openDirectoryPicker(callback);
    },
    readDir(path) {
        return RNAndroidFs.readDir(path);
    },
    readTextFile(path) {
        return RNAndroidFs.readTextFile(path);
    },
    writeTextFile(path, content) {
        return RNAndroidFs.writeTextFile(path, content);
    },
    exists(path) {
        return RNAndroidFs.exists(path);
    },
    mkdir(path, dir) {
        return RNAndroidFs.mkdir(path, dir);
    },
    touch(path, file, mime) {
        if (mime === undefined) {
            const fileParse = file.match(/(.+?)(\.[^.]*$|$)/)
            return RNAndroidFs.touch(path, fileParse[1], mime.lookup(fileParse[2]));
        }
        return RNAndroidFs.touch(path, file, mime);
    },
    requestIgnoreBatteryOptimizations() {
        return RNAndroidFs.requestIgnoreBatteryOptimizations();
    },
    showIgnoreBatteryOptimizationsSettings() {
        RNAndroidFs.showIgnoreBatteryOptimizationsSettings();
    }
};

module.exports = AndroidFs;
