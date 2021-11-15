/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import io.carbynestack.common.Stub;
import io.carbynestack.common.result.Failure;
import io.carbynestack.common.result.Result;
import io.carbynestack.common.result.Success;

import java.io.*;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static io.carbynestack.cli.util.KeyStoreUtilFailures.*;

/**
 * Handles the generation of certificates and creation of keystores.
 *
 * @since 0.5.0
 */
public final class KeyStoreUtil {
    /**
     * Generates a certificate using a factory and a path.
     *
     * @param factory the certificate factory
     * @param cert    the certificate path
     * @return the generated certificate or a failure as a {@link Result}
     * @since 0.5.0
     */
    @Stub
    static Result<X509Certificate, KeyStoreUtilFailures> generateCertificate(CertificateFactory factory, Path cert) {
        return Result.of(() -> (X509Certificate) factory.generateCertificate(new FileInputStream(cert.toFile())),
                Map.of(FileNotFoundException.class, FILE_NOT_FOUND, CertificateException.class, CERTIFICATE_PARSING,
                        Throwable.class, OTHER), OTHER);
    }

    /**
     * Creates a temporary keystore for the given certificates, store and factory.
     *
     * @param certs              the certificates to store in the keystore
     * @param store              the certificate keystore
     * @param certificateFactory the certificate factory
     * @return the keystore file or a failure reason as a {@link Result}
     * @since 0.5.0
     */
    @Stub
    static Result<File, KeyStoreUtilFailures> tempKeyStorePems(Collection<Path> certs, KeyStore store, CertificateFactory certificateFactory) {
        return Result.of(() -> {
            if (certs == null || certs.isEmpty()) return NO_CERTS.toFailure();

            store.load(null);

            for (var entry : certs.stream().filter(Objects::nonNull)
                    .map(cert -> generateCertificate(certificateFactory, cert))
                    .collect(Collectors.toSet())) {
                if (entry.tryPeek(cert -> store.setCertificateEntry(cert.getSubjectX500Principal().getName(), cert),
                        JKS_INSTANCE) instanceof Failure<X509Certificate, KeyStoreUtilFailures> failure)
                    return new Failure<>(failure.reason());
            }

            var temp = File.createTempFile("cs_keystore", ".jks");

            try (var output = new FileOutputStream(temp)) {
                store.store(output, "".toCharArray());
            }

            temp.deleteOnExit();

            return new Success<>(temp);
        }, Map.of(UnrecoverableEntryException.class, WRONG_PASSWORD, IOException.class, KEYSTORE_DATA_FORMAT,
                NoSuchAlgorithmException.class, NO_SUCH_ALGORITHM, Throwable.class, OTHER), OTHER).unsafeFlatten();
    }

    /**
     * Creates a temporary keystore for the given certificates.
     *
     * @param certs the certificates to store in the keystore
     * @return the keystore file or a failure reason as a {@link Result}
     * @since 0.5.0
     */
    public static Result<File, KeyStoreUtilFailures> tempKeyStorePems(Collection<Path> certs) {
        return Result.of(() -> tempKeyStorePems(certs, KeyStore.getInstance("JKS"),
                CertificateFactory.getInstance("X509")), Map.of(KeyStoreException.class, JKS_INSTANCE,
                CertificateException.class, X509_CERTIFICATE_FACTORY), OTHER).unsafeFlatten();
    }
}
