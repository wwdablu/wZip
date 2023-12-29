package com.wwdablu.soumya.wzip

import java.io.File
import java.io.FileInputStream
import java.util.LinkedList
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class WZip {

    companion object {

        /**
         * Method creates a new background thread for every zip call
         * @param fileList List of files to zip
         * @param destinationFolder Destination where the zip file is to be created. Ensure valid.
         * @param workerIdentifier Background thread identifier
         * @param callback Callback method
         */
        @JvmStatic
        fun zip(
            fileList: List<File>,
            destinationFolder: File,
            workerIdentifier: String,
            callback: WZipCallback
        ) {
            WZipWorker(fileList, destinationFolder, workerIdentifier, callback).run {
                start()
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
            zipFile: File,
            destinationFolder: File,
            workerIdentifier: String,
            callback: WZipCallback
        ) {
            WUnzipWorker(zipFile, destinationFolder, workerIdentifier, callback).run {
                start()
            }
        }

        /**
         * Returns the details of all the files present inside the zip archive
         * @param zipFile Zip file
         * @return List of files with their details
         */
        @JvmStatic
        fun getFilesInfoFromZip(zipFile: File): List<ZipEntry> {
            val fileList = LinkedList<ZipEntry>()

            ZipInputStream(FileInputStream(zipFile)).use { zis ->
                var zipEntry: ZipEntry?
                do {
                    zipEntry = zis.nextEntry
                    fileList.add(zipEntry)
                } while (zipEntry != null)
            }

            return fileList
        }
    }
}
