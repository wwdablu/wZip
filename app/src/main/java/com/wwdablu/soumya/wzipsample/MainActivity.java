package com.wwdablu.soumya.wzipsample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.wwdablu.soumya.wzip.WZipCallback;

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
    }

    @Override
    public void onStarted(String identifier) {

    }

    @Override
    public void onZipCompleted(String identifier) {

    }

    @Override
    public void onUnzipCompleted(String identifier) {

    }

    @Override
    public void onError(Throwable throwable, String identifier) {

    }
}
