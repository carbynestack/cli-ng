/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.carbynestack.common.Generated;
import io.carbynestack.common.Stub;
import io.carbynestack.common.Tuple.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

/**
 * Represents a {@link Resolvable} resolver.
 *
 * @since 0.5.0
 */
public record Resolver(Map<String, String> supplied) {
    /**
     * The provider config key pattern with groups for the
     * provider name and service urls.
     *
     * @since 0.5.0
     */
    static final Pattern PROVIDER_PATTERN = Pattern
            .compile("vcp(.+)(amphoraserviceurl|baseurl|castorserviceurl|ephemeralserviceurl)");
    /**
     * The environment used for variable lookup (default: System::getenv)
     *
     * @since 0.5.0
     */
    @Stub
    static UnaryOperator<String> environment = System::getenv;

    /**
     * Creates a {@code Resolver} instance.
     *
     * @return an instance
     * @since 0.5.0
     */
    public static Resolver config() {
        return new Resolver(new HashMap<>());
    }

    /**
     * Creates a {@code Resolver} instance from a config file.
     *
     * @param src the config file
     * @return an instance
     * @since 0.5.0
     */
    public static Resolver config(File src) {
        try {
            var output = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
            var raw = new ObjectMapper()
                    .readValue(src, new TypeReference<Map<String, Object>>() {
                    });

            output.putAll(flattenEntries(raw.entrySet().stream())
                    .collect(Collectors.toUnmodifiableMap(Entry::getKey,
                            entry -> entry.getValue().toString())));

            return new Resolver(output);
        } catch (IOException ignored) {
            //TODO log loading failure
            return config();
        }
    }

    /**
     * Returns the combined and flattened entry key of the parent
     * and child entries.
     *
     * @param parent the parent entry
     * @param child  the child entry
     * @return the combined and flattened entry key
     * @since 0.5.0
     */
    @Stub
    static String flattenedEntryKey(Entry<String, Object> parent, Entry<String, Object> child) {
        return parent.getKey() +
                Character.toTitleCase(child.getKey().charAt(0))
                + child.getKey().substring(1);
    }

    /**
     * Returns a flattened version of the supplied entry stream using
     * {@link #flattenedEntryKey(Entry, Entry)}.
     *
     * @param stream the nested entry stream
     * @return the flattened entry stream
     * @since 0.5.0
     */
    @Stub
    @SuppressWarnings("unchecked")
    static Stream<Entry<String, Object>> flattenEntries(Stream<Entry<String, Object>> stream) {
        record EntryImpl<K, V>(K getKey, V getValue) implements Entry<K, V> {
            @Override
            @Generated
            public V setValue(V value) {
                throw new UnsupportedOperationException();
            }
        }

        return stream.flatMap(entry -> {
            if (entry.getValue() instanceof Map map) {
                return flattenEntries(((Map<String, Object>) map).entrySet().stream()
                        .filter(nested -> nested.getKey() instanceof String)
                        .map(nested -> new EntryImpl<>(flattenedEntryKey(entry, nested),
                                nested.getValue())));
            } else if (entry.getValue() instanceof ArrayList list) {
                return Stream.of(new EntryImpl<>(entry.getKey(), list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","))));
            } else {
                return Stream.of(entry);
            }
        });
    }

    /**
     * Returns all provider names present in the config file.
     *
     * @return the provider names
     * @since 0.5.0
     */
    public Collection<String> providers() {
        return this.supplied.keySet().stream()
                .map(String::toLowerCase)
                .map(PROVIDER_PATTERN::matcher)
                .filter(Matcher::matches)
                .map(matcher -> matcher.group(1))
                .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Resolves a {@link Resolvable} into an one arity
     * {@link With} record.
     *
     * @param r   the {@code Resolvable}
     * @param <E> the {@code Resolvable} value type
     * @return the created {@code With} instance
     * @since 0.5.0
     */
    public <E> With<E> resolve(Resolvable<E> r) {
        return new With<>(this, r);
    }

    /**
     * Resolves a {@link Resolvable} into a two arity
     * {@link With2} record.
     *
     * @param r1   the first resolvable
     * @param r2   the second resolvable
     * @param <E1> the first resolvable value type
     * @param <E2> the second resolvable value type
     * @return the created {@code With2} instance
     * @since 0.5.0
     */
    public <E1, E2> With2<E1, E2> resolve(Resolvable<E1> r1, Resolvable<E2> r2) {
        return new With2<>(this, r1, r2);
    }

    /**
     * Returns an {@link Optional} containing the result of an
     * environment lookup for the {@link Resolvable#environmentKey()}.
     *
     * @param resolvable the {@code Resolvable}
     * @param <T>        the {@code Resolvable} value type
     * @return an {@code Optional} with the lookup result or
     * {@link Optional#empty()}
     * @since 0.5.0
     */
    private <T> Optional<T> resolveEnvironment(Resolvable<T> resolvable) {
        return ofNullable(environment.apply(resolvable.environmentKey()))
                .flatMap(resolvable::parse);
    }

    /**
     * Returns an {@link Optional} containing the result of a
     * config lookup for the {@link Resolvable#configKey()}.
     *
     * @param resolvable the {@code Resolvable}
     * @param <T>        the {@code Resolvable} value type
     * @return an {@code Optional} with the lookup result or
     * {@link Optional#empty()}
     * @since 0.5.0
     */
    private <T> Optional<T> resolveSupplied(Resolvable<T> resolvable) {
        return ofNullable(supplied.get(resolvable.configKey()))
                .flatMap(resolvable::parse);
    }

    /**
     * Represents the intermediate result of an one arity
     * resolve call.
     *
     * @param <E> the {@link Resolvable} value type
     * @since 0.5.0
     */
    public static record With<E>(Resolver resolver, Resolvable<E> resolvable) {
        /**
         * Returns the supplied {@link Optional} if not
         * empty or the result of an environment lookup or
         * the result of a config entry lookup or an empty
         * {@code Optional} if no value could be found and
         * parsed.
         *
         * @param value the default value
         * @return an {@code Optional} with the lookup result or
         * {@link Optional#empty()}
         * @since 0.5.0
         */
        public E with(Optional<E> value) {
            return value.or(() -> resolver.resolveEnvironment(resolvable))
                    .or(() -> resolver.resolveSupplied(resolvable))
                    .orElse(null);
        }
    }

    /**
     * Represents the intermediate result of a two arity
     * resolve call.
     *
     * @param <E1> the first resolvable value type
     * @param <E2> the second resolvable value type
     * @since 0.5.0
     */
    public static record With2<E1, E2>(Resolver resolver, Resolvable<E1> r1, Resolvable<E2> r2) {
        /**
         * Returns the supplied {@link Optional} if not
         * empty or the result of an environment lookup or
         * the result of a config entry lookup or an null
         * filled {@link Tuple2} if no values could be found
         * and parsed.
         *
         * @param v1 the first default value
         * @param v2 the second default value
         * @return a {@code Tuple2} with the lookup results
         * @since 0.5.0
         */
        public Tuple2<E1, E2> with(Optional<E1> v1, Optional<E2> v2) {
            return new Tuple2<>(resolver.resolve(r1).with(v1),
                    resolver.resolve(r2).with(v2));
        }
    }
}
