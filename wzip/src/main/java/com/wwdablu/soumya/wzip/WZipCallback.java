package com.wwdablu.soumya.wzip;

import java.io.File;

public interface WZipCallback {
    void onStarted(String identifier);
    void onZipCompleted(String identifier);
    void onUnzipCompleted(String identifier);
    void onError(Throwable throwable, String identifier);
}
