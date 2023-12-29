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

    private static final String TAG = MainActivity.class.getSimpleName();

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
        WZip.zip(fileList,
                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "misdir" + File.separator + "test.zip"),
                "Zipper",
                this);
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

                String s = dummyFile.getParent();

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

    @Override
    public void onStart(@NonNull String worker, @NonNull Mode mode) {
        Log.d(TAG, "onStart: " + mode.name() + " has been started for " + worker);
    }

    @Override
    public void onZipComplete(@NonNull String worker, @NonNull File zipFile) {

        Log.d(MainActivity.class.getSimpleName(), "Zip ops completed for identifier " + worker);
        showToast("Zip ops completed for identifier " + worker);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File[] files = file.listFiles();
        for (File f : files) {

            if(f.getName().contains("test_") && f.getName().endsWith(".txt")) {
                f.delete();
            }
        }

        Log.d(MainActivity.class.getSimpleName(), "File count: " + WZip.getFilesInfoFromZip(zipFile).size());
        WZip.unzip(zipFile,
                new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "misdirunzip"),
                "Unzipper",
                MainActivity.this);

        showToast("Extracting files");
    }

    @Override
    public void onUnzipComplete(@NonNull String worker, @NonNull File extractedFolder) {
        Log.d(TAG, "onUnzipComplete: " + worker + " has done unzip at " + extractedFolder.getAbsolutePath());
        showToast(worker + " has done unzip at " + extractedFolder.getAbsolutePath());
    }

    @Override
    public void onError(@NonNull String worker, @NonNull Exception e, @NonNull Mode mode) {
        Log.d(TAG, "onError: " + worker + " has encountered an exception " + e.getMessage() +
                " during " + mode.name());
    }
}
