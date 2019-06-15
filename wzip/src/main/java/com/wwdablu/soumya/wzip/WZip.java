package com.wwdablu.soumya.wzip;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class WZip {

    /**
     * Method creates a new background thread for every zip call
     * @param fileList List of files to zip
     * @param destinationFolder Destination where the zip file is to be created. Ensure valid.
     * @param workerIdentifier Background thread identifier
     * @param callback Callback method
     */
    public void zip(@NonNull List<File> fileList,
                    @NonNull File destinationFolder,
                    @NonNull String workerIdentifier,
                    @NonNull WZipCallback callback) {

        WZipWorker worker = new WZipWorker(fileList, destinationFolder, workerIdentifier, callback);
        worker.start();
    }

    public void unzip(@NonNull File zipFile,
                      @NonNull File destinationFolder,
                      @NonNull String workerIdentifier,
                      @NonNull WZipCallback callback) {

        WUnzipWorker unzipWorker = new WUnzipWorker(zipFile, destinationFolder, workerIdentifier, callback);
        unzipWorker.start();
    }

    /**
     * Returns the details of all the files present inside the zip archive
     * @param zipFile Zip file
     * @return List of files with their details
     */
    public List<ZipEntry> getFilesInZip(@NonNull File zipFile) {

        LinkedList<ZipEntry> fileList = new LinkedList<>();
        ZipInputStream zipInputStream = null;

        try {
            zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry;

            do {
                zipEntry = zipInputStream.getNextEntry();
                fileList.add(zipEntry);
            } while (zipEntry != null);

        } catch (IOException e) {
            Log.e(WZip.class.getName(), "Could not retrieve the list of files from zip.", e);
        } finally {

            if(zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return fileList;
    }
}
