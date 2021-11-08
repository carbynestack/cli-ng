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

import static io.carbynestack.cli.util.KeyStoreUtilFailures.*;

public final class KeyStoreUtil {
    static Result<X509Certificate, KeyStoreUtilFailures> generateCertificate(CertificateFactory factory, Path cert) {
        try {
            return new Success<>((X509Certificate) factory.generateCertificate(new FileInputStream(cert.toFile())));
        } catch (Throwable throwable) {
            return Map.of(FileNotFoundException.class, FILE_NOT_FOUND, CertificateException.class, CERTIFICATE_PARSING)
                    .getOrDefault(throwable.getClass(), OTHER).toFailure();
        }
    }

    @Stub
    static Result<File, KeyStoreUtilFailures> tempKeyStorePems(Collection<Path> certs, KeyStore store, CertificateFactory certificateFactory) {
        //TODO make recovery more versatile -> or maybe offer possibility to transform (or map with two params/biMap?)
        if(certs == null || certs.isEmpty()) return NO_CERTS.toFailure();

        try {
            //Throws java.io.IOException, java.security.NoSuchAlgorithmException, java.security.cert.CertificateException
            store.load(null);
            var collection = certs.stream()
                    .filter(Objects::nonNull)
                    .map(cert -> generateCertificate(certificateFactory, cert))
                    .toList();

            for(var entry : collection) {
                if(entry instanceof Success<X509Certificate, ?> success) {
                    //TODO maybe Result.perform/do/...
                    //Throws java.security.KeyStoreException
                    store.setCertificateEntry(success.value().getSubjectX500Principal().getName(), success.value());
                } else if (entry instanceof Failure<?, KeyStoreUtilFailures> failure) {
                    //TODO make failures convertible
                    return new Failure<>(failure.reason());
                }
            }

            //Throws java.io.IOException
            var temp = File.createTempFile("cs_keystore", ".jks");

            try(var output = new FileOutputStream(temp)) {
                store.store(output, "".toCharArray());
                //TODO think about possible failure...
            }

            temp.deleteOnExit();

            return new Success<>(temp);
        } catch (IOException ioException) {
            //TODO append the stacktrace
            if(ioException.getCause() instanceof UnrecoverableEntryException) {
                return WRONG_PASSWORD.toFailure();
            } else {
                //TODO could also be file creation exception
                return KEYSTORE_DATA_FORMAT.toFailure();
            }
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            //TODO append the stacktrace
            return NO_SUCH_ALGORITHM.toFailure();
        } catch (Throwable throwable) {
            return OTHER.toFailure();
        }
    }

    public static Result<File, KeyStoreUtilFailures> tempKeyStorePems(Collection<Path> certs) {
        try {
            return tempKeyStorePems(certs, KeyStore.getInstance("JKS"), CertificateFactory.getInstance("X509"));
        } catch (KeyStoreException e) {
            //TODO append the stacktrace
            return JKS_INSTANCE.toFailure();
        } catch (CertificateException certificateException) {
            //TODO append the stacktrace
            return X509_CERTIFICATE_FACTORY.toFailure();
        }
    }
}
