package com.wwdablu.soumya.wzip;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class WZipWorker extends Thread {

    private static final int BUFFER = 256 * 1024;

    private List<File> mFileList;
    private File mDestination;
    private String mWorkerId;
    private WZipCallback mCallback;

    WZipWorker(@NonNull List<File> fileList,
               @NonNull File destinationFolder,
               @NonNull String workerId,
               @NonNull WZipCallback callback) {

        mFileList = fileList;
        mDestination = destinationFolder;
        mWorkerId = workerId;
        mCallback = callback;
    }

    @Override
    public void run() {

        ZipOutputStream zipOutputStream = null;
        mCallback.onStarted(mWorkerId);

        try {

            BufferedInputStream bufferedInputStream;
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(mDestination)));
            byte[] data = new byte[BUFFER];

            for (File file : mFileList) {

                Log.d(WZip.class.getName(), "Zipping: " + file.getName());
                FileInputStream fi = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1));
                zipOutputStream.putNextEntry(entry);
                int count;

                while ((count = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
                    zipOutputStream.write(data, 0, count);
                }

                bufferedInputStream.close();
            }

            mCallback.onZipCompleted(mDestination, mWorkerId);

        } catch (Throwable throwable) {
            mCallback.onError(throwable, mWorkerId);

        } finally {

            if(zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                } catch (IOException e) {
                    Log.e(WZip.class.getName(), "An unexpected error occurred.", e);
                }
            }
        }
    }
}
