package com.studies.catalog.admin.infrastructure.video;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.video.ImageMedia;
import com.studies.catalog.admin.domain.video.Video;
import com.studies.catalog.admin.domain.video.VideoID;
import com.studies.catalog.admin.domain.video.VideoMedia;
import com.studies.catalog.admin.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;

@IntegrationTest
class DefaultVideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void testInjection() {
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
        final var marlonBrando = castMemberGateway.create(Fixture.CastMembers.marlonBrando());
        final var movies = categoryGateway.create(Fixture.Categories.movies());
        final var crime = genreGateway.create(Fixture.Genres.crime());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(movies.getId());
        final var expectedGenres = Set.of(crime.getId());
        final var expectedMembers = Set.of(marlonBrando.getId());

        final VideoMedia expectedVideo =
                VideoMedia.with("123", "video", "/media/video");

        final VideoMedia expectedTrailer =
                VideoMedia.with("123", "trailer", "/media/trailer");

        final ImageMedia expectedBanner =
                ImageMedia.with("123", "banner", "/media/banner");

        final ImageMedia expectedThumb =
                ImageMedia.with("123", "thumb", "/media/thumb");

        final ImageMedia expectedThumbHalf =
                ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        // when
        final var currentVideo = videoGateway.create(aVideo);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());

        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), currentVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), currentVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), currentVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), currentVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), currentVideo.getThumbnailHalf().get().name());

        final var persistedVideo = videoRepository.findById(currentVideo.getId().getValue()).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

    @Test
    @Transactional
    void givenAValidVideoWithoutRelations_whenCallsCreate_shouldPersistIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // when
        final var currentVideo = videoGateway.create(aVideo);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());

        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertTrue(currentVideo.getVideo().isEmpty());
        Assertions.assertTrue(currentVideo.getTrailer().isEmpty());
        Assertions.assertTrue(currentVideo.getBanner().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(currentVideo.getThumbnailHalf().isEmpty());

        final var persistedVideo = videoRepository.findById(currentVideo.getId().getValue()).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    void givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
        // given
        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        final var marlonBrando = castMemberGateway.create(Fixture.CastMembers.marlonBrando());
        final var movies = categoryGateway.create(Fixture.Categories.movies());
        final var crime = genreGateway.create(Fixture.Genres.crime());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(movies.getId());
        final var expectedGenres = Set.of(crime.getId());
        final var expectedMembers = Set.of(marlonBrando.getId());

        final VideoMedia expectedVideo =
                VideoMedia.with("123", "video", "/media/video");

        final VideoMedia expectedTrailer =
                VideoMedia.with("123", "trailer", "/media/trailer");

        final ImageMedia expectedBanner =
                ImageMedia.with("123", "banner", "/media/banner");

        final ImageMedia expectedThumb =
                ImageMedia.with("123", "thumb", "/media/thumb");

        final ImageMedia expectedThumbHalf =
                ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

        final var updatedVideo = Video.with(aVideo)
                .update(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        // when
        final var currentVideo = videoGateway.update(updatedVideo);

        // then
        Assertions.assertNotNull(currentVideo);
        Assertions.assertNotNull(currentVideo.getId());

        Assertions.assertEquals(expectedTitle, currentVideo.getTitle());
        Assertions.assertEquals(expectedDescription, currentVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, currentVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, currentVideo.getDuration());
        Assertions.assertEquals(expectedOpened, currentVideo.getOpened());
        Assertions.assertEquals(expectedPublished, currentVideo.getPublished());
        Assertions.assertEquals(expectedRating, currentVideo.getRating());
        Assertions.assertEquals(expectedCategories, currentVideo.getCategories());
        Assertions.assertEquals(expectedGenres, currentVideo.getGenres());
        Assertions.assertEquals(expectedMembers, currentVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), currentVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), currentVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), currentVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), currentVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), currentVideo.getThumbnailHalf().get().name());
        Assertions.assertNotNull(currentVideo.getCreatedAt());
        Assertions.assertTrue(currentVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));

        final var persistedVideo = videoRepository.findById(currentVideo.getId().getValue()).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
        Assertions.assertNotNull(persistedVideo.getCreatedAt());
        Assertions.assertTrue(persistedVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));
    }

    @Test
    void givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        Assertions.assertEquals(1, videoRepository.count());

        final var anId = aVideo.getId();

        // when
        videoGateway.deleteById(anId);

        // then
        Assertions.assertEquals(0, videoRepository.count());
    }

    @Test
    void givenAnInvalidVideoId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        videoGateway.create(Video.newVideo(
                Fixture.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        Assertions.assertEquals(1, videoRepository.count());

        final var anId = VideoID.unique();

        // when
        videoGateway.deleteById(anId);

        // then
        Assertions.assertEquals(1, videoRepository.count());
    }

}