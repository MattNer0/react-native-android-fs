# react-native-android-fs

React Native Android module to use Android's Storage Access Framework and DocumentsProvier to access files on the device.

### Installation

```bash
yarn add react-native-android-fs
```

### Add it to your android project

- Link automatically with (if your version of react-native doesn't support autolinking):

```bash
react-native link react-native-android-fs
```

### Usage

```js
import AndroidFs from 'react-native-android-fs'
```

```js
// pick directory and read content
AndroidFs.openDirectoryPicker(async (treeUri) => {
  const contentArray = await AndroidFs.readDir(treeUri)
  for (let i=0; i<contentArray.length; i++) {
    console.log(contentArray[i])
    /*
      {
        'name'         : <string>,
        'path'         : <string>,
        'lastModified' : <number>,
        'isDirectory'  : <boolean>,
        'isFile'       : <boolean>
      }
    */
  }
})
```

```js
// create new directory
const newPath = await AndroidFs.mkdir(path, name)
```

```js
// create new file
const newPath = await AndroidFs.touch(path, filename, mimeType)
```

```js
// write text file
await AndroidFs.writeTextFile(fileUri, text)
```

```js
// read file content
const contentString = await AndroidFs.readTextFile(fileUri)
```

```js
// delete file
await AndroidFs.delete(fileUri)
```

### License

MIT
