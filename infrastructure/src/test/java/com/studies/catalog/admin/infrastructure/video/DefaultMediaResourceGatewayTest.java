package com.studies.catalog.admin.infrastructure.video;


import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.video.*;
import com.studies.catalog.admin.infrastructure.services.StorageService;
import com.studies.catalog.admin.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.studies.catalog.admin.domain.Fixture.Videos.mediaType;
import static com.studies.catalog.admin.domain.Fixture.Videos.resource;


@IntegrationTest
class DefaultMediaResourceGatewayTest {

    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @Autowired
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        storageService().clear();
    }

    @Test
    void testInjection() {
        Assertions.assertNotNull(mediaResourceGateway);
        Assertions.assertInstanceOf(DefaultMediaResourceGateway.class, mediaResourceGateway);

        Assertions.assertNotNull(storageService);
        Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    void givenValidResource_whenCallsStorageVideo_shouldStoreIt() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        // when
        final var currentMedia =
                this.mediaResourceGateway.storeVideo(expectedVideoId, VideoResource.with(expectedType, expectedResource));

        // then
        Assertions.assertNotNull(currentMedia.id());
        Assertions.assertEquals(expectedLocation, currentMedia.rawLocation());
        Assertions.assertEquals(expectedResource.name(), currentMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), currentMedia.checksum());
        Assertions.assertEquals(expectedStatus, currentMedia.status());
        Assertions.assertEquals(expectedEncodedLocation, currentMedia.encodedLocation());

        final var currentStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, currentStored);
    }

    @Test
    void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        // when
        final var currentMedia =
                this.mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedType, expectedResource));

        // then
        Assertions.assertNotNull(currentMedia.id());
        Assertions.assertEquals(expectedLocation, currentMedia.location());
        Assertions.assertEquals(expectedResource.name(), currentMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), currentMedia.checksum());

        final var currentStored = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, currentStored);
    }

    @Test
    void givenValidVideoId_whenCallsGetResource_shouldReturnIt() {
        // given
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);

        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), expectedType), expectedResource);
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), resource(mediaType()));

        Assertions.assertEquals(3, storageService().storage().size());

        // when
        final var currentResult = this.mediaResourceGateway.getResource(videoOne, expectedType).get();

        // then
        Assertions.assertEquals(expectedResource, currentResult);
    }

    @Test
    void givenInvalidType_whenCallsGetResource_shouldReturnEmpty() {
        // given
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.THUMBNAIL;

        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), resource(mediaType()));
        storageService().store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), resource(mediaType()));

        Assertions.assertEquals(3, storageService().storage().size());

        // when
        final var currentResult = this.mediaResourceGateway.getResource(videoOne, expectedType);

        // then
        Assertions.assertTrue(currentResult.isEmpty());
    }

    @Test
    void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        // given
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(id -> storageService().store(id, resource(mediaType())));
        expectedValues.forEach(id -> storageService().store(id, resource(mediaType())));

        Assertions.assertEquals(5, storageService().storage().size());

        // when
        this.mediaResourceGateway.clearResources(videoOne);

        // then
        Assertions.assertEquals(2, storageService().storage().size());

        final var currentKeys = storageService().storage().keySet();

        Assertions.assertEquals(expectedValues.size(), currentKeys.size());
        Assertions.assertTrue(currentKeys.containsAll(expectedValues));
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }

}