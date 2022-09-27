/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.resolve;

import io.carbynestack.common.Tuple.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

public class ResolverTest {
    private static final Resolver CONFIG_RESOLVER = Resolver.config(Path.of("", "src/test/resources")
            .resolve("config.json").toFile());

    @Test
    void whenCallingEnvironmentOnResolverThenReturnInstanceOfUnaryOperator() {
        assertThat(Resolver.environment).isInstanceOf(UnaryOperator.class);
    }

    @Test
    void givenDefaultInitializedResolverWhenCallingSuppliedOnResolverThenConfigIsEmpty() {
        assertThat(Resolver.config().supplied()).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"prime", "PRIME", "Prime", "r", "R", "rinv", "RINV", "Rinv",
            "trustedCertificates", "TRUSTEDCERTIFICATES", "TrustedCertificates", "noSslValidation",
            "NOSSLVALIDATION", "NoSslValidation", "vcpApolloAmphoraServiceUrl", "VCPAPOLLOAMPHORASERVICEURL",
            "VcpApolloAmphoraServiceUrl", "vcpApolloBaseUrl", "VCPAPOLLOBASEURL", "VcpApolloBaseUrl",
            "vcpApolloCastorServiceUrl", "VCPAPOLLOCASTORSERVICEURL", "VcpApolloCastorServiceUrl",
            "vcpApolloEphemeralServiceUrl", "VCPAPOLLOEPHEMERALSERVICEURL", "VcpApolloEphemeralServiceUrl",
            "vcpStarbuckAmphoraServiceUrl", "VCPSTARBUCKAMPHORASERVICEURL", "VcpStarbuckAmphoraServiceUrl",
            "vcpStarbuckBaseUrl", "VCPSTARBUCKBASEURL", "VcpStarbuckBaseUrl", "vcpStarbuckCastorServiceUrl",
            "VCPSTARBUCKCASTORSERVICEURL", "VcpStarbuckCastorServiceUrl", "vcpStarbuckEphemeralServiceUrl",
            "VCPSTARBUCKEPHEMERALSERVICEURL", "VcpStarbuckEphemeralServiceUrl"})
    void givenConfigurationKeysAndValidConfigWhenCallingSuppliedOnResolverThenConfigContainsAllKeys(String key) {
        assertThat(CONFIG_RESOLVER.supplied()).containsKey(key);
    }

    @Test
    void givenInvalidConfigPathWhenCallingConfigOnResolverThenConfigIsEmpty() {
        assertThat(Resolver.config(new File("missing.config")).supplied()).isEmpty();
    }

    @Test
    void givenEntriesOfWrongKeyTypeWhenCallingFlattenEntriesOnResolverReturnEmptyFlattenedStream() {
        assertThat(Resolver.flattenEntries(Map.of("test", (Object) Map.of(1, 2))
                .entrySet().stream())).isEmpty();
    }

    @Test
    void givenValidConfigWhenCallingProvidersOnResolverThenReturnConfiguredProviders() {
        assertThat(CONFIG_RESOLVER.providers()).containsExactlyInAnyOrder("apollo", "starbuck");
    }

    @Test
    void givenValidConfigAndPrimeKeyPathWhenCallingResolveOnResolverThenReturnWithBigIntegerResolver() {
        var with = CONFIG_RESOLVER.resolve(new IntResolvable("prime"));
        assertThat(with.resolvable()).isExactlyInstanceOf(IntResolvable.class);
        assertThat(with.resolver()).isEqualTo(CONFIG_RESOLVER);
    }

    @Test
    void givenValidConfigAndPrimeKeyPathAndRKeyPathWhenCallingResolveOnResolverThenReturnWith2BigIntegerResolver() {
        var with = CONFIG_RESOLVER.resolve(new IntResolvable("prime"),
                new IntResolvable("r"));
        assertThat(with.r1()).isExactlyInstanceOf(IntResolvable.class);
        assertThat(with.r2()).isExactlyInstanceOf(IntResolvable.class);
        assertThat(with.resolver()).isEqualTo(CONFIG_RESOLVER);
    }

    @Test
    void givenValidConfigAndPrimeKeyPathResolvableAndNoOverrideValueWhenCallingWithOnWithThenReturnExpectedBigInteger() {
        assertThat(CONFIG_RESOLVER.resolve(new IntResolvable("prime")).with(empty()))
                .isEqualTo(new BigInteger("198766463529478683931867765928436695041"));
    }

    @Test
    void givenValidConfigAndPrimeKeyPathResolvableAndRKeyPathResolvableAndNoOverrideValuesWhenCallingWithOnWith2ThenReturnExpectedBigIntegerTuple2() {
        assertThat(CONFIG_RESOLVER.resolve(new IntResolvable("prime"),
                new IntResolvable("r")).with(empty(), empty()))
                .isEqualTo(new Tuple2<>(new BigInteger("198766463529478683931867765928436695041"),
                        new BigInteger("141515903391459779531506841503331516415")));
    }

    @Test
    void givenRKeyPathResolvableAndEnvironmentContainingRAndOverrideValueWhenCallingWithOnWithThenReturnOverrideValue() {
        Resolver.environment = Map.of("CS_R", "1")::get;

        assertThat(CONFIG_RESOLVER.resolve(new IntResolvable("r"))
                .with(Optional.of(BigInteger.valueOf(0)))).isZero();

        Resolver.environment = System::getenv;
    }

    @Test
    void givenRKeyPathResolvableAndEnvironmentContainingRAndNoOverrideValueWhenCallingWithOnWithThenReturnExpectedEnvironmentValue() {
        Resolver.environment = Map.of("CS_R", "1")::get;

        assertThat(CONFIG_RESOLVER.resolve(new IntResolvable("r"))
                .with(empty())).isEqualTo(1);

        Resolver.environment = System::getenv;
    }

    @Test
    void givenValidConfigAndRKeyPathResolvableAndNoEnvironmentWhenCallingWithOnWithThenReturnExpectedConfigValue() {
        Resolver.environment = Collections.<String, String>emptyMap()::get;

        assertThat(CONFIG_RESOLVER.resolve(new IntResolvable("r"))
                .with(empty())).isEqualTo("141515903391459779531506841503331516415");

        Resolver.environment = System::getenv;
    }

    private static record IntResolvable(String keyPath) implements Resolvable<BigInteger> {
        @Override
        public Optional<BigInteger> parse(String value) {
            return Optional.ofNullable(value).map(BigInteger::new);
        }

        @Override
        public String synopsis() {
            return "testing";
        }
    }
}
