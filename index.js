/**
 * @providesModule AndroidFs
 */

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
    exists(path) {
        return RNAndroidFs.exists(path);
    },
    mkdir(path) {
        return RNAndroidFs.mkdir(path);
    },
    requestIgnoreBatteryOptimizations() {
        return RNAndroidFs.requestIgnoreBatteryOptimizations();
    },
    showIgnoreBatteryOptimizationsSettings() {
        RNAndroidFs.showIgnoreBatteryOptimizationsSettings();
    }
};

module.exports = AndroidFs;
