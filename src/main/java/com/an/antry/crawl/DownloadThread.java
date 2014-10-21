package com.an.antry.crawl;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class downloads a file from a URL.
public class DownloadThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(DownloadThread.class);
    // These are the status names.
    public static final String STATUSES[] = { "Downloading", "Paused", "Complete", "Cancelled", "Error" };
    // These are the status codes.
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    private URL url; // download URL
    private int size = -1; // size of download in bytes
    private int downloaded = 0; // number of bytes downloaded
    private int status = DOWNLOADING; // current status of download
    private String fileName = null;

    private static final int DOWNLOAD_CONTENT_SIZE = 30000;
    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = DOWNLOAD_CONTENT_SIZE;

    // Constructor for Download.
    public DownloadThread(URL url, String fileName) {
        this.url = url;
        this.fileName = fileName;
    }

    // Get file name portion of URL.
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    // Download file.
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;
        try {
            // Open connection to URL.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Specify what portion of file to download.
            // conn.setRequestProperty("Range", "bytes=" + downloaded + "-");
            conn.setRequestProperty("Accept-Encoding", "identity");
            // Connect to server.
            conn.connect();
            // Make sure response code is in the 200 range.
            if (conn.getResponseCode() / 100 != 2) {
                error();
            }
            // Check for valid content length.
            int contentLength = conn.getContentLength();
            logger.error("contentLength {}", contentLength);
            if (contentLength < 1) {
                // error();
                contentLength = DOWNLOAD_CONTENT_SIZE;
            }
            // Set the size for this download if it hasn't been already set.
            if (size == -1) {
                size = contentLength;
                logger.error("size == -1");
                // stateChanged();
            }
            // Open file and seek to the end of it.

            if (fileName == null) {
                fileName = getFileName(url);
            }
            file = new RandomAccessFile(fileName, "rw");
            file.seek(downloaded);
            stream = conn.getInputStream();
            while (status == DOWNLOADING) {
                if (size == downloaded) {
                    logger.info("Finished downloading.");
                    break;
                } else if (size < downloaded) {
                    logger.error("size < downloaded.");
                    break;
                }

                // Size buffer according to how much of the file is left to download.
                byte buffer[];
                if (size - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    buffer = new byte[size - downloaded];
                }
                try {
                    int read = stream.read(buffer);
                    // Write buffer to file.
                    file.write(buffer, 0, read);
                    downloaded += read;
                } catch (Exception e) {
                    logger.error("Error while downloading URL {}", url.toString());
                    e.printStackTrace();
                    break;
                }
            }

            // Change status to complete if this point was reached because downloading has finished.
            if (status == DOWNLOADING) {
                complete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            error();
        } finally {
            // Close file.
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Close connection to server.
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Pause this download.
    public void pause() {
        status = PAUSED;
    }

    // Cancel this download.
    public void cancel() {
        status = CANCELLED;
    }

    // Mark this download as having an error.
    private void error() {
        status = ERROR;
    }

    private void complete() {
        status = COMPLETE;
    }

    // Get this download's URL.
    public String getUrl() {
        return url.toString();
    }

    // Get this download's size.
    public int getSize() {
        return size;
    }

    // Get this download's progress.
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }

    // Get this download's status.
    public int getStatus() {
        return status;
    }
}
