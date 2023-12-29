package com.wwdablu.soumya.wzip

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

internal class WUnzipWorker(
    private val zipFile: File,
    private val destinationFolder: File,
    private val workerIdentifier: String,
    private val callback: WZipCallback
) : BaseWorker() {
    override fun run() {

            ZipInputStream(FileInputStream(zipFile)).use { zis ->

            try {
                callback.onStart(workerIdentifier, WZipCallback.Mode.UNZIP)
                createDestinationFolderIfMissing(destinationFolder)
                var zipEntry = zis.nextEntry
                while (zipEntry != null) {

                    val opFile = destinationFolder.absolutePath + File.separator + zipEntry.name
                    FileOutputStream(opFile).use { fos ->
                        var size = zis.read()
                        while (size != -1) {
                            fos.write(size)
                            size = zis.read()
                        }
                    }
                    zipEntry = zis.nextEntry
                }

                callback.onUnzipComplete(workerIdentifier, destinationFolder)

            } catch (exception: Exception) {
                callback.onError(workerIdentifier, exception, WZipCallback.Mode.UNZIP)
            }
        }
    }
}
