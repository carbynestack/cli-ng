package io.carbynestack.cli.config;

import io.carbynestack.cli.resolve.BigIntegerResolvable;
import io.carbynestack.cli.resolve.BooleanResolvable;
import io.carbynestack.cli.resolve.PathsResolvable;

import java.util.Iterator;
import java.util.List;

public final class Config {
    public static final BigIntegerResolvable PRIME = new BigIntegerResolvable("prime",
            "Modulus N as used by the MPC backend", "Modulus N as used by the MPC backend");
    public static final BigIntegerResolvable R = new BigIntegerResolvable("r",
            "Auxiliary modulus R as used by the MPC backend",
            "Auxiliary modulus R as used by the MPC backend");
    public static final BigIntegerResolvable RINV = new BigIntegerResolvable("rinv",
            "Multiplicative inverse for the auxiliary modulus R as used by the MPC backend",
            "Multiplicative inverse for the auxiliary modulus R as used by the MPC backend");
    public static final BooleanResolvable NO_SSL_VALIDATION = new BooleanResolvable("no/ssl/validation",
            "Disable SSL certificate validation", "Disable SSL certificate validation");
    public static final PathsResolvable TRUSTED_CERTIFICATES = new PathsResolvable("trusted/certificates",
            "Trusted certificates", "Trusted certificates");
    private static final Iterator<String> CLUSTER_NAMES = List.of("apollo", "starbuck").iterator();
}
