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
    void testFrom() {
        BiFunction<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.apply(12, 21)).isEqualTo(33);
        assertThat(Functions.from(f).apply(12, 21)).isEqualTo(33);
    }

    @Test
    void testTo() {
        Function2<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.apply(12, 21)).isEqualTo(33);
        assertThat(Functions.to(f).apply(12, 21)).isEqualTo(33);
    }

    @Test
    void testFunction2ApplyTuple2() {
        Function2<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.apply(new Tuple2<>(12, 21))).isEqualTo(33);
    }

    @Test
    void testFunction2AndThen() {
        Function2<Integer, Integer, Integer> f = Integer::sum;
        assertThat(f.andThen(v -> -1 * v).apply(12, 21)).isEqualTo(-33);
    }

    @Test
    void testFunction3ApplyTuple3() {
        Function3<Integer, Integer, Integer, Integer> f = (a, b, c) -> a + b + c;
        assertThat(f.apply(new Tuple3<>(12, 21, 34))).isEqualTo(67);
    }

    @Test
    void testFunction3AndThen() {
        Function3<Integer, Integer, Integer, Integer> f = (a, b, c) -> a + b + c;
        assertThat(f.andThen(v -> -1 * v).apply(12, 21, 34)).isEqualTo(-67);
    }

    @Test
    void testFunction4ApplyTuple4() {
        Function4<Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d) -> a + b + c + d;
        assertThat(f.apply(new Tuple4<>(12, 21, 34, 43))).isEqualTo(110);
    }

    @Test
    void testFunction4AndThen() {
        Function4<Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d) -> a + b + c + d;
        assertThat(f.andThen(v -> -1 * v).apply(12, 21, 34, 43)).isEqualTo(-110);
    }

    @Test
    void testFunction5ApplyTuple5() {
        Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d, e) -> a + b
                + c + d + e;
        assertThat(f.apply(new Tuple5<>(1, 1, 1, 1, 1))).isEqualTo(5);
    }

    @Test
    void testFunction5AndThen() {
        Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (a, b, c, d, e) -> a + b
                + c + d + e;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1)).isEqualTo(-5);
    }

    @Test
    void testFunction6ApplyTuple6() {
        Function6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6) ->
                a1 + a2 + a3 + a4 + a5 + a6;
        assertThat(f.apply(new Tuple6<>(1, 1, 1, 1, 1, 1))).isEqualTo(6);
    }

    @Test
    void testFunction6AndThen() {
        Function6<Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6) ->
                a1 + a2 + a3 + a4 + a5 + a6;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1, 1)).isEqualTo(-6);
    }

    @Test
    void testFunction7ApplyTuple7() {
        Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7;
        assertThat(f.apply(new Tuple7<>(1, 1, 1, 1, 1, 1, 1))).isEqualTo(7);
    }

    @Test
    void testFunction7AndThen() {
        Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1, 1, 1)).isEqualTo(-7);
    }

    @Test
    void testFunction8ApplyTuple8() {
        Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7, a8) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8;
        assertThat(f.apply(new Tuple8<>(1, 1, 1, 1, 1, 1, 1, 1))).isEqualTo(8);
    }

    @Test
    void testFunction8AndThen() {
        Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> f = (a1, a2, a3, a4, a5, a6, a7, a8) ->
                a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8;
        assertThat(f.andThen(v -> -1 * v).apply(1, 1, 1, 1, 1, 1, 1, 1)).isEqualTo(-8);
    }
}
