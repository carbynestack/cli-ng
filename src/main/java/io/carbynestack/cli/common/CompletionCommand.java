package io.carbynestack.cli.common;

import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;
import picocli.AutoComplete;
import picocli.CommandLine.Command;

import static io.carbynestack.cli.util.ExitCodes.success;

/**
 * Represents the auto-completion generation command that outputs a Bash/ZSH
 * completion script for the top-level command {@code cs}.
 *
 * @see AutoComplete.GenerateCompletion
 * @since 0.1.0
 */
@Command(name = "generate-completion", description = {
        "Generate bash/zsh completion script for ${ROOT-COMMAND-NAME:-the root command of this command}.",
        "Run the following command to give `${ROOT-COMMAND-NAME:-$PARENTCOMMAND}` TAB completion in the current shell:",
        "", "  source <(${PARENT-COMMAND-FULL-NAME:-$PARENTCOMMAND} ${COMMAND-NAME})", ""},
        optionListHeading = "Options:%n")
public class CompletionCommand extends DefaultCommandRunner {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArgs, Common common) {
        common.spec.commandLine().getOut().print(AutoComplete.bash(common.spec.root().name(),
                common.spec.root().commandLine()) + '\n');
        common.spec.commandLine().getOut().flush();
        return success();
    }
}
