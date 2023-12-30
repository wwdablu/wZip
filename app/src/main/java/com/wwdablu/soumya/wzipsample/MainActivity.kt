package com.wwdablu.soumya.wzipsample

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import com.wwdablu.soumya.wzip.WZip.Companion.unzip
import com.wwdablu.soumya.wzip.WZip.Companion.zip
import com.wwdablu.soumya.wzip.WZipCallback
import java.util.LinkedList

class MainActivity : AppCompatActivity(), WZipCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prepare()
    }

    private fun prepare() {

        //Create dummy files for testing
        createDummyFiles()

        //Fetch the files
        val testDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        DocumentFile.fromFile(testDir).createDirectory("msdir")?.run {
            val df = listFiles()
            val dfList: MutableList<DocumentFile> = LinkedList()
            for (d in df) {
                dfList.add(d)
            }

            createFile("application/zip", "test.zip")?.let {
                it.renameTo("test.zip")
                zip(this@MainActivity, dfList, it, "Zipper", this@MainActivity)
            }
        }
    }

    private fun showToast(text: String) {
        runOnUiThread { Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show() }
    }

    private fun createDummyFiles() {
        var fileIndex = 0
        while (fileIndex++ != 2) {
            try {
                val testDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)

                DocumentFile.fromFile(testDir).createDirectory("msdir")?.let { documentFile ->
                    documentFile.createFile("text/plain", "empty")?.let {
                        it.renameTo("test_$fileIndex.txt")
                        contentResolver.openOutputStream(it.uri)
                            .use { os -> os!!.write("Hello World".toByteArray()) }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                fileIndex = Int.MAX_VALUE
            }
        }
    }

    override fun onStart(worker: String, mode: WZipCallback.Mode) {
        Log.d(TAG, "onStart: " + mode.name + " has been started for " + worker)
    }

    override fun onZipComplete(worker: String, zipFile: DocumentFile) {
        Log.d(MainActivity::class.java.simpleName, "Zip ops completed for identifier $worker")
        showToast("Zip ops completed for identifier " + worker + " for " + zipFile.name)

        val testDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        DocumentFile.fromFile(testDir).createDirectory("msdir_extracted")?.let {
            unzip(this, zipFile, it, "Unzipper", this)
        }

        showToast("Extracting files")
    }

    override fun onUnzipComplete(worker: String, extractedFolder: DocumentFile) {
        Log.d(TAG, "onUnzipComplete: " + worker + " has done unzip at " + extractedFolder.name)
        showToast(worker + " has done unzip at " + extractedFolder.name)
    }

    override fun onError(worker: String, e: Exception, mode: WZipCallback.Mode) {
        Log.d(
            TAG, "onError: " + worker + " has encountered an exception " + e.message +
                    " during " + mode.name
        )
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
