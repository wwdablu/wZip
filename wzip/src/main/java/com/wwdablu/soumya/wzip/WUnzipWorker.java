package com.wwdablu.soumya.wzip;



import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class WUnzipWorker extends BaseWorker {

    WUnzipWorker(@NonNull File zipFile,
                 @NonNull File destinationFolder,
                 @NonNull String workerIdentifier,
                 @NonNull WZipCallback callback) {

        super(zipFile, destinationFolder, workerIdentifier, callback);
    }

    @Override
    public void run() {

        ZipInputStream zipInputStream = null;

        try {

            mCallback.onStarted(mWorkerIdentifier);
            createDestinationFolderIfMissing(mDestinationFolder);

            zipInputStream = new ZipInputStream(new FileInputStream(mZipFile));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {

                FileOutputStream fos = new FileOutputStream(mDestinationFolder.getAbsolutePath() + File.separator + zipEntry.getName());
                int size = zipInputStream.read();

                while (size != -1) {
                    fos.write(size);
                    size = zipInputStream.read();
                }

                fos.close();
                zipEntry = zipInputStream.getNextEntry();
            }

            mCallback.onUnzipCompleted(mWorkerIdentifier);

        } catch (Throwable throwable) {
            mCallback.onError(throwable, mWorkerIdentifier);

        } finally {

            if(zipInputStream != null) {
                try {
                    zipInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
