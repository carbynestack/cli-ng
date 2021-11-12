/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.common;

import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;
import org.junit.jupiter.api.Test;

import static io.carbynestack.cli.util.ExitCodes.success;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandExecutorTest {
    @Test
    void constructor() {
        assertThatThrownBy(CommandExecutor::new)
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Instance creation of utility class CommandExecutor not permitted!");
    }

    @Test
    void execute() {
        assertThat(CommandExecutor.execute(() -> new TestRunner(success()), new NoArg(), new Common())).isZero();
        assertThat(CommandExecutor.execute(() ->
                new TestRunner(new Failure<Integer, CsFailureReason>(
                        new TestReason("first", "second"))), new NoArg(), new Common())).isEqualTo(3);
    }

    static final record TestReason(String synopsis, String description) implements CsFailureReason {
    }

    static final record TestRunner(Result<Integer, ? extends CsFailureReason> result) implements CommandRunner<NoArg> {
        @Override
        public Result<Integer, ? extends CsFailureReason> run(NoArg args, Common common) {
            return result;
        }
    }
}
