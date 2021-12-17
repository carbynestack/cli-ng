package io.carbynestack.cli.resolve;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;
import static java.util.function.Predicate.not;

public record SecretsValueResolvable(String synopsis, String description) implements Resolvable<BigInteger[]> {
    /**
     * Synopsis and description validation logic.
     *
     * @param synopsis    the resolvable synopsis
     * @param description the resolvable description
     * @since 0.9.0
     */
    public SecretsValueResolvable {
        if (requireNonNull(synopsis).isBlank())
            throw new IllegalArgumentException("Missing synopsis.");
        if (requireNonNull(description).isBlank())
            throw new IllegalArgumentException("Missing description.");
    }

    public SecretsValueResolvable() {
        this("The resolving of the input secrets failed.",
                "The resolving of the input secrets failed.");
    }

    @Override
    public Optional<BigInteger[]> parse(String value) {
        return Optional.ofNullable(value)
                .map(v -> Arrays.stream(v.split(lineSeparator()))
                        .map(String::strip)
                        .filter(not(String::isBlank))
                        .flatMap(secret -> {
                            try {
                                return Stream.of(new BigInteger(secret));
                            } catch (NumberFormatException ignored) {
                                return Stream.empty();
                            }
                        })
                        .toArray(BigInteger[]::new))
                .flatMap(a -> a.length > 0 ? Optional.of(a)
                        : Optional.empty());
    }

    @Override
    public String keyPath() {
        return "internal/amphora/secrets/value";
    }
}
