/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

import io.carbynestack.common.Tuple.*;
import io.carbynestack.common.function.Applicable.*;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Functions with [2, 8] parameters.
 *
 * @since 0.5.0
 */
public interface Functions {

    /**
     * Converts a function of type {@code BiFunction} to a {@code Function2}.
     *
     * @param biFunction the function to convert
     * @param <A1>       the type of the first argument to the function
     * @param <A2>       the type of the second argument to the function
     * @param <R>        the type of the result of the function
     * @return the converted function of type {@code Function2}
     * @see BiFunction
     * @since 0.5.0
     */
    static <A1, A2, R> Function2<A1, A2, R> from(BiFunction<A1, A2, R> biFunction) {
        return biFunction::apply;
    }

    /**
     * Converts a function of type {@code Function2} to a {@code BiFunction}.
     *
     * @param function2 the function to convert
     * @param <A1>      the type of the first argument to the function
     * @param <A2>      the type of the second argument to the function
     * @param <R>       the type of the result of the function
     * @return the converted function of type {@code BiFunction}
     * @see BiFunction
     * @since 0.5.0
     */
    static <A1, A2, R> BiFunction<A1, A2, R> to(Function2<A1, A2, R> function2) {
        return function2::apply;
    }

    /**
     * Represents a function that accepts two arguments and produces a result. This is the two-arity
     * specialization of {@link Function}.
     *
     * @param <A1> the type of the first argument to the function
     * @param <A2> the type of the second argument to the function
     * @param <R>  the type of the result of the function
     * @see Function
     * @see Applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Function2<A1, A2, R> extends Applicable2<A1, A2, R>, Applicable1<Tuple2<A1, A2>, R> {

        /**
         * Applies the function to the given {@code Tuple}.
         *
         * @param tuple2 the first function argument
         * @return the function result
         * @see Tuple2
         * @since 0.5.0
         */
        @Override
        default R apply(Tuple2<A1, A2> tuple2) {
            return apply(tuple2.e1(), tuple2.e2());
        }

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @return the function result
         * @since 0.5.0
         */
        @Override
        R apply(A1 a1, A2 a2);

        /**
         * Returns a composed function that first applies this function to its input, and then
         * applies the {@code after} function to the result. If evaluation of either function throws
         * an exception, it is relayed to the caller of the composed function.
         *
         * @param <V>   the type of output of the {@code after} function, and of the composed
         *              function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then applies the {@code
         * after} function
         * @since 0.5.0
         */
        default <V> Function2<A1, A2, V> andThen(Function<? super R, ? extends V> after) {
            return (A1 a1, A2 a2) -> after.apply(apply(a1, a2));
        }

    }

    /**
     * Represents a function that accepts three arguments and produces a result. This is the
     * three-arity specialization of {@link Function}.
     *
     * @param <A1> the type of the first argument to the function
     * @param <A2> the type of the second argument to the function
     * @param <A3> the type of the third argument to the function
     * @param <R>  the type of the result of the function
     * @see Function
     * @see Applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Function3<A1, A2, A3, R> extends Applicable3<A1, A2, A3, R>, Applicable1<Tuple3<A1, A2, A3>, R> {

        /**
         * Applies the function to the given {@code Tuple}.
         *
         * @param tuple3 the first function argument
         * @return the function result
         * @see Tuple3
         * @since 0.5.0
         */
        @Override
        default R apply(Tuple3<A1, A2, A3> tuple3) {
            return apply(tuple3.e1(), tuple3.e2(), tuple3.e3());
        }

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @param a3 the third function argument
         * @return the function result
         * @since 0.5.0
         */
        @Override
        R apply(A1 a1, A2 a2, A3 a3);

        /**
         * Returns a composed function that first applies this function to its input, and then
         * applies the {@code after} function to the result. If evaluation of either function throws
         * an exception, it is relayed to the caller of the composed function.
         *
         * @param <V>   the type of output of the {@code after} function, and of the composed
         *              function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then applies the {@code
         * after} function
         * @since 0.5.0
         */
        default <V> Function3<A1, A2, A3, V> andThen(Function<? super R, ? extends V> after) {
            return (A1 a1, A2 a2, A3 a3) -> after.apply(apply(a1, a2, a3));
        }

    }

    /**
     * Represents a function that accepts four arguments and produces a result. This is the
     * four-arity specialization of {@link Function}.
     *
     * @param <A1> the type of the first argument to the function
     * @param <A2> the type of the second argument to the function
     * @param <A3> the type of the third argument to the function
     * @param <A4> the type of the fourth argument to the function
     * @param <R>  the type of the result of the function
     * @see Function
     * @see Applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Function4<A1, A2, A3, A4, R> extends Applicable4<A1, A2, A3, A4, R>, Applicable1<Tuple4<A1, A2, A3, A4>, R> {

        /**
         * Applies the function to the given {@code Tuple}.
         *
         * @param tuple4 the first function argument
         * @return the function result
         * @see Tuple4
         * @since 0.5.0
         */
        @Override
        default R apply(Tuple4<A1, A2, A3, A4> tuple4) {
            return apply(tuple4.e1(), tuple4.e2(), tuple4.e3(), tuple4.e4());
        }

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @param a3 the third function argument
         * @param a4 the fourth function argument
         * @return the function result
         * @since 0.5.0
         */
        @Override
        R apply(A1 a1, A2 a2, A3 a3, A4 a4);

        /**
         * Returns a composed function that first applies this function to its input, and then
         * applies the {@code after} function to the result. If evaluation of either function throws
         * an exception, it is relayed to the caller of the composed function.
         *
         * @param <V>   the type of output of the {@code after} function, and of the composed
         *              function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then applies the {@code
         * after} function
         * @since 0.5.0
         */
        default <V> Function4<A1, A2, A3, A4, V> andThen(Function<? super R, ? extends V> after) {
            return (A1 a1, A2 a2, A3 a3, A4 a4) -> after.apply(apply(a1, a2, a3, a4));
        }

    }

    /**
     * Represents a function that accepts five arguments and produces a result. This is the
     * five-arity specialization of {@link Function}.
     *
     * @param <A1> the type of the first argument to the function
     * @param <A2> the type of the second argument to the function
     * @param <A3> the type of the third argument to the function
     * @param <A4> the type of the fourth argument to the function
     * @param <A5> the type of the fifth argument to the function
     * @param <R>  the type of the result of the function
     * @see Function
     * @see Applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Function5<A1, A2, A3, A4, A5, R> extends Applicable5<A1, A2, A3, A4, A5, R>, Applicable1<Tuple5<A1, A2, A3, A4, A5>, R> {

        /**
         * Applies the function to the given {@code Tuple}.
         *
         * @param tuple5 the first function argument
         * @return the function result
         * @see Tuple5
         * @since 0.5.0
         */
        @Override
        default R apply(Tuple5<A1, A2, A3, A4, A5> tuple5) {
            return apply(tuple5.e1(), tuple5.e2(), tuple5.e3(), tuple5.e4(), tuple5.e5());
        }

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @param a3 the third function argument
         * @param a4 the fourth function argument
         * @param a5 the fifth function argument
         * @return the function result
         * @since 0.5.0
         */
        @Override
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5);

        /**
         * Returns a composed function that first applies this function to its input, and then
         * applies the {@code after} function to the result. If evaluation of either function throws
         * an exception, it is relayed to the caller of the composed function.
         *
         * @param <V>   the type of output of the {@code after} function, and of the composed
         *              function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then applies the {@code
         * after} function
         * @since 0.5.0
         */
        default <V> Function5<A1, A2, A3, A4, A5, V> andThen(Function<? super R, ? extends V> after) {
            return (A1 a1, A2 a2, A3 a3, A4 a4, A5 a5) -> after.apply(apply(a1, a2, a3, a4, a5));
        }

    }

    /**
     * Represents a function that accepts six arguments and produces a result. This is the six-arity
     * specialization of {@link Function}.
     *
     * @param <A1> the type of the first argument to the function
     * @param <A2> the type of the second argument to the function
     * @param <A3> the type of the third argument to the function
     * @param <A4> the type of the fourth argument to the function
     * @param <A5> the type of the fifth argument to the function
     * @param <A6> the type of the sixth argument of the function
     * @param <R>  the type of the result of the function
     * @see Function
     * @see Applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Function6<A1, A2, A3, A4, A5, A6, R> extends Applicable6<A1, A2, A3, A4, A5, A6, R>, Applicable1<Tuple6<A1, A2, A3, A4, A5, A6>, R> {

        /**
         * Applies the function to the given {@code Tuple}.
         *
         * @param tuple6 the first function argument
         * @return the function result
         * @see Tuple6
         * @since 0.5.0
         */
        @Override
        default R apply(Tuple6<A1, A2, A3, A4, A5, A6> tuple6) {
            return apply(tuple6.e1(), tuple6.e2(), tuple6.e3(), tuple6.e4(), tuple6.e5(),
                    tuple6.e6());
        }

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @param a3 the third function argument
         * @param a4 the fourth function argument
         * @param a5 the fifth function argument
         * @param a6 the sixth function argument
         * @return the function result
         * @since 0.5.0
         */
        @Override
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6);

        /**
         * Returns a composed function that first applies this function to its input, and then
         * applies the {@code after} function to the result. If evaluation of either function throws
         * an exception, it is relayed to the caller of the composed function.
         *
         * @param <V>   the type of output of the {@code after} function, and of the composed
         *              function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then applies the {@code
         * after} function
         * @since 0.5.0
         */
        default <V> Function6<A1, A2, A3, A4, A5, A6, V> andThen(Function<? super R, ? extends V> after) {
            return (A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6) -> after
                    .apply(apply(a1, a2, a3, a4, a5, a6));
        }

    }

    /**
     * Represents a function that accepts seven arguments and produces a result. This is the
     * seven-arity specialization of {@link Function}.
     *
     * @param <A1> the type of the first argument to the function
     * @param <A2> the type of the second argument to the function
     * @param <A3> the type of the third argument to the function
     * @param <A4> the type of the fourth argument to the function
     * @param <A5> the type of the fifth argument to the function
     * @param <A6> the type of the sixth argument of the function
     * @param <A7> the type of the seventh argument of the function
     * @param <R>  the type of the result of the function
     * @see Function
     * @see Applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Function7<A1, A2, A3, A4, A5, A6, A7, R> extends Applicable7<A1, A2, A3, A4, A5, A6, A7, R>, Applicable1<Tuple7<A1, A2, A3, A4, A5, A6, A7>, R> {

        /**
         * Applies the function to the given {@code Tuple}.
         *
         * @param tuple7 the first function argument
         * @return the function result
         * @see Tuple7
         * @since 0.5.0
         */
        @Override
        default R apply(Tuple7<A1, A2, A3, A4, A5, A6, A7> tuple7) {
            return apply(tuple7.e1(), tuple7.e2(), tuple7.e3(), tuple7.e4(), tuple7.e5(),
                    tuple7.e6(), tuple7.e7());
        }

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @param a3 the third function argument
         * @param a4 the fourth function argument
         * @param a5 the fifth function argument
         * @param a6 the sixth function argument
         * @param a7 the seventh function argument
         * @return the function result
         * @since 0.5.0
         */
        @Override
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7);

        /**
         * Returns a composed function that first applies this function to its input, and then
         * applies the {@code after} function to the result. If evaluation of either function throws
         * an exception, it is relayed to the caller of the composed function.
         *
         * @param <V>   the type of output of the {@code after} function, and of the composed
         *              function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then applies the {@code
         * after} function
         * @since 0.5.0
         */
        default <V> Function7<A1, A2, A3, A4, A5, A6, A7, V> andThen(Function<? super R, ? extends V> after) {
            return (A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7) -> after
                    .apply(apply(a1, a2, a3, a4, a5, a6, a7));
        }

    }

    /**
     * Represents a function that accepts eight arguments and produces a result. This is the
     * eight-arity specialization of {@link Function}.
     *
     * @param <A1> the type of the first argument to the function
     * @param <A2> the type of the second argument to the function
     * @param <A3> the type of the third argument to the function
     * @param <A4> the type of the fourth argument to the function
     * @param <A5> the type of the fifth argument to the function
     * @param <A6> the type of the sixth argument of the function
     * @param <A7> the type of the seventh argument of the function
     * @param <A8> the type of the eighth argument of the function
     * @param <R>  the type of the result of the function
     * @see Function
     * @see Applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Function8<A1, A2, A3, A4, A5, A6, A7, A8, R> extends Applicable8<A1, A2, A3, A4, A5, A6, A7, A8, R>, Applicable1<Tuple8<A1, A2, A3, A4, A5, A6, A7, A8>, R> {

        /**
         * Applies the function to the given {@code Tuple}.
         *
         * @param tuple8 the first function argument
         * @return the function result
         * @see Tuple8
         * @since 0.5.0
         */
        @Override
        default R apply(Tuple8<A1, A2, A3, A4, A5, A6, A7, A8> tuple8) {
            return apply(tuple8.e1(), tuple8.e2(), tuple8.e3(), tuple8.e4(), tuple8.e5(),
                    tuple8.e6(), tuple8.e7(), tuple8.e8());
        }

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @param a3 the third function argument
         * @param a4 the fourth function argument
         * @param a5 the fifth function argument
         * @param a6 the sixth function argument
         * @param a7 the seventh function argument
         * @param a8 the eighth function argument
         * @return the function result
         * @since 0.5.0
         */
        @Override
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7, A8 a8);

        /**
         * Returns a composed function that first applies this function to its input, and then
         * applies the {@code after} function to the result. If evaluation of either function throws
         * an exception, it is relayed to the caller of the composed function.
         *
         * @param <V>   the type of output of the {@code after} function, and of the composed
         *              function
         * @param after the function to apply after this function is applied
         * @return a composed function that first applies this function and then applies the {@code
         * after} function
         * @since 0.5.0
         */
        default <V> Function8<A1, A2, A3, A4, A5, A6, A7, A8, V> andThen(Function<? super R, ? extends V> after) {
            return (A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7, A8 a8) -> after
                    .apply(apply(a1, a2, a3, a4, a5, a6, a7, a8));
        }

    }

}