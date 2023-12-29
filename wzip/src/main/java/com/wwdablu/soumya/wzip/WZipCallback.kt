package com.wwdablu.soumya.wzip

import java.io.File
import java.lang.Exception

interface WZipCallback {

    enum class Mode {
        ZIP,
        UNZIP
    }
    fun onStart(worker: String, mode: Mode)
    fun onZipComplete(worker: String, zipFile: File)

    fun onUnzipComplete(worker: String, extractedFolder: File)
    fun onError(worker: String, e: Exception, mode: Mode)
}