package com.yssatir.s3client;

import com.amazonaws.event.ProgressEventType;

public class UploadProgressPercentagePrinter implements com.amazonaws.event.ProgressListener{
    private long transferredBytes = 0;
    private final long totalBytes;
    
    public UploadProgressPercentagePrinter(long fileLength) {
    	super();
    	totalBytes = fileLength;
    }

    @Override
    public void progressChanged(com.amazonaws.event.ProgressEvent progressEvent) {
        if (progressEvent.getEventType() == ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT) {
            transferredBytes += progressEvent.getBytesTransferred();
            int progress = (int) ((transferredBytes * 100) / totalBytes);
            System.out.print("\rUploading: " + progress + "%");
        }
    }
    
}
