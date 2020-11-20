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
        return RNAndroidFs.touch(path, file, mime);
    },
    rename(path, name) {
        return RNAndroidFs.rename(path, name);
    },
    delete(path) {
        return RNAndroidFs.delete(path);
    },
    requestIgnoreBatteryOptimizations() {
        return RNAndroidFs.requestIgnoreBatteryOptimizations();
    },
    showIgnoreBatteryOptimizationsSettings() {
        RNAndroidFs.showIgnoreBatteryOptimizationsSettings();
    }
};

module.exports = AndroidFs;
