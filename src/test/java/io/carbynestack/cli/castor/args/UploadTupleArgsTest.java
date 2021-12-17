/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor.args;

import io.carbynestack.castor.common.entities.TupleType;
import io.carbynestack.testing.blankstring.EmptyOrBlankStringSource;
import io.carbynestack.testing.nullable.NullableParamSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

import static io.carbynestack.castor.common.entities.TupleType.MULTIPLICATION_TRIPLE_GFP;
import static io.carbynestack.cli.castor.CastorFailureReasons.INVALID_TUPLE_DATA;
import static io.carbynestack.cli.castor.CastorFailureReasons.READ_FILE;
import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UploadTupleArgsTest {
    private static final String PROVIDER_NAME = "apollo";
    private static final File TUPLE_FILE = Path.of("", "src/test/resources")
            .resolve("triples-p-p0").toFile();
    private static final TupleType TUPLE_TYPE = MULTIPLICATION_TRIPLE_GFP;
    private static final UUID CHUNK_ID = UUID.randomUUID();

    @SuppressWarnings("unused")
    private static final Arguments PARAMS = Arguments.of(PROVIDER_NAME, TUPLE_FILE, TUPLE_TYPE, CHUNK_ID);

    @Test
    void constructor() {
        var args = new UploadTupleArgs(PROVIDER_NAME, TUPLE_FILE, TUPLE_TYPE, CHUNK_ID);
        assertThat(args.providerName()).isEqualTo(PROVIDER_NAME);
        assertThat(args.tupleFile()).isEqualTo(TUPLE_FILE);
        assertThat(args.tupleType()).isEqualTo(TUPLE_TYPE);
        assertThat(args.chunkId()).isEqualTo(CHUNK_ID);
    }

    @ParameterizedTest
    @EmptyOrBlankStringSource
    void constructorEmptyOrBlankProviderName(String providerName) {
        assertThatThrownBy(() -> new UploadTupleArgs(providerName, TUPLE_FILE, TUPLE_TYPE, CHUNK_ID))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provider name cannot be empty or blank!");
    }

    @ParameterizedTest
    @NullableParamSource("PARAMS")
    void constructorNullableValues(String providerName, File tupleFile, TupleType tupleType, UUID chunkId) {
        assertThatThrownBy(() -> new UploadTupleArgs(providerName, tupleFile, tupleType, chunkId))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void parseChunk() {
        assertThat(new UploadTupleArgs(PROVIDER_NAME, TUPLE_FILE, TUPLE_TYPE, CHUNK_ID)
                .parseChunk()).isSuccess();
    }

    @Test
    void parseChunkNotAFile() {
        assertThat(new UploadTupleArgs(PROVIDER_NAME, new File("tuples"),
                TUPLE_TYPE, CHUNK_ID).parseChunk()).hasReason(READ_FILE);
    }

    @Test
    void parseChunkInvalidTupleData() {
        assertThat(new UploadTupleArgs(PROVIDER_NAME, Path.of("", "src/test/resources")
                .resolve("triples").toFile(), TUPLE_TYPE, CHUNK_ID)
                .parseChunk()).hasReason(INVALID_TUPLE_DATA);
    }
}
