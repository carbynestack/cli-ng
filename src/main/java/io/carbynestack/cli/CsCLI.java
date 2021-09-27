package io.carbynestack.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "cs", version = "0.1-SNAPSHOT", mixinStandardHelpOptions = true)
public class CsCLI implements Callable<Integer> {

    public static void main(String[] args) {
        System.exit(new CommandLine(new CsCLI()).execute(args));
    }

    @Override
    public Integer call() {
		System.out.println("Hello Carbyne Stack Community!");
		return 0;
	}

}
