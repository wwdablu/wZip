package com.wwdablu.soumya.wzipsample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.wwdablu.soumya.wzip.WZip;
import com.wwdablu.soumya.wzip.WZipCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements WZipCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int wes = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int res = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(wes != PackageManager.PERMISSION_GRANTED || res != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1000);
        } else {
            prepare();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode != 1000) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if(grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Need read and write permission to continue.", Toast.LENGTH_SHORT).show();
            return;
        }

        prepare();
    }

    private void prepare() {

        //Create dummy files for testing
        createDummyFiles();

        //Fetch the list of files that needs to be zipped
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "misdir");
        File[] files = file.listFiles();
        LinkedList<File> fileList = new LinkedList<>();
        for (File f : files) {

            if(f.getName().contains("test_") && f.getName().endsWith(".txt")) {
                fileList.add(f);
            }
        }

        //Perform the zip operation
        WZip wZip = new WZip();
        wZip.zip(fileList,
                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "misdir" + File.separator + "test.zip"),
                "zip",
                this);
    }

    @Override
    public void onStarted(String identifier) {
        Log.d(MainActivity.class.getSimpleName(), "Started: " + identifier);
    }

    @Override
    public void onZipCompleted(File zipFile, String identifier) {
        Log.d(MainActivity.class.getSimpleName(), "Zip ops completed for identifier " + identifier);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File[] files = file.listFiles();
        for (File f : files) {

            if(f.getName().contains("test_") && f.getName().endsWith(".txt")) {
                f.delete();
            }
        }

        //Wait for 1500 msec to test unzip ops
        new Handler(MainActivity.this.getMainLooper()).postDelayed(() -> {
            WZip wZip = new WZip();
            Log.d(MainActivity.class.getSimpleName(), "File count: " + wZip.getFilesInfoFromZip(zipFile).size());
            wZip.unzip(zipFile,
                    new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "misdirunzip"),
                    "unzip",
                    MainActivity.this);
        }, 1500);
    }

    @Override
    public void onUnzipCompleted(String identifier) {
        Log.d(MainActivity.class.getSimpleName(), "Unzip ops completed for " + identifier);
    }

    @Override
    public void onError(Throwable throwable, String identifier) {
        showToast(throwable.getMessage());
        Log.e(MainActivity.class.getSimpleName(), throwable.getMessage(), throwable);
    }

    private void showToast(String text) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show());
    }

    private void createDummyFiles() {

        int fileIndex = 0;
        while (fileIndex != 2) {

            try {

                File dummyFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "misdir" + File.separator + "test_" + fileIndex + ".txt");

                boolean r = new File(dummyFile.getParent()).mkdirs();
                r = dummyFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(dummyFile);

                byte[] data = ("This is a string test." + fileIndex).getBytes();
                int repeat = 1000;
                while (--repeat != 0) {
                    fos.write(data);
                }

                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            fileIndex++;
        }
    }
}
