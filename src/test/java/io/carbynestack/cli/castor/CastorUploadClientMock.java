/*
 * Copyright (c) 2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository https://github.com/carbynestack/cli-ng.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package io.carbynestack.cli.castor;

import io.carbynestack.castor.client.upload.CastorUploadClient;
import io.carbynestack.castor.common.entities.TupleChunk;
import io.carbynestack.castor.common.entities.TupleType;
import io.carbynestack.castor.common.exceptions.CastorClientException;
import org.assertj.core.api.Condition;
import org.assertj.core.api.ListAssert;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

record CastorUploadClientMock(boolean successful, List<Invocation> invocations) implements CastorUploadClient {
    public CastorUploadClientMock(boolean successful) {
        this(successful, new ArrayList<>());
    }

    static ListAssert<Invocation> verify(CastorUploadClient client, int count) {
        if (client instanceof CastorUploadClientMock mock) {
            var listAssert = assertThat(new ArrayList<>(mock.invocations.subList(0, count)));
            mock.invocations.subList(0, count).clear();
            return listAssert;
        }
        return assertThat(Collections.emptyList());
    }

    static Condition<Invocation> connect() {
        return new Condition<>(ConnectWebSocket.class::isInstance,
                "connectWebSocket");
    }

    static Condition<Invocation> upload() {
        return new Condition<>(UploadTupleChunk.class::isInstance,
                "uploadTupleChunk");
    }

    static Condition<Invocation> tupleType(TupleType type) {
        return new Condition<>(invocation -> invocation instanceof UploadTupleChunk uploadTupleChunk
                && Objects.equals(uploadTupleChunk.tupleChunk.getTupleType(), type),
                "uploadTupleChunk with tupleType of %s", type);
    }

    static Condition<Invocation> activate() {
        return new Condition<>(ActivateTupleChunk.class::isInstance,
                "activateTupleChunk");
    }

    static Condition<Invocation> chunkId(String id) {
        return new Condition<>(invocation -> (invocation instanceof UploadTupleChunk uploadTupleChunk
                && Objects.equals(uploadTupleChunk.tupleChunk.getChunkId(), UUID.fromString(id))
                || (invocation instanceof ActivateTupleChunk activateTupleChunk
                && Objects.equals(activateTupleChunk.tupleChunkId, UUID.fromString(id)))),
                "uploadTupleChunk or activateTupleChunk with chunkId %s", id);
    }

    static Condition<Invocation> disconnect() {
        return new Condition<>(DisconnectWebSocket.class::isInstance,
                "disconnectWebSocket");
    }

    @Override
    public boolean uploadTupleChunk(TupleChunk tupleChunk) throws CastorClientException {
        invocations.add(new UploadTupleChunk(tupleChunk, -1));
        return successful;
    }

    @Override
    public boolean uploadTupleChunk(TupleChunk tupleChunk, long timeout) throws CastorClientException {
        invocations.add(new UploadTupleChunk(tupleChunk, timeout));
        return successful;
    }

    @Override
    public void activateTupleChunk(UUID tupleChunkId) {
        if (successful) {
            invocations.add(new ActivateTupleChunk(tupleChunkId));
        } else {
            throw new CastorClientException("Tuple chunk activation for "
                    + tupleChunkId + "failed!");
        }
    }

    @Override
    public void connectWebSocket(long timeout) {
        invocations.add(new ConnectWebSocket(timeout));
    }

    @Override
    public void disconnectWebSocket() {
        invocations.add(new DisconnectWebSocket());
    }

    private sealed interface Invocation {
    }

    record UploadTupleChunk(TupleChunk tupleChunk, long timeout) implements Invocation {
    }

    record ActivateTupleChunk(UUID tupleChunkId) implements Invocation {
    }

    record ConnectWebSocket(long timeout) implements Invocation {
    }

    record DisconnectWebSocket() implements Invocation {
    }
}
