/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.ephemeral;

import io.carbynestack.common.CsFailureReason;

public enum EphemeralFailureReasons implements CsFailureReason {
    //TODO should be fixed by extending the environment resolution process
    ENDPOINTS("The resolving of ephemeral endpoints failed.",
            "The resolving of ephemeral endpoints failed."),
    READ_CODE("The reading of the function code failed.",
            "No function code found in the collected input stream data."),
    CLIENT_INIT("The ephemeral multi-client initialization failed.",
            "The ephemeral multi-client initialization failed."),
    ACTIVATION("The activation of the backend ephemeral service failed.",
            "An HTTP error code was returned from a failed execution on the "
                    + "server-side, or an error occurred on the network or representation layer."),
    NO_RESULTS("The execution resulted in no outputs.",
            "The execution resulted in no outputs."),
    DIFFERENT_RESULTS("The clients returned different results.",
            "The clients returned different results.");

    private final String synopsis;
    private final String description;

    EphemeralFailureReasons(String synopsis, String description) {
        this.synopsis = synopsis;
        this.description = description;
    }

    @Override
    public String synopsis() {
        return synopsis;
    }

    @Override
    public String description() {
        return description;
    }
}
