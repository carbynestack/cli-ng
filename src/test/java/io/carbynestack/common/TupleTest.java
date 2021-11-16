/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import io.carbynestack.common.Tuple.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TupleTest {
    @Test
    void testTuple2() {
        var tuple = new Tuple2<>(12, 21);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
    }

    @Test
    void testTuple2As() {
        record IntTuple2(int e1, int e2) {
        }
        var tuple = new Tuple2<>(12, 21).as(IntTuple2::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
    }

    @Test
    void testTuple2Combine() {
        var tuple = new Tuple2<>(12, 21).combine(34);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
    }

    @Test
    void testTuple3() {
        var tuple = new Tuple3<>(12, 21, 34);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
    }

    @Test
    void testTuple3As() {
        record IntTuple3(int e1, int e2, int e3) {
        }
        var tuple = new Tuple3<>(12, 21, 34).as(IntTuple3::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
    }

    @Test
    void testTuple3Combine() {
        var tuple = new Tuple3<>(12, 21, 34).combine(43);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
    }

    @Test
    void testTuple4() {
        var tuple = new Tuple4<>(12, 21, 34, 43);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
    }

    @Test
    void testTuple4As() {
        record IntTuple4(int e1, int e2, int e3, int e4) {
        }
        var tuple = new Tuple4<>(12, 21, 34, 43).as(IntTuple4::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
    }

    @Test
    void testTuple4Combine() {
        var tuple = new Tuple4<>(12, 21, 34, 43).combine(56);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
    }

    @Test
    void testTuple5() {
        var tuple = new Tuple5<>(12, 21, 34, 43, 56);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
    }

    @Test
    void testTuple5As() {
        record IntTuple5(int e1, int e2, int e3, int e4, int e5) {
        }
        var tuple = new Tuple5<>(12, 21, 34, 43, 56).as(IntTuple5::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
    }

    @Test
    void testTuple5Combine() {
        var tuple = new Tuple5<>(12, 21, 34, 43, 56).combine(65);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
    }

    @Test
    void testTuple6() {
        var tuple = new Tuple6<>(12, 21, 34, 43, 56, 65);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
    }

    @Test
    void testTuple6As() {
        record IntTuple6(int e1, int e2, int e3, int e4, int e5, int e6) {
        }
        var tuple = new Tuple6<>(12, 21, 34, 43, 56, 65).as(IntTuple6::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
    }

    @Test
    void testTuple6Combine() {
        var tuple = new Tuple6<>(12, 21, 34, 43, 56, 65).combine(78);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
        assertThat(tuple.e7()).isEqualTo(78);
    }

    @Test
    void testTuple7() {
        var tuple = new Tuple7<>(12, 21, 34, 43, 56, 65, 78);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
        assertThat(tuple.e7()).isEqualTo(78);
    }

    @Test
    void testTuple7As() {
        record IntTuple7(int e1, int e2, int e3, int e4, int e5, int e6, int e7) {
        }
        var tuple = new Tuple7<>(12, 21, 34, 43, 56, 65, 78).as(IntTuple7::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
        assertThat(tuple.e7()).isEqualTo(78);
    }

    @Test
    void testTuple7Combine() {
        var tuple = new Tuple7<>(12, 21, 34, 43, 56, 65, 78).combine(87);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
        assertThat(tuple.e7()).isEqualTo(78);
        assertThat(tuple.e8()).isEqualTo(87);
    }

    @Test
    void testTuple8() {
        var tuple = new Tuple8<>(12, 21, 34, 43, 56, 65, 78, 87);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
        assertThat(tuple.e7()).isEqualTo(78);
        assertThat(tuple.e8()).isEqualTo(87);
    }

    @Test
    void testTuple8As() {
        record IntTuple8(int e1, int e2, int e3, int e4, int e5, int e6, int e7, int e8) {
        }
        var tuple = new Tuple8<>(12, 21, 34, 43, 56, 65, 78, 87).as(IntTuple8::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
        assertThat(tuple.e7()).isEqualTo(78);
        assertThat(tuple.e8()).isEqualTo(87);
    }
}
