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
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import static io.carbynestack.cli.util.KeyStoreUtil.generateCertificate;
import static io.carbynestack.cli.util.KeyStoreUtil.tempKeyStorePems;
import static io.carbynestack.cli.util.KeyStoreUtilFailures.*;
import static io.carbynestack.testing.result.ResultAssert.assertThat;
import static java.util.Collections.emptyList;

public class KeyStoreUtilTest {
    @Test
    public void generateCertificateFileNotFound() throws CertificateException {
        var temp = Paths.get("missing-cert", ".X509");
        var result = generateCertificate(CertificateFactory.getInstance("X509"),
                temp);
        assertThat(result).hasReason(FILE_NOT_FOUND);
    }

    @Test
    public void generateCertificateParsingFailure() throws CertificateException, IOException {
        var temp = Files.createTempFile("empty-cert", ".X509");
        var result = generateCertificate(CertificateFactory.getInstance("X509"),
                temp);
        assertThat(result).hasReason(CERTIFICATE_PARSING);
        Files.delete(temp);
    }

    @Test
    public void generateCertificateNullPointer() {
        assertThat(generateCertificate(null, null)).hasReason(OTHER);
    }

    @Test
    public void tempKeyStorePemsNull() {
        assertThat(tempKeyStorePems(null)).hasReason(NO_CERTS);
    }

    @Test
    public void tempKeyStorePemsEmpty() {
        assertThat(tempKeyStorePems(emptyList())).hasReason(NO_CERTS);
    }
}
