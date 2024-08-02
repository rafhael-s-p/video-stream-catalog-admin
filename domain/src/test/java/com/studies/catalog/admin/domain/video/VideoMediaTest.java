package com.studies.catalog.admin.domain.video;

import com.studies.catalog.admin.domain.UnitTest;
import com.studies.catalog.admin.domain.utils.IdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VideoMediaTest extends UnitTest {

    @Test
    void givenValidParams_whenCallsNewAudioVideo_ShouldReturnInstance() {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedRawLocation = "/images/ac";
        final var expectedEncodedLocation = "/images/ac-encoded";
        final var expectedStatus = MediaStatus.PENDING;

        // when
        final var currentVideo =
                VideoMedia.with(expectedId, expectedChecksum, expectedName, expectedRawLocation, expectedEncodedLocation, expectedStatus);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertEquals(expectedId, currentVideo.id());
        Assertions.assertEquals(expectedChecksum, currentVideo.checksum());
        Assertions.assertEquals(expectedName, currentVideo.name());
        Assertions.assertEquals(expectedRawLocation, currentVideo.rawLocation());
        Assertions.assertEquals(expectedEncodedLocation, currentVideo.encodedLocation());
        Assertions.assertEquals(expectedStatus, currentVideo.status());
    }

    @Test
    void givenTwoVideosWithSameChecksumAndLocation_whenCallsEquals_ShouldReturnTrue() {
        // given
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/images/ac";

        final var img1 =
                VideoMedia.with(expectedChecksum, "Random", expectedRawLocation);

        final var img2 =
                VideoMedia.with(expectedChecksum, "Simple", expectedRawLocation);

        // then
        Assertions.assertEquals(img1, img2);
        Assertions.assertNotSame(img1, img2);
    }

    @Test
    void givenInvalidParams_whenCallsWith_ShouldReturnError() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> VideoMedia.with(null, "131", "Random", "/videos", "/videos", MediaStatus.PENDING)
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> VideoMedia.with("id", "abc", null, "/videos", "/videos", MediaStatus.PENDING)
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> VideoMedia.with("id", "abc", "Random", null, "/videos", MediaStatus.PENDING)
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> VideoMedia.with("id", "abc", "Random", "/videos", null, MediaStatus.PENDING)
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> VideoMedia.with("id", "abc", "Random", "/videos", "/videos", null)
        );
    }

}