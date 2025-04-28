package com.yssatir.s3client;

import picocli.CommandLine;


public class CLIStarter {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new RootCommand())
                .addSubcommand("create-bucket", new CreateBucketCommand())
                .addSubcommand("upload-file", new UploadFileCommand())
                .addSubcommand("download-file", new DownloadFileCommand())
                .execute(args);
        System.exit(exitCode);
    }
    
}