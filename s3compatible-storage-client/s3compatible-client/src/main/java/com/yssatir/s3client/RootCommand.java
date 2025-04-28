package com.yssatir.s3client;

import picocli.CommandLine;

@CommandLine.Command(
        name = "s3client",
        description = "CLI to interact with S3-compatible storage (MinIO)",
        mixinStandardHelpOptions = true,
        subcommands = {CommandLine.HelpCommand.class}
)
public class RootCommand {
}
