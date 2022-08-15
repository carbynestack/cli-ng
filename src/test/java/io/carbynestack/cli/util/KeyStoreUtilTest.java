/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Set;

import static io.carbynestack.cli.util.KeyStoreUtil.generateCertificate;
import static io.carbynestack.cli.util.KeyStoreUtil.tempKeyStorePems;
import static io.carbynestack.cli.util.KeyStoreUtilFailures.*;
import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KeyStoreUtilTest {
    @Test
    void whenCreatingKeyStoreUtilThenThrowUnsupportedOperationException() {
        assertThatThrownBy(KeyStoreUtil::new)
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Instance creation of utility class KeyStoreUtil not permitted!");
    }

    @Test
    void givenX509CertificateFactoryAndInvalidCertPathWhenCallingGenerateCertificateOnKeyStoreUtilThenReturnFileNotFoundFailureResult() throws CertificateException {
        var temp = Paths.get("missing-cert", ".X509");
        var result = generateCertificate(CertificateFactory.getInstance("X509"),
                temp);
        assertThat(result).hasReason(FILE_NOT_FOUND);
    }

    @Test
    void givenX509CertificateFactoryAndEmptyCertWhenCallingGenerateCertificateOnKeyStoreUtilThenReturnCertificateParsingFailureResult() throws CertificateException, IOException {
        var temp = Files.createTempFile("empty-cert", ".X509");
        var result = generateCertificate(CertificateFactory.getInstance("X509"),
                temp);
        assertThat(result).hasReason(CERTIFICATE_PARSING);
        Files.delete(temp);
    }

    @Test
    void givenFactoryAndCertAreNullWhenCallingGenerateCertificateOnKeyStoreUtilThenReturnOtherFailureResult() {
        assertThat(generateCertificate(null, null)).hasReason(OTHER);
    }

    @Test
    void givenValidCertPathsWhenCallingTempKeyStorePemsOnKeyStoreUtilThenSuccessfullyCreateTemporaryStore() {
        assertThat(tempKeyStorePems(Set.of(Path.of("",
                "src/test/resources").resolve("cert.pem")))).isSuccess();
    }

    @Test
    void givenCertPathsAreNullWhenCallingTempKeyStorePemsOnKeyStoreUtilThenReturnNoCertsFailureResult() {
        assertThat(tempKeyStorePems(null)).hasReason(NO_CERTS);
    }

    @Test
    void givenCertPathsAreEmptyWhenCallingTempKeyStorePemsOnKeyStoreUtilThenReturnNoCertsFailureResult() {
        assertThat(tempKeyStorePems(emptyList())).hasReason(NO_CERTS);
    }

    @Test
    void givenInvalidCertPathsWhenCallingTempKeyStorePemsOnKeyStoreUtilThenReturnFileNotFoundFailureResult() {
        assertThat(tempKeyStorePems(Set.of(Path.of("missing.file")))).hasReason(FILE_NOT_FOUND);
    }

    @Test
    void givenEmptyCertsWhenCallingTempKeyStorePemsOnKeyStoreUtilThenReturnCertificateParsingFailureResult() {
        assertThat(tempKeyStorePems(Set.of(Path.of("", "src/test/resources")
                .resolve("config.json")))).hasReason(CERTIFICATE_PARSING);
    }
}
