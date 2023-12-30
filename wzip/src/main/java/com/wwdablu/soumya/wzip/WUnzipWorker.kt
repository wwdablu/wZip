package com.wwdablu.soumya.wzip

import android.content.Context
import androidx.documentfile.provider.DocumentFile
import java.util.zip.ZipInputStream

internal class WUnzipWorker(
    private val context: Context,
    private val zipFile: DocumentFile,
    private val destinationFolder: DocumentFile,
    private val workerIdentifier: String,
    private val callback: WZipCallback
) : BaseWorker() {
    override fun run() {

        context.contentResolver.openInputStream(zipFile.uri).use { zipFileInputStream ->

            ZipInputStream(zipFileInputStream).use { zis ->

                try {
                    callback.onStart(workerIdentifier, WZipCallback.Mode.UNZIP)

                    var zipEntry = zis.nextEntry
                    while (zipEntry != null) {

                        val opFile = destinationFolder.createFile("text/plain", zipEntry.name)
                        if (opFile != null) {
                            opFile.renameTo(zipEntry.name)
                            context.contentResolver.openOutputStream(opFile.uri).use { fos ->
                                var size = zis.read()
                                while (size != -1 && fos != null) {
                                    fos.write(size)
                                    size = zis.read()
                                }
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
}
