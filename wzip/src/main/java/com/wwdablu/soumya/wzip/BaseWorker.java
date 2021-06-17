package com.wwdablu.soumya.wzip;

import android.os.Build;

import androidx.annotation.NonNull;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.List;

abstract public class BaseWorker extends Thread {

    protected final List<File> mFileList;
    protected final File mZipFile;
    protected final File mDestinationFolder;
    protected final String mWorkerIdentifier;
    protected final WZipCallback mCallback;

    protected BaseWorker(@NonNull File zipFile,
                         @NonNull File destinationFolder,
                         @NonNull String workerIdentifier,
                         @NonNull WZipCallback callback) {

        mZipFile = zipFile;
        mFileList = null;
        mDestinationFolder = destinationFolder;
        mWorkerIdentifier = workerIdentifier;
        mCallback = callback;
    }

    protected BaseWorker(@NonNull List<File> fileList,
                         @NonNull File destinationFolder,
                         @NonNull String workerId,
                         @NonNull WZipCallback callback) {

        mZipFile = null;
        mFileList = fileList;
        mDestinationFolder = destinationFolder;
        mWorkerIdentifier = workerId;
        mCallback = callback;
    }

    protected void createDestinationFolderIfMissing(File destination) throws Exception {

        if(destination.exists()) {
            return;
        }

        //Create all the necessary missing directories
        boolean mkdirSuccess = true;
        if(destination.exists()) {
            return;
        }
        mkdirSuccess = destination.mkdirs();

        if(!mkdirSuccess) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                throw new FileSystemException("Unable to create directories in the provided destination, " + destination.getAbsolutePath());
            } else {
                throw new Exception("Unable to create directories in the provided destination, " + destination.getAbsolutePath());
            }
        }
    }
}
