package io.carbynestack.cli.configure;

import io.carbynestack.cli.common.CommandRunner;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.resolve.BigIntegerResolvable;
import io.carbynestack.cli.resolve.BooleanResolvable;
import io.carbynestack.cli.resolve.PathsResolvable;
import io.carbynestack.cli.util.ExitCodes;
import io.carbynestack.cli.util.args.NoArg;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;
import picocli.CommandLine.Command;

import java.io.File;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static io.carbynestack.cli.resolve.Resolver.config;
import static picocli.CommandLine.*;

@Command(name = "configure")
public class Configure implements CommandRunner<NoArg>, Callable<Integer> {
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
    @Unmatched
    List<String> unmatched;
    @Option(names = "--prime")
    private Optional<BigInteger> prime;
    @Option(names = "--r")
    private Optional<BigInteger> r;
    @Option(names = "--rinv")
    private Optional<BigInteger> rInv;
    @Option(names = {"-o", "--output"})
    private File outputFile = new File(System.getProperty("user.home") + File.separator + ".cs/config");

    private String getClusterName() {
        var clusterName = CLUSTER_NAMES.hasNext() ? CLUSTER_NAMES.next() : null;
        System.out.print("Cluster name" + (clusterName == null ? "" : " (" + clusterName + ")") + ": ");
        //TODO make quitting possible
        return Optional.of(System.console().readLine())
                .filter(Predicate.not(String::isBlank))
                .orElse(clusterName);
    }

    @Override
    public Result<Integer, ? extends CsFailureReason> run(NoArg noArgs, Common common) {
        var resolved = common.config().resolve(NO_SSL_VALIDATION)
                .with(Optional.empty());

        var res = Map.of(PRIME, prime, R, r, RINV, rInv)
                .entrySet().stream()
                .map(entry -> Map.entry(entry.getKey().keyPath(), Optional.ofNullable(config()
                        .resolve(entry.getKey()).with(entry.getValue())).or(() -> {
                    System.out.print(entry.getKey().keyPath() + ": ");
                    return entry.getKey().parse(System.console().readLine());
                }).orElse(null)))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println(res);

        for (var i = 0; i < 3; i++) {
            String clusterName = null;

            while (clusterName == null) {
                clusterName = getClusterName();
            }

            System.out.println(clusterName);
        }

        System.out.println("Unmatched: " + unmatched);

        return ExitCodes.success();
    }

    @Override
    public Integer call() {
        return execute(() -> this, new NoArg(), null);
    }
}
