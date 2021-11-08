/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.util;

import io.carbynestack.common.CsFailureReason;

public enum KeyStoreUtilFailures implements CsFailureReason {
    NO_CERTS("No certificates provided."),
    JKS_INSTANCE("JKS instance retrieval from KeyStore failed."),
    X509_CERTIFICATE_FACTORY("X509 factory instance retrieval from CertificateFactory failed."),
    KEYSTORE_DATA_FORMAT(""),
    WRONG_PASSWORD(""),
    NO_SUCH_ALGORITHM("The algorithm used to check the integrity of the KeyStore cannot be found."),
    FILE_NOT_FOUND("The certificate file was not found."),
    CERTIFICATE_PARSING("The parsing of a certificate failed."),
    //TODO move to CommandFailure
    OTHER("An unknown runtime exception led to this failure. Please review the stack trace and submit an issue.");

    private final String description;

    KeyStoreUtilFailures(String description) {
        this.description = description;
    }

    @Override
    public String synopsis() {
        return "Failed to generate temporary keystore.";
    }

    @Override
    public String description() {
        return this.description;
    }
}
