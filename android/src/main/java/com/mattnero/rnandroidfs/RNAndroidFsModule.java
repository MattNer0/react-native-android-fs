package com.mattnero.rnandroidfs;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
//import android.provider.DocumentsContract;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.NullPointerException;
import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReactMethod;

public class RNAndroidFsModule extends ReactContextBaseJavaModule {
    private static final int DIRECTORY_SELECT_CODE = 65502;
    private static final String TAG = RNAndroidFsModule.class.getSimpleName();

    private ReactApplicationContext reactContext;
    private Callback mCallback;

    public RNAndroidFsModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "AndroidFs";
    }

    @ReactMethod
    public void openDirectoryPicker(Callback callback) {
        mCallback = callback;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        try {
            Activity currentActivity = getCurrentActivity();
            currentActivity.startActivityForResult(intent, DIRECTORY_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            callback.invoke("");
        }
    }

    @ReactMethod
    public void readDir(String path, final Promise promise) {
        DocumentFile pickedDir = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
        WritableArray array = new WritableNativeArray();

        for (DocumentFile file : pickedDir.listFiles()) { 
            WritableMap fileMap = new WritableNativeMap();

            fileMap.putString("name", file.getName());
            fileMap.putString("path", file.getUri().toString());
            fileMap.putDouble("lastModified", file.lastModified());
            fileMap.putBoolean("isDirectory", file.isDirectory());
            fileMap.putBoolean("isFile", file.isFile());

            array.pushMap(fileMap);
        }

        promise.resolve(array);
    }

    @ReactMethod
    public void readTextFile(String path, final Promise promise) {
        if (path == null) {
            promise.reject("NullPointerException", "Path is null");
            return;
        }

        DocumentFile pickedFile = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
        if (pickedFile.isFile()) {
            InputStream is = null;
            StringBuilder result = new StringBuilder();
            try {
                is = this.reactContext.getContentResolver().openInputStream(pickedFile.getUri());
                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = r.readLine()) != null) {
                    result.append(line);
                    result.append("\n");
                }
                try { if (is != null) is.close(); } catch (IOException e) { }
                promise.resolve(result.toString());
            } catch (FileNotFoundException e) {
                promise.reject("FileNotFoundException", e.getMessage());
            } catch (IOException e) {
                promise.reject("IOException", e.getMessage());
            }
            return;
        }

        promise.reject("FileNotFoundException", "Path is not a valid file");
    }

    @ReactMethod
    public void writeTextFile(String path, String content, final Promise promise) {
        if (path == null) {
            promise.reject("NullPointerException", "Path is null");
            return;
        }

        DocumentFile pickedFile = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
        if (pickedFile.isFile()) {
            OutputStream os = null;
            StringBuilder result = new StringBuilder();
            try {
                os = this.reactContext.getContentResolver().openOutputStream(pickedFile.getUri());
                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(os));
                w.write(content);
                w.flush();
                w.close();

                try { if (os != null) os.close(); } catch (IOException e) { }
                promise.resolve(content);

            } catch (FileNotFoundException e) {
                promise.reject("FileNotFoundException", e.getMessage());
            } catch (IOException e) {
                promise.reject("IOException", e.getMessage());
            }
            return;
        }

        promise.reject("FileNotFoundException", "Path is not a valid file");
    }

    @ReactMethod
    public void exists(String path, final Promise promise) {
        DocumentFile pickedFile = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
        promise.resolve(pickedFile.exists());
    }

    @ReactMethod
    public void mkdir(String path, String newDirName, final Promise promise) {
        try {
            DocumentFile pickedFile = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
            DocumentFile newDir = pickedFile.createDirectory(newDirName);
            promise.resolve(newDir.getUri().toString());
            return;
        } catch (UnsupportedOperationException e) {
            promise.reject("UnsupportedOperationException", e.getMessage());
        } catch (NullPointerException e) {
            promise.reject("NullPointerException", e.getMessage());
        }

        promise.resolve(null);
    }

    @ReactMethod
    public void touch(String path, String newFileName, String mimeType, final Promise promise) {
        try {
            DocumentFile pickedFile = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
            DocumentFile newFile = pickedFile.createFile(mimeType, newFileName);
            promise.resolve(newFile.getUri().toString());
            return;
        } catch (UnsupportedOperationException e) {
            promise.reject("UnsupportedOperationException", e.getMessage());
        } catch (NullPointerException e) {
            promise.reject("NullPointerException", e.getMessage());
        }

        promise.resolve(null);
    }

    @ReactMethod
    public void rename(String path, String newName, final Promise promise) {
        try {
            DocumentFile pickedFile = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
            pickedFile.renameTo(newName);
            promise.resolve(pickedFile.getUri().toString());
            return;
        } catch (UnsupportedOperationException e) {
            promise.reject("UnsupportedOperationException", e.getMessage());
        } catch (NullPointerException e) {
            promise.reject("NullPointerException", e.getMessage());
        }

        promise.resolve(null);
    }

    @ReactMethod
    public void delete(String path, final Promise promise) {
        try {
            DocumentFile pickedFile = DocumentFile.fromTreeUri(this.reactContext, Uri.parse(path));
            promise.resolve(pickedFile.delete());
            return;
        } catch (UnsupportedOperationException e) {
            promise.reject("UnsupportedOperationException", e.getMessage());
        } catch (NullPointerException e) {
            promise.reject("NullPointerException", e.getMessage());
        }

        promise.resolve(null);
    }

    @ReactMethod
    public void requestIgnoreBatteryOptimizations(final Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) this.reactContext.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(this.reactContext.getPackageName())) {
                Intent sendIntent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                sendIntent.setData(Uri.fromParts("package", this.reactContext.getPackageName(), null));
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (sendIntent.resolveActivity(this.reactContext.getPackageManager()) != null) {
                    this.reactContext.startActivity(sendIntent);

                    promise.resolve(true);
                    return;
                }
            }
        }

        promise.resolve(false);
    }

    @ReactMethod
    public void showIgnoreBatteryOptimizationsSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent sendIntent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (sendIntent.resolveActivity(this.reactContext.getPackageManager()) != null) {
                this.reactContext.startActivity(sendIntent);
            }
        }
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == DIRECTORY_SELECT_CODE && resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri treeUri = data.getData();
                    reactContext.grantUriPermission(reactContext.getPackageName(), treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    reactContext.getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    mCallback.invoke(treeUri.toString());
                }
            }
        }
    };
}
