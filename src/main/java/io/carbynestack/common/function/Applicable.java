/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.common.function;

import io.carbynestack.common.function.Functions.*;

/**
 * Represents an applicable that accepts zero or more arguments and produces a result.
 *
 * @since 0.5.0
 */
public interface Applicable {

    /**
     * Represents an applicable that accepts zero arguments and produces a result. This is the
     * zero-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable0#apply()}.</p>
     *
     * @param <R> the type of the result of the applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable0<R> extends Applicable {

        /**
         * Applies the function.
         *
         * @return the function result
         * @since 0.5.0
         */
        R apply();

    }

    /**
     * Represents an applicable that accepts one argument and produces a result. This is the
     * one-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable1#apply(Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <R>  the type of the result of the applicable
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable1<A1, R> extends Applicable {

        /**
         * Applies the function to the given argument.
         *
         * @param a1 the first function argument
         * @return the function result
         * @since 0.5.0
         */
        R apply(A1 a1);

    }

    /**
     * Represents an applicable that accepts two arguments and produces a result. This is the
     * two-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable2#apply(Object, Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <A2> the type of the second argument to the applicable
     * @param <R>  the type of the result of the applicable
     * @see Function2
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable2<A1, A2, R> extends Applicable {

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @return the function result
         * @since 0.5.0
         */
        R apply(A1 a1, A2 a2);

    }

    /**
     * Represents an applicable that accepts three arguments and produces a result. This is the
     * three-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable3#apply(Object, Object, Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <A2> the type of the second argument to the applicable
     * @param <A3> the type of the third argument to the applicable
     * @param <R>  the type of the result of the applicable
     * @see Function3
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable3<A1, A2, A3, R> extends Applicable {

        /**
         * Applies the function to the given arguments.
         *
         * @param a1 the first function argument
         * @param a2 the second function argument
         * @param a3 the third function argument
         * @return the function result
         * @since 0.5.0
         */
        R apply(A1 a1, A2 a2, A3 a3);

    }

    /**
     * Represents an applicable that accepts four arguments and produces a result. This is the
     * four-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable4#apply(Object, Object, Object, Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <A2> the type of the second argument to the applicable
     * @param <A3> the type of the third argument to the applicable
     * @param <A4> the type of the fourth argument to the applicable
     * @param <R>  the type of the result of the applicable
     * @see Function4
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable4<A1, A2, A3, A4, R> extends Applicable {

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
        R apply(A1 a1, A2 a2, A3 a3, A4 a4);

    }

    /**
     * Represents an applicable that accepts five arguments and produces a result. This is the
     * five-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable5#apply(Object, Object, Object, Object, Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <A2> the type of the second argument to the applicable
     * @param <A3> the type of the third argument to the applicable
     * @param <A4> the type of the fourth argument to the applicable
     * @param <A5> the type of the fifth argument to the applicable
     * @param <R>  the type of the result of the applicable
     * @see Function5
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable5<A1, A2, A3, A4, A5, R> extends Applicable {

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
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5);

    }

    /**
     * Represents an applicable that accepts six arguments and produces a result. This is the
     * six-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable6#apply(Object, Object, Object, Object, Object, Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <A2> the type of the second argument to the applicable
     * @param <A3> the type of the third argument to the applicable
     * @param <A4> the type of the fourth argument to the applicable
     * @param <A5> the type of the fifth argument to the applicable
     * @param <A6> the type of the sixth argument of the applicable
     * @param <R>  the type of the result of the applicable
     * @see Function6
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable6<A1, A2, A3, A4, A5, A6, R> extends Applicable {

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
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6);

    }

    /**
     * Represents an applicable that accepts seven arguments and produces a result. This is the
     * seven-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable7#apply(Object, Object, Object, Object, Object, Object, Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <A2> the type of the second argument to the applicable
     * @param <A3> the type of the third argument to the applicable
     * @param <A4> the type of the fourth argument to the applicable
     * @param <A5> the type of the fifth argument to the applicable
     * @param <A6> the type of the sixth argument of the applicable
     * @param <A7> the type of the seventh argument of the applicable
     * @param <R>  the type of the result of the applicable
     * @see Function7
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable7<A1, A2, A3, A4, A5, A6, A7, R> extends Applicable {

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
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7);

    }

    /**
     * Represents an applicable that accepts eight arguments and produces a result. This is the
     * eight-arity specialization of {@link Applicable}.
     *
     * <p>This is a functional interface whose functional method is {@link
     * Applicable8#apply(Object, Object, Object, Object, Object, Object, Object, Object)}.</p>
     *
     * @param <A1> the type of the first argument to the applicable
     * @param <A2> the type of the second argument to the applicable
     * @param <A3> the type of the third argument to the applicable
     * @param <A4> the type of the fourth argument to the applicable
     * @param <A5> the type of the fifth argument to the applicable
     * @param <A6> the type of the sixth argument of the applicable
     * @param <A7> the type of the seventh argument of the applicable
     * @param <A8> the type of the eighth argument of the applicable
     * @param <R>  the type of the result of the applicable
     * @see Function8
     * @since 0.5.0
     */
    @FunctionalInterface
    interface Applicable8<A1, A2, A3, A4, A5, A6, A7, A8, R> extends Applicable {

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
        R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6, A7 a7, A8 a8);

    }

}
