package com.studies.catalog.admin.infrastructure.video;

import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.video.*;
import com.studies.catalog.admin.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    private CastMember marlonBrando;
    private CastMember alPacino;

    private Category movies;
    private Category trailers;

    private Genre crime;
    private Genre drama;

    @BeforeEach
    public void setUp() {
        marlonBrando = castMemberGateway.create(Fixture.CastMembers.marlonBrando());
        alPacino = castMemberGateway.create(Fixture.CastMembers.alPacino());

        movies = categoryGateway.create(Fixture.Categories.movies());
        trailers = categoryGateway.create(Fixture.Categories.trailers());

        crime = genreGateway.create(Fixture.Genres.crime());
        drama = genreGateway.create(Fixture.Genres.drama());
    }

    @Test
    void testInjection() {
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(videoRepository);
    }

    @Test
    void givenEmptyParams_whenCallFindAll_shouldReturnAllList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedTotal, currentPage.items().size());
    }

    @Test
    void givenEmptyVideos_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedTotal, currentPage.items().size());
    }

    @Test
    void givenAValidCategory_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(movies.getId()),
                Set.of()
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedTotal, currentPage.items().size());

        Assertions.assertEquals("The Movie", currentPage.items().get(0).title());
        Assertions.assertEquals("The Panic in Needle Park", currentPage.items().get(1).title());
    }

    @Test
    void givenAValidCastMember_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(marlonBrando.getId()),
                Set.of(),
                Set.of()
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedTotal, currentPage.items().size());

        Assertions.assertEquals("The Godfather", currentPage.items().get(0).title());
        Assertions.assertEquals("The Movie", currentPage.items().get(1).title());
    }

    @Test
    void givenAValidGenre_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of(drama.getId())
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedTotal, currentPage.items().size());

        Assertions.assertEquals("The Godfather", currentPage.items().get(0).title());
    }

    @Test
    void givenAllParameters_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "panic";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(alPacino.getId()),
                Set.of(movies.getId()),
                Set.of(drama.getId())
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedTotal, currentPage.items().size());

        Assertions.assertEquals("The Panic in Needle Park", currentPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,4,Driving Miss Daisy;The Godfather",
            "1,2,2,4,The Movie;The Panic in Needle Park",
    })
    void givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideos
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedItemsCount, currentPage.items().size());

        int index = 0;
        for (final var expectedTitle : expectedVideos.split(";")) {
            final var currentTitle = currentPage.items().get(index).title();
            Assertions.assertEquals(expectedTitle, currentTitle);
            index++;
        }
    }

    @ParameterizedTest
    @CsvSource({
            "panic,0,10,1,1,The Panic in Needle Park",
            "Movie,0,10,1,1,The Movie",
            "Godfather,0,10,1,1,The Godfather",
            "miss,0,10,1,1,Driving Miss Daisy",
    })
    void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
        // given
        mockVideos();

        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedItemsCount, currentPage.items().size());
        Assertions.assertEquals(expectedVideo, currentPage.items().get(0).title());
    }

    @ParameterizedTest
    @CsvSource({
            "title,asc,0,10,4,4,Driving Miss Daisy",
            "title,desc,0,10,4,4,The Panic in Needle Park",
            "createdAt,asc,0,10,4,4,Driving Miss Daisy",
            "createdAt,desc,0,10,4,4,The Panic in Needle Park",
    })
    void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedVideo
    ) {
        // given
        mockVideos();

        final var expectedTerms = "";

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var currentPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentPage.currentPage());
        Assertions.assertEquals(expectedPerPage, currentPage.perPage());
        Assertions.assertEquals(expectedTotal, currentPage.total());
        Assertions.assertEquals(expectedItemsCount, currentPage.items().size());
        Assertions.assertEquals(expectedVideo, currentPage.items().get(0).title());
    }

    @Test
    void givenAValidVideo_whenCallsFindById_shouldReturnIt() {
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

        final var aVideo = videoGateway.create(
                Video.newVideo(
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
                        .setThumbnailHalf(expectedThumbHalf)
        );

        // when
        final var currentVideo = videoGateway.findById(aVideo.getId()).get();

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
    }

    @Test
    void givenAInvalidVideoId_whenCallsFindById_shouldReturnEmpty() {
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

        final var anId = VideoID.unique();

        // when
        final var currentVideo = videoGateway.findById(anId);

        // then
        Assertions.assertTrue(currentVideo.isEmpty());
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

    private void mockVideos() {

        videoGateway.create(Video.newVideo(
                "Driving Miss Daisy",
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

        videoGateway.create(Video.newVideo(
                "The Godfather",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(trailers.getId()),
                Set.of(drama.getId()),
                Set.of(marlonBrando.getId(), alPacino.getId())
        ));

        videoGateway.create(Video.newVideo(
                "The Movie",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(movies.getId()),
                Set.of(crime.getId()),
                Set.of(marlonBrando.getId())
        ));

        videoGateway.create(Video.newVideo(
                "The Panic in Needle Park",
                Fixture.Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.rating(),
                Set.of(movies.getId()),
                Set.of(drama.getId()),
                Set.of(alPacino.getId())
        ));
    }

}