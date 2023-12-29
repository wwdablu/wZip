package com.wwdablu.soumya.wzip

import android.os.Build
import java.io.File
import java.nio.file.FileSystemException
import java.util.LinkedList

abstract class BaseWorker : Thread() {

    @Throws(Exception::class)
    protected fun createDestinationFolderIfMissing(destination: File) {
        if (destination.exists()) {
            return
        }

        //Create all the necessary missing directories
        var mkdirSuccess = true
        if (destination.exists()) {
            return
        }
        mkdirSuccess = destination.mkdirs()
        if (!mkdirSuccess) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                throw FileSystemException("Unable to create directories in the provided destination, " + destination.absolutePath)
            } else {
                throw Exception("Unable to create directories in the provided destination, " + destination.absolutePath)
            }
        }
    }
}
