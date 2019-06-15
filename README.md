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
wZip library provides only 3 methods as of now. These methods are:  
* zip  
* unzip  
* getFilesInfoFromZip - Provides the file entry information  

The process to generate a zip archive is to provide the list of files and the destination where the zip is to be generated.  
~~~  
WZip wZip = new WZip();
wZip.zip(fileList,
    new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.zip"),
    "bitmapZipper",
    wZipCallback);
~~~  

The process to unzip the contents and extract them to a folder is:  
~~~  
WZip wZip = new WZip();
wZip.unzip(zipFile,
        new File(Environment.getExternalStorageDirectory().getAbsolutePath()),
        "backupUnzipper",
        wZipCallback);
~~~  

wZip also provides 4 callback methods to notify the states of the process.  
~~~  
void onStarted(String identifier);
void onZipCompleted(File zipFile, String identifier);
void onUnzipCompleted(String identifier);
void onError(Throwable throwable, String identifier);
~~~
