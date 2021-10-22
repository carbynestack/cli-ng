package io.carbynestack.common;

import org.junit.jupiter.api.Test;

import static io.carbynestack.testing.result.ResultAssert.assertThat;

public class CsFailureReasonTest {
    @Test
    public void toFailure() {
        record TestReason(String synopsis, String description) implements CsFailureReason {}
        assertThat(new TestReason("synopsis", "description").toFailure()).isFailure();
    }
}
