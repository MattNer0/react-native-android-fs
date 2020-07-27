# react-native-android-fs

React Native Android module to use Android's Intent and DocumentsProvier to access files stored on the device.

### Installation

```bash
yarn add react-native-android-fs
```

### Add it to your android project

- Automatically with (if your version of react-native doesn't support autolinking):

```bash
react-native link react-native-android-fs
```

#### Manually

- In `android/setting.gradle`

```gradle
...
include ':RNAndroidFsModule', ':app'
project(':RNAndroidFsModule').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-fs/android')
```

- In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':RNAndroidFsModule')
}
```

- Register Module (in MainApplication.java)

```java
import com.mattnero.rnandroidfs.RNAndroidFsPackage;  // <--- import

public class MainApplication extends Application implements ReactApplication {
  ......

  @Override
  protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new RNAndroidFsPackage()); // <------ add this line to your MainApplication class
  }

  ......

}
```

## License

MIT
