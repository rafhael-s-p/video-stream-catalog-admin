package com.studies.catalog.admin.infrastructure.services.local;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class InMemoryStorageAPITest {

    private InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setUp() {
        target.clear();
    }

    @Test
    void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "item";

        target.store(expectedId, expectedResource);

        final var currentContent = this.target.storage().get(expectedId);

        Assertions.assertEquals(expectedResource, currentContent);
    }

    @Test
    void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "item";

        this.target.storage().put(expectedId, expectedResource);

        final var currentContent = target.get(expectedId).get();

        Assertions.assertEquals(expectedResource, currentContent);
    }

    @Test
    void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        final var currentContent = target.list("it");

        Assertions.assertEquals(expectedIds.size(), currentContent.size());
        Assertions.assertTrue(expectedIds.containsAll(currentContent));
    }

    @Test
    void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        target.deleteAll(expectedIds);

        Assertions.assertTrue(this.target.storage().isEmpty());
    }

}