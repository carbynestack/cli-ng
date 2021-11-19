package io.carbynestack.cli.resolve;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnresolvableTest {
    private static final BooleanResolvable resolvable =
            new BooleanResolvable("no/ssl/validation", "synopsis", "description");

    @Test
    void constructorNullableValues() {
        assertThatThrownBy(() -> new Unresolvable(null))
                .isExactlyInstanceOf(NullPointerException.class);
    }

    @Test
    void synopsis() {
        assertThat(new Unresolvable(resolvable).synopsis())
                .isEqualTo("Resolving no/ssl/validation failed! (re-run with -v for more details)");
    }

    @Test
    void description() {
        assertThat(new Unresolvable(resolvable).description())
                .isEqualToIgnoringWhitespace("""
                        The failure can be resolved by setting:
                            export CS_NO_SSL_VALIDATION=<VALUE>
                        or specifying:
                            {
                                ⋯
                                "noSslValidation": "VALUE",
                                ⋯
                            }
                        in the config file (~/.cs/config)""");
    }

    @Test
    void simpleJsonSnippet() {
        assertThat(new Unresolvable(resolvable).jsonSnippet())
                .isEqualTo("""
                        {
                            ⋯
                            "noSslValidation": "VALUE",
                            ⋯
                        }""");
    }

    @Test
    void nestedJsonSnippet() {
        assertThat(new Unresolvable(new UriResolvable("vcp/apollo/amphora/service/url",
                "s", "d")).jsonSnippet()).isEqualTo("""
                {
                    ⋯
                    "vcp": {
                        ⋯
                        "apollo": {
                            "amphoraServiceUrl": "VALUE",
                        }
                        ⋯
                    },
                    ⋯
                }""");
    }
}
