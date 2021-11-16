/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common;

import io.carbynestack.common.function.Functions.*;

/**
 * Tuples of [2, 8] elements.
 *
 * @since 0.5.0
 */
public interface Tuple {

    /**
     * A tuple of 2 elements.
     *
     * @param <E1> the type of the first element
     * @param <E2> the type of the second element
     * @since 0.5.0
     */
    record Tuple2<E1, E2>(E1 e1, E2 e2) implements Tuple {

        /**
         * Converts a {@code Tuple2} to a value of type {@code T} using the converter {@code
         * Function2}.
         *
         * @param function2 the converter function
         * @param <T>       the conversion result type
         * @return the conversion result
         * @see Function2
         * @since 0.5.0
         */
        public <T> T as(Function2<E1, E2, T> function2) {
            return function2.apply(this);
        }

        /**
         * Combines a {@code Tuple2} with an additional element of type {@code T}.
         *
         * @param other the additional element
         * @param <T>   the additional element type
         * @return the combined tuple
         * @since 0.5.0
         */
        public <T> Tuple3<E1, E2, T> combine(T other) {
            return new Tuple3<>(e1, e2, other);
        }

    }

    /**
     * A tuple of 3 elements.
     *
     * @param <E1> the type of the first element
     * @param <E2> the type of the second element
     * @param <E3> the type of the third element
     * @since 0.5.0
     */
    record Tuple3<E1, E2, E3>(E1 e1, E2 e2, E3 e3) implements Tuple {

        /**
         * Converts a {@code Tuple3} to a value of type {@code T} using the converter {@code
         * Function3}.
         *
         * @param function3 the converter function
         * @param <T>       the conversion result type
         * @return the conversion result
         * @see Function3
         * @since 0.5.0
         */
        public <T> T as(Function3<E1, E2, E3, T> function3) {
            return function3.apply(this);
        }

        /**
         * Combines a {@code Tuple3} with an additional element of type {@code T}.
         *
         * @param other the additional element
         * @param <T>   the additional element type
         * @return the combined tuple
         * @since 0.5.0
         */
        public <T> Tuple4<E1, E2, E3, T> combine(T other) {
            return new Tuple4<>(e1, e2, e3, other);
        }

    }

    /**
     * A tuple of 4 elements.
     *
     * @param <E1> the type of the first element
     * @param <E2> the type of the second element
     * @param <E3> the type of the third element
     * @param <E4> the type of the fourth element
     * @since 0.5.0
     */
    record Tuple4<E1, E2, E3, E4>(E1 e1, E2 e2, E3 e3, E4 e4) implements Tuple {

        /**
         * Converts a {@code Tuple4} to a value of type {@code T} using the converter {@code
         * Function4}.
         *
         * @param function4 the converter function
         * @param <T>       the conversion result type
         * @return the conversion result
         * @see Function4
         * @since 0.5.0
         */
        public <T> T as(Function4<E1, E2, E3, E4, T> function4) {
            return function4.apply(this);
        }

        /**
         * Combines a {@code Tuple4} with an additional element of type {@code T}.
         *
         * @param other the additional element
         * @param <T>   the additional element type
         * @return the combined tuple
         * @since 0.5.0
         */
        public <T> Tuple5<E1, E2, E3, E4, T> combine(T other) {
            return new Tuple5<>(e1, e2, e3, e4, other);
        }

    }

    /**
     * A tuple of 5 elements.
     *
     * @param <E1> the type of the first element
     * @param <E2> the type of the second element
     * @param <E3> the type of the third element
     * @param <E4> the type of the fourth element
     * @param <E5> the type of the fifth element
     * @since 0.5.0
     */
    record Tuple5<E1, E2, E3, E4, E5>(E1 e1, E2 e2, E3 e3, E4 e4, E5 e5) implements Tuple {

        /**
         * Converts a {@code Tuple5} to a value of type {@code T} using the converter {@code
         * Function5}.
         *
         * @param function5 the converter function
         * @param <T>       the conversion result type
         * @return the conversion result
         * @see Function5
         * @since 0.5.0
         */
        public <T> T as(Function5<E1, E2, E3, E4, E5, T> function5) {
            return function5.apply(this);
        }

        /**
         * Combines a {@code Tuple5} with an additional element of type {@code T}.
         *
         * @param other the additional element
         * @param <T>   the additional element type
         * @return the combined tuple
         * @since 0.5.0
         */
        public <T> Tuple6<E1, E2, E3, E4, E5, T> combine(T other) {
            return new Tuple6<>(e1, e2, e3, e4, e5, other);
        }

    }

    /**
     * A tuple of 6 elements.
     *
     * @param <E1> the type of the first element
     * @param <E2> the type of the second element
     * @param <E3> the type of the third element
     * @param <E4> the type of the fourth element
     * @param <E5> the type of the fifth element
     * @param <E6> the type of the sixth element
     * @since 0.5.0
     */
    record Tuple6<E1, E2, E3, E4, E5, E6>(E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6) implements Tuple {

        /**
         * Converts a {@code Tuple6} to a value of type {@code T} using the converter {@code
         * Function6}.
         *
         * @param function6 the converter function
         * @param <T>       the conversion result type
         * @return the conversion result
         * @see Function6
         * @since 0.5.0
         */
        public <T> T as(Function6<E1, E2, E3, E4, E5, E6, T> function6) {
            return function6.apply(this);
        }

        /**
         * Combines a {@code Tuple6} with an additional element of type {@code T}.
         *
         * @param other the additional element
         * @param <T>   the additional element type
         * @return the combined tuple
         * @since 0.5.0
         */
        public <T> Tuple7<E1, E2, E3, E4, E5, E6, T> combine(T other) {
            return new Tuple7<>(e1, e2, e3, e4, e5, e6, other);
        }

    }

    /**
     * A tuple of 7 elements.
     *
     * @param <E1> the type of the first element
     * @param <E2> the type of the second element
     * @param <E3> the type of the third element
     * @param <E4> the type of the fourth element
     * @param <E5> the type of the fifth element
     * @param <E6> the type of the sixth element
     * @param <E7> the type of the seventh element
     * @since 0.5.0
     */
    record Tuple7<E1, E2, E3, E4, E5, E6, E7>(E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6, E7 e7) implements Tuple {

        /**
         * Converts a {@code Tuple7} to a value of type {@code T} using the converter {@code
         * Function7}.
         *
         * @param function7 the converter function
         * @param <T>       the conversion result type
         * @return the conversion result
         * @see Function7
         * @since 0.5.0
         */
        public <T> T as(Function7<E1, E2, E3, E4, E5, E6, E7, T> function7) {
            return function7.apply(this);
        }

        /**
         * Combines a {@code Tuple7} with an additional element of type {@code T}.
         *
         * @param other the additional element
         * @param <T>   the additional element type
         * @return the combined tuple
         * @since 0.5.0
         */
        public <T> Tuple8<E1, E2, E3, E4, E5, E6, E7, T> combine(T other) {
            return new Tuple8<>(e1, e2, e3, e4, e5, e6, e7, other);
        }

    }

    /**
     * A tuple of 8 elements.
     *
     * @param <E1> the type of the first element
     * @param <E2> the type of the second element
     * @param <E3> the type of the third element
     * @param <E4> the type of the fourth element
     * @param <E5> the type of the fifth element
     * @param <E6> the type of the sixth element
     * @param <E7> the type of the seventh element
     * @param <E8> the type of the eighth element
     * @since 0.5.0
     */
    record Tuple8<E1, E2, E3, E4, E5, E6, E7, E8>(E1 e1, E2 e2, E3 e3, E4 e4, E5 e5, E6 e6, E7 e7,
                                                  E8 e8) implements Tuple {

        /**
         * Converts a {@code Tuple8} to a value of type {@code T} using the converter {@code
         * Function8}.
         *
         * @param function8 the converter function
         * @param <T>       the conversion result type
         * @return the conversion result
         * @see Function8
         * @since 0.5.0
         */
        public <T> T as(Function8<E1, E2, E3, E4, E5, E6, E7, E8, T> function8) {
            return function8.apply(this);
        }

    }

}
