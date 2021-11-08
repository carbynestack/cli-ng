/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import org.junit.jupiter.api.Test;

import static io.carbynestack.testing.result.ResultAssert.assertThat;

class CsFailureReasonTest {
    @Test
    void toFailure() {
        record TestReason(String synopsis, String description) implements CsFailureReason {}
        assertThat(new TestReason("synopsis", "description").toFailure()).isFailure();
    }
}
