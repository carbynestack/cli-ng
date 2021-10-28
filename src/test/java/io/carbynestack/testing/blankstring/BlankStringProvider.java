/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.testing.blankstring;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.Map;
import java.util.stream.Stream;

/**
 * {@code BlankStringProvider} is an {@code ArgumentsProvider} responsible for
 * {@linkplain #provideArguments providing} a stream of named {@link Arguments}
 * containing UTF-8 characters which valid in a blank string.
 *
 * @see ParameterizedTest
 * @see ArgumentsSource
 * @see Arguments
 * @see AnnotationConsumer
 * @since 0.1.0
 */
public class BlankStringProvider implements ArgumentsProvider {
    /**
     * Provides a {@link Stream} of named {@code Arguments} containing
     * each an UTF-8 character string.
     *
     * @param context the current extension context
     * @return a stream of named {@link Arguments} containing an UTF-8
     * char
     * @since 0.1.0
     */
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                        Map.of(
                                "SPACE", "\u0020",
                                "EN QUAD", "\u2000",
                                "EM QUAD", "\u2001",
                                "EN SPACE", "\u2002",
                                "EM SPACE", "\u2003",
                                "THREE-PER-EM SPACE", "\u2004",
                                "FOUR-PER-EM SPACE", "\u2005",
                                "SIX-PER-EM SPACE", "\u2006",
                                "PUNCTUATION SPACE", "\u2008",
                                "THIN SPACE", "\u2009"
                        ),
                        Map.of(
                                "HAIR SPACE", "\u200A",
                                "MEDIUM MATHEMATICAL SPACE", "\u205F",
                                "IDEOGRAPHIC SPACE", "\u3000",
                                "OGHAM SPACE MARK", "\u1680",
                                "HORIZONTAL TABULATION", "\t",
                                "LINE FEED", "\n",
                                "VERTICAL TABULATION", "\u000B",
                                "FORM FEED", "\f",
                                "CARRIAGE RETURN", "\r",
                                "FILE SEPARATOR", "\u001C"
                        ),
                        Map.of(
                                "GROUP SEPARATOR", "\u001D",
                                "RECORD SEPARATOR", "\u001E",
                                "UNIT SEPARATOR", "\u001F",
                                "LINE SEPARATOR", "\u2028",
                                "PARAGRAPH SEPARATOR", "\u2029"
                        )
                ).flatMap(map -> map.entrySet().stream())
                .map(entry -> Named.of(entry.getKey(), entry.getValue()))
                .map(Arguments::of);
    }
}
