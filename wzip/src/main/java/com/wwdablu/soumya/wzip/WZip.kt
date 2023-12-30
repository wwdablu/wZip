package com.wwdablu.soumya.wzip

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileInputStream
import java.security.InvalidParameterException
import java.util.LinkedList
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class WZip {

    companion object {

        /**
         * Method creates a new background thread for every zip call
         * @param filesAndDirectories List of files to zip
         * @param destinationFile Destination where the zip file is to be created. Ensure valid.
         * @param workerIdentifier Background thread identifier
         * @param callback Callback method
         */
        @JvmStatic
        fun zip(
            context: Context,
            filesAndDirectories: List<DocumentFile>,
            destinationFile: DocumentFile,
            workerIdentifier: String,
            callback: WZipCallback
        ) {
            if(filesAndDirectories.isNotEmpty() && destinationFile.isFile && destinationFile.exists()) {
                WZipWorker(
                    context,
                    filesAndDirectories,
                    destinationFile,
                    workerIdentifier,
                    callback
                ).run {
                    start()
                }
            } else {
                callback.onError(workerIdentifier, InvalidParameterException(), WZipCallback.Mode.ZIP)
            }
        }

        /**
         * Method to extract the contents of the ZIP file
         * @param zipFile Zip file to extract
         * @param destinationFolder Where to extract
         * @param workerIdentifier Background thread identifier
         * @param callback Callback method
         */
        @JvmStatic
        fun unzip(
            context: Context,
            zipFile: DocumentFile,
            destinationFolder: DocumentFile,
            workerIdentifier: String,
            callback: WZipCallback
        ) {
            if(destinationFolder.isDirectory && destinationFolder.exists() && !destinationFolder.isVirtual) {
                WUnzipWorker(context, zipFile, destinationFolder, workerIdentifier, callback).run {
                    start()
                }
            } else {
                callback.onError(workerIdentifier, InvalidParameterException(), WZipCallback.Mode.UNZIP)
            }
        }

        /**
         * Returns the details of all the files present inside the zip archive
         * @param zipFile Zip file
         * @return List of files with their details
         */
        @JvmStatic
        fun getFilesInfoFromZip(context: Context, zipFile: DocumentFile): List<ZipEntry> {

            val fileList = LinkedList<ZipEntry>()

            ZipInputStream(context.contentResolver.openInputStream(zipFile.uri)).use { zis ->
                var zipEntry: ZipEntry?
                do {
                    zipEntry = zis.nextEntry
                    if(zipEntry != null) fileList.add(zipEntry)
                } while (zipEntry != null)
            }

            return fileList
        }
    }
}
