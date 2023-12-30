package com.wwdablu.soumya.wzip

import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.lang.Exception

interface WZipCallback {

    enum class Mode {
        ZIP,
        UNZIP
    }
    fun onStart(worker: String, mode: Mode)
    fun onZipComplete(worker: String, zipFile: DocumentFile)

    fun onUnzipComplete(worker: String, extractedFolder: DocumentFile)
    fun onError(worker: String, e: Exception, mode: Mode)
}