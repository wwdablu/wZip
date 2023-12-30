package com.wwdablu.soumya.wzip

import android.content.Context
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal class WZipWorker(
    private val context: Context,
    private val filesList: List<DocumentFile>,
    private val destinationFile: DocumentFile,
    private val workerId: String,
    private val callback: WZipCallback
) : BaseWorker() {

    override fun run() {

        ZipOutputStream(BufferedOutputStream(context.contentResolver.openOutputStream(destinationFile.uri))).use { zos ->

            callback.onStart(workerId, WZipCallback.Mode.ZIP)

            try {

                val data = ByteArray(BUFFER)

                filesList.forEach { documentFile ->

                    context.contentResolver.openInputStream(documentFile.uri).use { dfis ->

                        BufferedInputStream(dfis, BUFFER).use { bis ->
                            val entry = ZipEntry(documentFile.name)
                            zos.putNextEntry(entry)
                            var count: Int
                            while (bis.read(data, 0, BUFFER).also { count = it } != -1) {
                                zos.write(data, 0, count)
                            }
                        }
                    }
                }

                callback.onZipComplete(workerId, destinationFile)

            } catch (exception: Exception) {
                callback.onError(workerId, exception, WZipCallback.Mode.ZIP)
            }
        }
    }

    companion object {
        private const val BUFFER = 256 * 1024
    }
}
