package com.studies.catalog.admin.infrastructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.video.Resource;
import com.studies.catalog.admin.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GCStorageAPITest {

    private GCStorageService target;

    private Storage storage;

    private String bucket = "test";

    @BeforeEach
    public void setUp() {
        this.storage = Mockito.mock(Storage.class);
        this.target = new GCStorageService(bucket, storage);
    }

    @Test
    void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).get(eq(bucket), eq(expectedId));

        this.target.store(expectedId, expectedResource);

        final var capture = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(capture.capture(), eq(expectedResource.content()));

        final var currentBlob = capture.getValue();
        Assertions.assertEquals(this.bucket, currentBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedId, currentBlob.getBlobId().getName());
        Assertions.assertEquals(expectedResource.contentType(), currentBlob.getContentType());
        Assertions.assertEquals(expectedResource.checksum(), currentBlob.getCrc32cToHexString());
    }

    @Test
    void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).get(eq(bucket), eq(expectedId));

        final var currentContent = target.get(expectedId).get();

        Assertions.assertEquals(expectedResource.checksum(), currentContent.checksum());
        Assertions.assertEquals(expectedResource.name(), currentContent.name());
        Assertions.assertEquals(expectedResource.content(), currentContent.content());
        Assertions.assertEquals(expectedResource.contentType(), currentContent.contentType());
    }

    @Test
    void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var video = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var banner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedIds = List.of(video.name(), banner.name());

        final var page = Mockito.mock(Page.class);

        final Blob blob1 = mockBlob(video);
        final Blob blob2 = mockBlob(banner);

        doReturn(List.of(blob1, blob2)).when(page).iterateAll();
        doReturn(page).when(storage).list(eq(bucket), eq(Storage.BlobListOption.prefix("it")));

        final var currentContent = target.list("it");

        Assertions.assertEquals(expectedIds.size(), currentContent.size());
        Assertions.assertTrue(expectedIds.containsAll(currentContent));
    }

    @Test
    void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedIds = List.of("item1", "item2");

        target.deleteAll(expectedIds);

        final var capture = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(capture.capture());

        final var currentIds = ((List<BlobId>) capture.getValue()).stream()
                .map(BlobId::getName)
                .toList();

        Assertions.assertTrue(expectedIds.size() == currentIds.size() && currentIds.containsAll(expectedIds));
    }

    private Blob mockBlob(final Resource resource) {
        final var blob1 = Mockito.mock(Blob.class);
        when(blob1.getBlobId()).thenReturn(BlobId.of(bucket, resource.name()));
        when(blob1.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(blob1.getContent()).thenReturn(resource.content());
        when(blob1.getContentType()).thenReturn(resource.contentType());
        when(blob1.getName()).thenReturn(resource.name());
        return blob1;
    }

}