package com.yssatir.s3client;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.event.ProgressListener;

public class UploadProgressBarPrinter implements ProgressListener {
    private long transferredBytes = 0;
    private final long totalBytes;
    private final int barLength = 50;

    public UploadProgressBarPrinter(long fileLength) {
        this.totalBytes = fileLength;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        if (progressEvent.getEventType() == ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT) {
            transferredBytes += progressEvent.getBytesTransferred();
            int progress = (int) ((transferredBytes * 100) / totalBytes);
            int filledLength = (int) ((transferredBytes * barLength) / totalBytes);

            StringBuilder bar = new StringBuilder("[");
            for (int i = 0; i < barLength; i++) {
                bar.append(i < filledLength ? "#" : "-");
            }
            bar.append("] ").append(progress).append("%");

            System.out.print("\r" + bar);
        }
    }
}
