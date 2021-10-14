package io.carbynestack.cli;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class VersionTest {
    private final Version version = new Version();

    @Test
    public void getLocale() {
        var locale = Locale.getDefault();
        var target = new Locale("en", "US");
        var result = version.getLocale();
        assertThat(result).contains(locale.toLanguageTag());
        assertThat(result).contains(locale.getDisplayLanguage(target));
        assertThat(result).contains(locale.getDisplayCountry(target));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Carbyne Stack CLI", "Picocli", "Amphora", "Castor", "Ephemeral", "JVM", "OS", "Locale"})
    public void getVersion(String sequence) {
        assertThat(version.getVersion()).anyMatch(line -> line.contains(sequence + ":"));
    }
}
