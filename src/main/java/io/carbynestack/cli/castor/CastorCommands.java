/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor;

import io.carbynestack.castor.client.download.CastorIntraVcpClient;
import io.carbynestack.castor.client.upload.CastorUploadClient;
import io.carbynestack.castor.common.entities.TupleType;
import io.carbynestack.cli.castor.args.ActivateChunkArgs;
import io.carbynestack.cli.castor.args.TelemetryArgs;
import io.carbynestack.cli.castor.args.UploadTupleArgs;
import io.carbynestack.cli.castor.runners.ActivateChunkRunner;
import io.carbynestack.cli.castor.runners.TelemetryRunner;
import io.carbynestack.cli.castor.runners.UploadTupleRunner;
import io.carbynestack.cli.common.PicocliCommon;
import io.carbynestack.cli.common.runners.DefaultCommandRunner;
import io.carbynestack.common.Stub;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Callable;

import static io.carbynestack.cli.common.CommandExecutor.execute;
import static picocli.CommandLine.Mixin;
import static picocli.CommandLine.Option;

@Command(name = "castor", subcommands = {CastorCommands.Activate.class,
        CastorCommands.Telemetry.class, CastorCommands.UploadTuple.class},
        description = "Upload to and download from Castor service(s).")
public class CastorCommands extends DefaultCommandRunner {
    @Stub
    public static CastorUploadClient UPLOAD_CLIENT;
    @Stub
    public static CastorIntraVcpClient INTRA_VCP_CLIENT;

    @Command(name = "activate", description = "Upload a tuple file to Castor service(s).")
    static final class Activate implements Callable<Integer> {
        @Option(names = "--provider", required = true, paramLabel = "PROVIDER_NAME",
                description = "Provider-NAME as given by the configuration.")
        private String providerName;
        @Option(names = "--chunk", required = true, paramLabel = "CHUNK_ID",
                description = " Unique identifier as to be used by Castor.")
        private UUID chunkId;
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() {
            return execute(ActivateChunkRunner::new,
                    new ActivateChunkArgs(providerName, chunkId), common);
        }
    }

    @Command(name = "telemetry", description = "Download telemetry data.")
    static final class Telemetry implements Callable<Integer> {
        @Option(names = "--interval", paramLabel = "SECONDS",
                description = "Interval in seconds to get the telemetry data for.")
        private long interval = -1;
        @Option(names = "--provider", required = true, paramLabel = "PROVIDER_NAME",
                description = "Provider-NAME as given by the configuration.")
        private String providerName;
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() {
            return execute(TelemetryRunner::new,
                    new TelemetryArgs(providerName, interval), common);
        }
    }

    @Command(name = "upload", description = "Upload a tuple file to Castor service(s).")
    static final class UploadTuple implements Callable<Integer> {
        @Option(names = "--chunk", paramLabel = "CHUNK_ID",
                description = " Unique identifier as to be used by Castor.")
        private UUID chunkId = UUID.randomUUID();
        @Option(names = "--provider", required = true, paramLabel = "PROVIDER_NAME",
                description = "Provider-NAME as given by the configuration.")
        private String providerName;
        @Option(names = "--tuples", required = true, paramLabel = "TUPLE_FILE",
                description = "Tuple file path.")
        private File tupleFile;
        @Option(names = "--type", required = true, paramLabel = "TUPLE_TYPE",
                description = "Tuple type, e.g., MULTIPLICATION_TRIPLE_GFP.")
        private TupleType tupleType;
        @Mixin
        private PicocliCommon common;

        @Override
        public Integer call() {
            return execute(UploadTupleRunner::new,
                    new UploadTupleArgs(providerName, tupleFile, tupleType,
                            chunkId), common);
        }
    }
}
