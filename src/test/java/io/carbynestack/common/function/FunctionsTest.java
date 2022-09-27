/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

import io.carbynestack.common.Tuple.*;
import io.carbynestack.common.function.Functions.*;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionsTest {
    @Test
    void givenSumBiFunctionWhenCallingFromOnFunctionsThenReturnFunction2Equivalent() {
        BiFunction<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.apply(12, 21)).isEqualTo(33);
        assertThat(Functions.from(f).apply(12, 21)).isEqualTo(33);
    }

    @Test
    void givenSumFunction2WhenCallingToOnFunctionsThenReturnBiFunctionEquivalent() {
        Function2<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.apply(12, 21)).isEqualTo(33);
        assertThat(Functions.to(f).apply(12, 21)).isEqualTo(33);
    }

    @Test
    void givenTuple2InputWhenCallingApplyOnFunction2ThenReturnComputedValue() {
        Function2<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.apply(new Tuple2<>(12, 21))).isEqualTo(33);
    }

    @Test
    void givenSumFunction2WhenCallingAndThenOnFunction2ThenReturnComposedFunction2() {
        Function2<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.andThen(v -> -1 * v).apply(12, 21)).isEqualTo(-33);
    }

    @Test
    void givenTuple3InputWhenCallingApplyOnFunction3ThenReturnComputedValue() {
        Function3<Integer, Integer, Integer, Integer> f = (a, b, c) -> a + b + c;
        assertThat(f.apply(new Tuple3<>(12, 21, 34))).isEqualTo(67);
    }

    @Test
    void givenSumFunction3WhenCallingAndThenOnFunction3ThenReturnComposedFunction3() {
        Function3<Integer, Integer, Integer, Integer> f = (a, b, c) -> a + b + c;
        assertThat(f.andThen(v -> -1 * v).apply(12, 21, 34)).isEqualTo(-67);
    }

    @Test
    void givenTuple4InputWhenCallingApplyOnFunction4ThenReturnComputedValue() {
        Function4<Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d) -> a + b + c + d;
        assertThat(f.apply(new Tuple4<>(12, 21, 34, 43))).isEqualTo(110);
    }

    @Test
    void givenSumFunction4WhenCallingAndThenOnFunction4ThenReturnComposedFunction4() {
        Function4<Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d) -> a + b + c + d;
        assertThat(f.andThen(v -> -1 * v).apply(12, 21, 34, 43)).isEqualTo(-110);
    }

    @Test
    void givenTuple5InputWhenCallingApplyOnFunction5ThenReturnComputedValue() {
        Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d, e) -> a + b
                + c + d + e;
        assertThat(f.apply(new Tuple5<>(1, 1, 1, 1, 1))).isEqualTo(5);
    }

    @Test
    void givenSumFunction5WhenCallingAndThenOnFunction5ThenReturnComposedFunction5() {
        Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d, e) -> a + b
                + c + d + e;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1)).isEqualTo(-5);
    }

    @Test
    void givenTuple6InputWhenCallingApplyOnFunction6ThenReturnComputedValue() {
        Function6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6) ->
                a1 + a2 + a3 + a4 + a5 + a6;
        assertThat(f.apply(new Tuple6<>(1, 1, 1, 1, 1, 1))).isEqualTo(6);
    }

    @Test
    void givenSumFunction6WhenCallingAndThenOnFunction6ThenReturnComposedFunction6() {
        Function6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6) ->
                a1 + a2 + a3 + a4 + a5 + a6;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1, 1)).isEqualTo(-6);
    }

    @Test
    void givenTuple7InputWhenCallingApplyOnFunction7ThenReturnComputedValue() {
        Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7;
        assertThat(f.apply(new Tuple7<>(1, 1, 1, 1, 1, 1, 1))).isEqualTo(7);
    }

    @Test
    void givenSumFunction7WhenCallingAndThenOnFunction7ThenReturnComposedFunction7() {
        Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1, 1, 1)).isEqualTo(-7);
    }

    @Test
    void givenTuple8InputWhenCallingApplyOnFunction8ThenReturnComputedValue() {
        Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7, a8) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8;
        assertThat(f.apply(new Tuple8<>(1, 1, 1, 1, 1, 1, 1, 1))).isEqualTo(8);
    }

    @Test
    void givenSumFunction8WhenCallingAndThenOnFunction8ThenReturnComposedFunction8() {
        Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7, a8) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1, 1, 1, 1)).isEqualTo(-8);
    }
}
