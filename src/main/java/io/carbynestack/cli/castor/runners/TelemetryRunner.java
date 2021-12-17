/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor.runners;

import io.carbynestack.castor.client.download.CastorIntraVcpClient;
import io.carbynestack.castor.common.entities.TelemetryData;
import io.carbynestack.castor.common.entities.TupleMetric;
import io.carbynestack.cli.castor.CastorCommandRunnerBase;
import io.carbynestack.cli.castor.args.TelemetryArgs;
import io.carbynestack.cli.common.Common;
import io.carbynestack.cli.shapeless.Fragment;
import io.carbynestack.cli.shapeless.Fragment.Pair;
import io.carbynestack.cli.shapeless.Fragment.Section;
import io.carbynestack.common.CsFailureReason;
import io.carbynestack.common.result.Result;

import java.util.HashMap;
import java.util.Map;

import static io.carbynestack.cli.util.ExitCodes.success;

public class TelemetryRunner implements CastorCommandRunnerBase<TelemetryArgs> {
    @Override
    public Result<Integer, ? extends CsFailureReason> run(TelemetryArgs args, Common common) {
        common.fragmentTransform(fragment -> common.ansi(switch (common.format()) {
            case DEFAULT, PLAIN -> textualize(fragment);
            default -> fragment;
        }));

        return intraVcpClient(common, args.serviceUri(common.config()))
                .map(args.interval() > 0
                        ? client -> client.getTelemetryData(args.interval())
                        : CastorIntraVcpClient::getTelemetryData)
                .peek(data -> printTelemetry(data, common))
                .flatMap(r -> success());
    }

    private Fragment textualize(Fragment fragment) {
        if (fragment instanceof Section section) {
            var entriesCopy = new HashMap<>(section.entries());
            if (section.entries().containsKey("consumption-rate")) {
                entriesCopy.remove("consumption-rate");
                entriesCopy.put("Consumption/s", section.entries().get("consumption-rate"));
            }
            return new Section("@|bold %s|@".formatted(section.key().toUpperCase()), entriesCopy);
        }
        return fragment;
    }

    private void printTelemetry(TelemetryData data, Common common) {
        data.getMetrics().stream()
                .map(this::metricSection)
                .forEach(section -> common.out().write(section));
        common.out().write(intervalPair(data));
    }

    private Section metricSection(TupleMetric metric) {
        var type = metric.getType().toString();
        return new Section(type.replace('_', '-'), Map.of(
                "available", String.valueOf(metric.getAvailable()),
                "consumption-rate", String.valueOf(metric.getConsumptionRate())
        ));
    }

    private Pair intervalPair(TelemetryData data) {
        return new Pair("interval", String.valueOf(data.getInterval()));
    }
}
