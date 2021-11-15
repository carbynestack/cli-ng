/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import io.carbynestack.common.CsFailureReason;

/**
 * Represents the possible CsFailureReason based {@link KeyStoreUtil}
 * failure cases.
 *
 * @since 0.5.0
 */
public enum KeyStoreUtilFailures implements CsFailureReason {
    NO_CERTS("No certificates provided."),
    JKS_INSTANCE("JKS instance retrieval from KeyStore failed."),
    X509_CERTIFICATE_FACTORY("X509 factory instance retrieval from CertificateFactory failed."),
    KEYSTORE_DATA_FORMAT("There was an I/O problem with data."),
    WRONG_PASSWORD("An incorrect ProtectionParameter (e.g. wrong password) was specified."),
    NO_SUCH_ALGORITHM("The algorithm used to check the integrity of the KeyStore cannot be found."),
    FILE_NOT_FOUND("The certificate file was not found."),
    CERTIFICATE_PARSING("The parsing of a certificate failed."),
    OTHER("An unknown runtime exception led to this failure. Please review the stack trace and submit an issue.");

    /**
     * The failure reason description.
     *
     * @since 0.5.0
     */
    private final String description;

    /**
     * Creates a {@code KeyStoreUtilFailures} instance.
     *
     * @param description the failure reason description
     * @since 0.5.0
     */
    KeyStoreUtilFailures(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code "Failed to generate temporary keystore."}
     * @since 0.5.0
     */
    @Override
    public String synopsis() {
        return "Failed to generate temporary keystore.";
    }

    /**
     * {@inheritDoc}
     *
     * @return the case failure reason description
     * @since 0.5.0
     */
    @Override
    public String description() {
        return this.description;
    }
}
