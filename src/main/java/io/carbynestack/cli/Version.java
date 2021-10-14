package io.carbynestack.cli;

import picocli.CommandLine;
import picocli.CommandLine.IVersionProvider;

import java.util.Locale;

public class Version implements IVersionProvider {
    /**
     * Returns the version command output as an array of lines.
     *
     * @return An array of the individual version information lines.
     */
    @Override
    public String[] getVersion() {
        return String.format("""
                @|bold Command Line Interface to interact with Carbyne Stack Virtual Clouds|@
                Carbyne Stack CLI: %s
                
                @|bold Dependencies:|@
                Picocli: %s
                Amphora: 0.1-SNAPSHOT-1261403362-2-41864d
                Castor: 0.1-SNAPSHOT-1261403451-2-78f5f5b
                Ephemeral: 0.1-SNAPSHOT-1261324039-3-d2504ed
                
                @|bold Runtime & Environment:|@
                JVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})
                OS: ${os.name} ${os.version} ${os.arch}
                Locale: %s
                """, CsCLI.VERSION, CommandLine.VERSION, this.getLocale())
                .split(System.lineSeparator());
    }

    /**
     * Returns the tag, language, and country of the current environment locale.
     * The language and country names are given in English if available.
     *
     * @return The composed locale string sequence.
     */
    String getLocale() {
        var locale = Locale.getDefault();
        var target = new Locale("en", "US");
        return String.format("%s (%s %s)", locale.toLanguageTag(),
                locale.getDisplayLanguage(target), locale.getDisplayCountry(target));
    }
}
