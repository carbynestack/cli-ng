package io.carbynestack.cli.common.runners;

import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.Stub;
import io.carbynestack.common.result.Result;
import picocli.CommandLine;

import java.util.Locale;

import static io.carbynestack.cli.CsCLI.VERSION;
import static io.carbynestack.cli.util.ExitCodes.success;
import static java.lang.System.getProperty;

public class VersionRunner implements CommandRunner<NoArg> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArg, Common common) {
        common.out().println(getOutput());
        return success();
    }

    @Stub
    String getOutput() {
        return String.format(getText(), VERSION, CommandLine.VERSION, getProperty("java.version"),
                getProperty("java.vendor"), getProperty("java.vm.name"), getProperty("java.vm.version"),
                getProperty("os.name"), getProperty("os.version"), getProperty("os.arch"), getLocale());
    }

    @Stub
    String getText() {
        return """
                @|bold Command Line Interface to interact with Carbyne Stack Virtual Clouds|@
                Carbyne Stack CLI: %s
                                
                @|bold Dependencies:|@
                Picocli: %s
                Amphora: 0.1-SNAPSHOT-1261403362-2-41864d
                Castor: 0.1-SNAPSHOT-1261403451-2-78f5f5b
                Ephemeral: 0.1-SNAPSHOT-1261324039-3-d2504ed
                                
                @|bold Runtime & Environment:|@
                JVM: %s (%s %s %s)
                OS: %s %s %s
                Locale: %s""";
    }

    /**
     * Returns the tag, language, and country of the current environment locale.
     * The language and country names are given in English if available.
     *
     * @return The composed locale string sequence.
     * @since 0.1.0
     */
    @Stub
    String getLocale() {
        var locale = Locale.getDefault();
        var target = new Locale("en", "US");
        return String.format("%s (%s %s)", locale.toLanguageTag(),
                locale.getDisplayLanguage(target), locale.getDisplayCountry(target));
    }
}
