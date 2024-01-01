[![](https://jitpack.io/v/wwdablu/wZip.svg)](https://jitpack.io/#wwdablu/wZip)  

# wZip
An android library the provides zip and unzip functionality. This uses the default implementation of ZipInputStream and ZipOutputStream to read and generate the zip files.  

## Gradle  
~~~  
maven { url 'https://jitpack.io' }

dependencies {
    implementation 'com.github.wwdablu:wZip:x.y.z'
}
~~~  

## Usage  
wZip library provides only 2 methods:  
* zip  
* unzip   

The process to generate a zip archive is to provide the list of files and the destination where the zip is to be generated.  
~~~  
zip(this@MainActivity, // Context
    dfList,            // List of DocumentFile pointing to the individual files
    it,                // DocumentFile pointing to the zip file to be created
    "Zipper",          // Name of the zip process
    this@MainActivity) // Callback implementation
~~~  

The process to unzip the contents and extract them to a folder is:  
~~~  
unzip(this@MainActivity, // Context
      zipFile,           // DocumentFile pointing to the zip file
      it,                // DocumentFile pointing to the directory to be extracted into
      "Unzipper",        // Name of the zip process
      this@MainActivity) // Callback implementation
~~~  

wZip also provides 4 callback methods to notify the states of the process.  
~~~  
fun onStart(worker: String, mode: Mode)
fun onZipComplete(worker: String, zipFile: DocumentFile)
fun onUnzipComplete(worker: String, extractedFolder: DocumentFile)
fun onError(worker: String, e: Exception, mode: Mode)
~~~
  
## Note
It uses DocumentFile for access to the files and creating the ZIP file.