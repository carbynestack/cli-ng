/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor.args;

import io.carbynestack.castor.common.entities.TupleChunk;
import io.carbynestack.castor.common.entities.TupleType;
import io.carbynestack.cli.castor.CastorFailureReasons;
import io.carbynestack.common.result.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static io.carbynestack.cli.castor.CastorFailureReasons.INVALID_TUPLE_DATA;
import static io.carbynestack.cli.castor.CastorFailureReasons.READ_FILE;
import static java.util.Objects.requireNonNull;

public record UploadTupleArgs(String providerName, File tupleFile, TupleType tupleType,
                              UUID chunkId) implements CastorSharedArgs {
    public UploadTupleArgs {
        if (requireNonNull(providerName).isBlank())
            throw new IllegalArgumentException("Provider name cannot be empty or blank!");
        requireNonNull(tupleFile);
        requireNonNull(tupleType);
        requireNonNull(chunkId);
    }

    public Result<TupleChunk<?, ?>, CastorFailureReasons> parseChunk() {
        try (var fileInputStream = new FileInputStream(tupleFile)) {
            return Result.of(() -> TupleChunk.of(tupleType.getTupleCls(), tupleType.getField(),
                    chunkId, fileInputStream.readAllBytes()), INVALID_TUPLE_DATA);
        } catch (IOException e) {
            return READ_FILE.toFailure();
        }
    }
}
