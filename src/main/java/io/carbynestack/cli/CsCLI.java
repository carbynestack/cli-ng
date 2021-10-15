package io.carbynestack.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import static picocli.CommandLine.ScopeType.INHERIT;
import static picocli.CommandLine.usage;

@Command(name = "cs", description = "Command Line Interface to interact with Carbyne Stack Virtual Clouds",
        scope = INHERIT, mixinStandardHelpOptions = true, showAtFileInUsageHelp = true, versionProvider = Version.class)
public class CsCLI implements Runnable {
    //The Carbyne Stack CLI semantic version number.
    public static final String VERSION = "0.1.0";

    public static void main(String[] args) {
        System.exit(new CommandLine(new CsCLI()).execute(args));
    }

    @Override
    public void run() {
        usage(this, System.out);
    }
}
