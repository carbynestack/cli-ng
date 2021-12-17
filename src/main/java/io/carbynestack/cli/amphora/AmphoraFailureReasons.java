/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora;

import io.carbynestack.common.CsFailureReason;

public enum AmphoraFailureReasons implements CsFailureReason {
    ENDPOINTS("The resolving of amphora endpoints failed.",
            "The resolving of amphora endpoints failed."),
    CLIENT_INIT("The amphora client initialization failed.",
            "The amphora client initialization failed."),
    COMMUNICATION("The communication with at least one of the "
            + "defined amphora services failed.",
            "The communication with at least one of the "
                    + "defined amphora services failed."),
    MISSING_SECRETS("The resolving of the input secrets failed.",
            "The resolving of the input secrets failed."),
    SECRET_UPLOAD("The secret creation failed.",
            "Either the communication with at least one of "
                    + "the defined amphora services failed or there already "
                    + "exists a secret with the given id."),
    SECRET_FETCHING("The fetching of the secret failed.",
            "Either the communication with at least one of "
                    + "the defined amphora services failed or no secret "
                    + "with the given id exists."),
    SECRET_DELETION("The deletion of the secrets partially failed.",
            "The deletion of the secrets partially failed."),
    MISSING_TAG("The resolving of a tag failed.",
            "The resolving of a tag failed."),
    TAG_CREATION("The tag creation failed.",
            "Either the communication with at least one of "
                    + "the defined amphora services failed or there already "
                    + "exists a tag with the given key."),
    TAG_FETCHING("The fetching of the tag failed.",
            "Either the communication with at least one of "
                    + "the defined amphora services failed or no secret "
                    + "with the given id exists or no tag with the given "
                    + "key exists."),
    TAG_DELETION("The deletion of the tag failed.",
            "The deletion of the tag failed."),
    TAG_UPDATE("The updating of the tag value failed.",
            "Either the communication with at least one of "
                    + "the defined amphora services failed or no secret "
                    + "with the given id exists or no tag with the given "
                    + "key exists."),
    TAG_OVERWRITE("The replacement of the tags failed.",
            "Either the communication with at least one of "
                    + "the defined amphora services failed or no secret "
                    + "with the given id exists.");

    private final String synopsis;
    private final String description;

    AmphoraFailureReasons(String synopsis, String description) {
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
