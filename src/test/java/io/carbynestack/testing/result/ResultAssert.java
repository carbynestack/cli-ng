/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.result;

import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;
import io.carbynestack.common.result.Success;
import org.assertj.core.api.AbstractAssert;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@link Result} assertions based on {@link AbstractAssert}.
 *
 * @param <S> the success value type
 * @param <F> the failure reason type
 * @see Success
 * @see Failure
 * @since 0.1.0
 */
public class ResultAssert<S, F> extends AbstractAssert<ResultAssert<S, F>, Result<S, F>> {
    /**
     * The {@link AbstractAssert} required constructor.
     *
     * @param actual the actual value
     * @since 0.1.0
     */
    public ResultAssert(Result<S, F> actual) {
        super(actual, ResultAssert.class);
    }

    /**
     * Create assertion for {@link Result}.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     * @since 0.1.0
     */
    public static <S, F> ResultAssert<S, F> assertThat(Result<S, F> actual) {
        return new ResultAssert<>(actual);
    }

    /**
     * Verifies that the actual value is a {@link Success}.
     *
     * @since 0.1.0
     */
    public ResultAssert<S, F> isSuccess() {
        isNotNull();
        if (this.actual instanceof Failure<S, F>)
            failWithMessage("Expecting a success result but was: %s", this.actual);
        return this;
    }

    /**
     * Verifies that the actual value is a {@link Failure}.
     *
     * @since 0.1.0
     */
    public ResultAssert<S, F> isFailure() {
        isNotNull();
        if (this.actual instanceof Success<S, F>)
            failWithMessage("Expecting a failure result but was: %s", this.actual);
        return this;
    }

    /**
     * Verifies that the actual {@link Success} value is equal to the given one.
     *
     * @since 0.1.0
     */
    public ResultAssert<S, F> hasValue(S value) {
        isSuccess();
        if (this.actual instanceof Success<S, F> success && !Objects.equals(success.value(), value))
            failWithActualExpectedAndMessage(success.value(), value,
                    "Expecting result success value to equal %s but was: %s", value, success.value());
        return this;
    }

    /**
     * Verifies that the actual {@link Failure} reason is equal to the given one.
     *
     * @since 0.1.0
     */
    public ResultAssert<S, F> hasReason(F reason) {
        isFailure();
        if (this.actual instanceof Failure<S, F> failure && !Objects.equals(failure.reason(), reason))
            failWithActualExpectedAndMessage(failure.reason(), reason,
                    "Expecting result failure reason to equal %s but was: %s", reason, failure.reason());
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param actual             the actual value that was found during the test
     * @param expected           the value that was expected
     * @param errorMessageFormat the error message to format
     * @param arguments          the arguments referenced by the format specifiers in the message
     * @apiNote Transforms string arguments to be displayed in quotes and escapes line breaks
     * for more legible error messages.
     * @since 0.1.0
     */
    @Override
    protected void failWithActualExpectedAndMessage(Object actual, Object expected, String errorMessageFormat, Object... arguments) {
        super.failWithActualExpectedAndMessage(actual, expected, errorMessageFormat, Arrays.stream(arguments)
                .map(arg -> arg instanceof String ? ((String) arg)
                        .replaceAll(System.lineSeparator(), "\\\\n") : arg)
                .map(arg -> arg instanceof String ? String.format("'%s'", arg) : arg)
                .toArray());
    }
}
