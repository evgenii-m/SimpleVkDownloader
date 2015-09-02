/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package push.simplevkdownloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author push
 */

enum DownloadStatus {
    DOWNLOADING, PAUSED, COMPLETE, CANCELLED, FAILED
}


class FileDownloader implements Runnable {

    public static final int MAX_BUFFER_SIZE = 8192;     // on bytes
    
    private final URL sourceUrl;
    private final String destinationFileName;
    private DownloadStatus downloadStatus;
    private int totalSize;          // on bytes
    private int downloadedSize;     // on bytes
    
    
    FileDownloader(URL url, String fileName) { 
        sourceUrl = url;
        destinationFileName = fileName;
        totalSize = -1;
        downloadedSize = 0;
        download();
    }
    
    int getTotalSize() {
        return totalSize;
    }

    int getDownloadedSize() {
        return downloadedSize;
    }

    DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    
    
    public void pause() {
        downloadStatus = DownloadStatus.PAUSED;
        System.out.println("Downloading file [" + destinationFileName +"] is paused.");
    }
    
    public void cancel() {
        downloadStatus = DownloadStatus.CANCELLED;
        System.out.println("Downloading file [" + destinationFileName +"] is cancelled.");
        downloadedSize = 0;
        totalSize = -1;
    }
    
    public void resume() {
        downloadStatus = DownloadStatus.DOWNLOADING;
        System.out.println("Downloading file [" + destinationFileName +"] is resumed.");
    }
    
    private void failed() {
        downloadStatus = DownloadStatus.FAILED;
        System.out.println("Downloading file [" + destinationFileName + "] is failed!"
                + " See log file for detail.");
    }
    
    private void complete() {
        downloadStatus = DownloadStatus.COMPLETE;
        System.out.println("Downloading file [" + destinationFileName +"] is complete!");
    }
    
    private void download() {
        downloadStatus = DownloadStatus.DOWNLOADING;
        System.out.println("Downloading file [" + destinationFileName +"] is started.");
        Thread thread = new Thread(this);
        thread.start();
    }
    
    
    @Override
    public void run() {
        try {
            HttpURLConnection connection = openHttpUrlConnection();
            if (downloadStatus == DownloadStatus.DOWNLOADING) { 
                downloadDataFromHttpUrlConnection(connection);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
            failed();
        }
    }
    
    private HttpURLConnection openHttpUrlConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection)sourceUrl.openConnection();
        connection.setRequestProperty("Range", "bytes=" + downloadedSize + "-"); // skip downloaded part
        connection.connect();
        int responseCode = connection.getResponseCode();
        if ((responseCode < 200 ) && (responseCode >= 300)) { // HTTP code no success
            Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, 
                    "GET request to [{0}] failed! Response code: [{1}]. Response message: [{2}].",
                    new Object[]{sourceUrl.toString(), responseCode, connection.getResponseMessage()});
            failed();
        } else {
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, 
                        "Invalid content length: [{0}].", contentLength);
                failed();
            }
            if (totalSize == -1)
                totalSize = contentLength;
        }
        return connection;
    }
    
    private void downloadDataFromHttpUrlConnection(HttpURLConnection connection) throws IOException {
        try (
                RandomAccessFile destinationFile = new RandomAccessFile(destinationFileName, "rw");
                InputStream connectionStream = connection.getInputStream()
            ) 
        {
            destinationFile.seek(downloadedSize);
            while (downloadStatus == DownloadStatus.DOWNLOADING) {
                int bufferSize = ((totalSize - downloadedSize) > MAX_BUFFER_SIZE) ? 
                        MAX_BUFFER_SIZE : (totalSize - downloadedSize);
                byte buffer[] = new byte[bufferSize];
                int readByteCount = connectionStream.read(buffer);
                if (readByteCount == -1) {
                    complete();
                } else {
                    destinationFile.write(buffer, 0, readByteCount);
                    downloadedSize += readByteCount;
                }
            }
        }
    }
    
}
