package com.wwdablu.soumya.wzip

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

internal class WZipWorker(
    private val fileList: List<File>,
    private val destinationFile: File,
    private val workerId: String,
    private val callback: WZipCallback
) : BaseWorker() {
    override fun run() {

        ZipOutputStream(BufferedOutputStream(FileOutputStream(destinationFile))).use { zos ->

            callback.onStart(workerId, WZipCallback.Mode.ZIP)

            try {

                createDestinationFolderIfMissing(
                    File(
                        destinationFile.absolutePath.substring(
                            0,
                            destinationFile.absolutePath.lastIndexOf(File.separator)
                        )
                    )
                )

                val data = ByteArray(BUFFER)
                for (file in fileList) {
                    Log.d(WZip::class.java.name, "Zipping: " + file.name)
                    val fi = FileInputStream(file)

                    BufferedInputStream(fi, BUFFER).use { bis ->
                        val entry = ZipEntry(file.absolutePath.substring(
                            file.absolutePath.lastIndexOf("/") + 1))
                        zos.putNextEntry(entry)
                        var count: Int
                        while (bis.read(data, 0, BUFFER).also { count = it } != -1) {
                            zos.write(data, 0, count)
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
