/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor;

import io.carbynestack.common.CsFailureReason;

public enum CastorFailureReasons implements CsFailureReason {
    READ_FILE("Failed to read tuples from provided file.",
            "Failed to read tuples from provided file."),
    INVALID_TUPLE_DATA("The provided tuple data is invalid.",
            "The length of tuple chunk data must be a multiple of 48 bytes!"),
    CHUNK_UPLOAD("The tuple chunk upload failed.",
            "Either the websocket connection could not be established, or the "
                    + "number of tuples in the given chunk exceeds the maximum number of "
                    + "allowed tuples per chunk."),
    ACTIVATE_CHUNK("The tuple chunk activation failed.",
            "The tuple chunk activation failed.");

    private final String synopsis;
    private final String description;

    CastorFailureReasons(String synopsis, String description) {
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
