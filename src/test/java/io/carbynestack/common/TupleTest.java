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
    void givenTwoValuesWhenCreatingTuple2ThenInitializeComponents() {
        var tuple = new Tuple2<>(12, 21);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
    }

    @Test
    void givenClassWithTwoParameterConstructorWhenCallingAsOnTuple2ThenCreateClassInstance() {
        record IntTuple2(int e1, int e2) {
        }
        var tuple = new Tuple2<>(12, 21).as(IntTuple2::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
    }

    @Test
    void givenExtraValueWhenCallingCombineOnTuple2ThenReturnCombinedTuple3() {
        var tuple = new Tuple2<>(12, 21).combine(34);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
    }

    @Test
    void givenThreeValuesWhenCreatingTuple3ThenInitializeComponents() {
        var tuple = new Tuple3<>(12, 21, 34);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
    }

    @Test
    void givenClassWithThreeParameterConstructorWhenCallingAsOnTuple3ThenCreateClassInstance() {
        record IntTuple3(int e1, int e2, int e3) {
        }
        var tuple = new Tuple3<>(12, 21, 34).as(IntTuple3::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
    }

    @Test
    void givenExtraValueWhenCallingCombineOnTuple3ThenReturnCombinedTuple4() {
        var tuple = new Tuple3<>(12, 21, 34).combine(43);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
    }

    @Test
    void givenFourValuesWhenCreatingTuple4ThenInitializeComponents() {
        var tuple = new Tuple4<>(12, 21, 34, 43);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
    }

    @Test
    void givenClassWithFourParameterConstructorWhenCallingAsOnTuple4ThenCreateClassInstance() {
        record IntTuple4(int e1, int e2, int e3, int e4) {
        }
        var tuple = new Tuple4<>(12, 21, 34, 43).as(IntTuple4::new);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
    }

    @Test
    void givenExtraValueWhenCallingCombineOnTuple4ThenReturnCombinedTuple5() {
        var tuple = new Tuple4<>(12, 21, 34, 43).combine(56);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
    }

    @Test
    void givenFiveValuesWhenCreatingTuple5ThenInitializeComponents() {
        var tuple = new Tuple5<>(12, 21, 34, 43, 56);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
    }

    @Test
    void givenClassWithFiveParameterConstructorWhenCallingAsOnTuple5ThenCreateClassInstance() {
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
    void givenExtraValueWhenCallingCombineOnTuple5ThenReturnCombinedTuple6() {
        var tuple = new Tuple5<>(12, 21, 34, 43, 56).combine(65);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
    }

    @Test
    void givenSixValuesWhenCreatingTuple6ThenInitializeComponents() {
        var tuple = new Tuple6<>(12, 21, 34, 43, 56, 65);
        assertThat(tuple.e1()).isEqualTo(12);
        assertThat(tuple.e2()).isEqualTo(21);
        assertThat(tuple.e3()).isEqualTo(34);
        assertThat(tuple.e4()).isEqualTo(43);
        assertThat(tuple.e5()).isEqualTo(56);
        assertThat(tuple.e6()).isEqualTo(65);
    }

    @Test
    void givenClassWithSixParameterConstructorWhenCallingAsOnTuple6ThenCreateClassInstance() {
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
    void givenExtraValueWhenCallingCombineOnTuple6ThenReturnCombinedTuple7() {
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
    void givenSevenValuesWhenCreatingTuple7ThenInitializeComponents() {
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
    void givenClassWithSevenParameterConstructorWhenCallingAsOnTuple7ThenCreateClassInstance() {
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
    void givenExtraValueWhenCallingCombineOnTuple7ThenReturnCombinedTuple8() {
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
    void givenEightValuesWhenCreatingTuple8ThenInitializeComponents() {
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
    void givenClassWithEightParameterConstructorWhenCallingAsOnTuple8ThenCreateClassInstance() {
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
