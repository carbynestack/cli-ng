package io.carbynestack.cli.amphora;

import io.carbynestack.amphora.client.AmphoraClient;
import io.carbynestack.amphora.client.Secret;
import io.carbynestack.amphora.common.Metadata;
import io.carbynestack.amphora.common.MetadataPage;
import io.carbynestack.amphora.common.Tag;
import io.carbynestack.amphora.common.TagFilter;
import io.carbynestack.amphora.common.exceptions.AmphoraClientException;
import io.carbynestack.amphora.common.paging.PageRequest;
import io.carbynestack.amphora.common.paging.Sort;

import java.util.List;
import java.util.UUID;

public interface AmphoraClientMockBase extends AmphoraClient {
    @Override
    default UUID createSecret(Secret secret) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void deleteSecret(UUID uuid) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default Secret getSecret(UUID uuid) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<Metadata> getSecrets() throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<Metadata> getSecrets(List<TagFilter> list) {
        throw new UnsupportedOperationException();
    }

    @Override
    default MetadataPage getSecrets(PageRequest pageRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<Metadata> getSecrets(Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<Metadata> getSecrets(List<TagFilter> list, Sort sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    default MetadataPage getSecrets(List<TagFilter> list, PageRequest pageRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<Tag> getTags(UUID uuid) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void createTag(UUID uuid, Tag tag) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void overwriteTags(UUID uuid, List<Tag> list) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default Tag getTag(UUID uuid, String s) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void updateTag(UUID uuid, Tag tag) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void deleteTag(UUID uuid, String s) throws AmphoraClientException {
        throw new UnsupportedOperationException();
    }
}
