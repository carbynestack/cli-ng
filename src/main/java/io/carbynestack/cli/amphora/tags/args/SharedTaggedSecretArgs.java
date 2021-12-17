/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.amphora.tags.args;

import io.carbynestack.amphora.common.Tag;
import io.carbynestack.cli.amphora.AmphoraFailureReasons;
import io.carbynestack.common.result.Result;

import java.util.Map;
import java.util.UUID;

import static io.carbynestack.amphora.common.TagValueType.LONG;
import static io.carbynestack.amphora.common.TagValueType.STRING;
import static io.carbynestack.cli.amphora.AmphoraFailureReasons.MISSING_TAG;
import static java.lang.Long.parseLong;
import static java.util.Objects.requireNonNull;

public record SharedTaggedSecretArgs(UUID id, Map<String, String> rawTag) {
    public SharedTaggedSecretArgs {
        requireNonNull(id);
        requireNonNull(rawTag);
    }

    public Result<Tag, AmphoraFailureReasons> tag() {
        return Result.of(() -> rawTag.entrySet().stream()
                .findAny().orElseThrow(), MISSING_TAG).map(entry -> Tag.builder()
                .key(entry.getKey())
                .value(entry.getValue())
                .valueType(Result.of(() -> parseLong(entry.getValue()), "")
                        .fold(s -> LONG, r -> STRING))
                .build());
    }
}