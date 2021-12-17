/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.environment.runners;

import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import static io.carbynestack.cli.util.ExitCodes.success;

public final class EnvironmentRunner implements CommandRunner<NoArg> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg args, Common common) {
        common.out().println("""
                Prior to the first use of the Carbyne Stack CLI it is required to run the configure command once:
                                  
                @|bold cs config|@
                                  
                This command will guide you through a step-by-step configuration to match your Virtual Cloud setup.
                While it is required to run the configuration at least once prior to the first command execution,
                it is possible to overload some settings using environment variables.
                The following environment variables can be used to adapt your configuration without overwriting the
                default config:
                                  
                @|bold CS_PRIME|@: Modulus N as used by the MPC backend
                @|bold CS_R|@: Auxiliary modulus R as used by the MPC backend
                @|bold CS_R_INV|@: Multiplicative inverse for the auxiliary modulus R as used by the MPC backend
                @|bold CS_NO_SSL_VALIDATION|@: Disable SSL certificate validation
                @|bold CS_VCP_{n}_BASE_URL|@: Base URL for the provider with ID "{n}"
                @|bold CS_VCP_{n}_AMPHORA_URL|@: Amphora Service URL for the provider with ID "{n}"
                @|bold CS_VCP_{n}_CASTOR_URL|@: Castor Service URL for the provider with ID "{n}"
                @|bold CS_VCP_{n}_EPHEMERAL_URL|@: Ephemeral Service URL for the provider with ID "{n}\"""");
        return success();
    }
}
