package com.studies.catalog.admin.application.video.update;

import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.application.UseCaseTest;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryGateway;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.DomainException;
import com.studies.catalog.admin.domain.exceptions.InternalErrorException;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.GenreGateway;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.utils.IdUtils;
import com.studies.catalog.admin.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UpdateVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private UpdateVideoUseCaseImpl useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
    }

    @Test
    void givenAValidInput_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.marlonBrando().getId(),
                Fixture.CastMembers.alPacino().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockVideoMedia();

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentResult = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentResult);
        Assertions.assertNotNull(currentResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(currentVideo ->
                Objects.equals(expectedTitle, currentVideo.getTitle())
                        && Objects.equals(expectedDescription, currentVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, currentVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, currentVideo.getDuration())
                        && Objects.equals(expectedOpened, currentVideo.getOpened())
                        && Objects.equals(expectedPublished, currentVideo.getPublished())
                        && Objects.equals(expectedRating, currentVideo.getRating())
                        && Objects.equals(expectedCategories, currentVideo.getCategories())
                        && Objects.equals(expectedGenres, currentVideo.getGenres())
                        && Objects.equals(expectedMembers, currentVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), currentVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), currentVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), currentVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), currentVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), currentVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), currentVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidInputWithoutCategories_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.marlonBrando().getId(),
                Fixture.CastMembers.alPacino().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockVideoMedia();

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentResult = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentResult);
        Assertions.assertNotNull(currentResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(currentVideo ->
                Objects.equals(expectedTitle, currentVideo.getTitle())
                        && Objects.equals(expectedDescription, currentVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, currentVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, currentVideo.getDuration())
                        && Objects.equals(expectedOpened, currentVideo.getOpened())
                        && Objects.equals(expectedPublished, currentVideo.getPublished())
                        && Objects.equals(expectedRating, currentVideo.getRating())
                        && Objects.equals(expectedCategories, currentVideo.getCategories())
                        && Objects.equals(expectedGenres, currentVideo.getGenres())
                        && Objects.equals(expectedMembers, currentVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), currentVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), currentVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), currentVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), currentVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), currentVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), currentVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidInputWithoutGenres_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(
                Fixture.CastMembers.marlonBrando().getId(),
                Fixture.CastMembers.alPacino().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        mockImageMedia();
        mockVideoMedia();

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentResult = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentResult);
        Assertions.assertNotNull(currentResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(currentVideo ->
                Objects.equals(expectedTitle, currentVideo.getTitle())
                        && Objects.equals(expectedDescription, currentVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, currentVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, currentVideo.getDuration())
                        && Objects.equals(expectedOpened, currentVideo.getOpened())
                        && Objects.equals(expectedPublished, currentVideo.getPublished())
                        && Objects.equals(expectedRating, currentVideo.getRating())
                        && Objects.equals(expectedCategories, currentVideo.getCategories())
                        && Objects.equals(expectedGenres, currentVideo.getGenres())
                        && Objects.equals(expectedMembers, currentVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), currentVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), currentVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), currentVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), currentVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), currentVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), currentVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidInputWithoutCastMembers_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockVideoMedia();

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentResult = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentResult);
        Assertions.assertNotNull(currentResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(currentVideo ->
                Objects.equals(expectedTitle, currentVideo.getTitle())
                        && Objects.equals(expectedDescription, currentVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, currentVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, currentVideo.getDuration())
                        && Objects.equals(expectedOpened, currentVideo.getOpened())
                        && Objects.equals(expectedPublished, currentVideo.getPublished())
                        && Objects.equals(expectedRating, currentVideo.getRating())
                        && Objects.equals(expectedCategories, currentVideo.getCategories())
                        && Objects.equals(expectedGenres, currentVideo.getGenres())
                        && Objects.equals(expectedMembers, currentVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), currentVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), currentVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), currentVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), currentVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), currentVideo.getThumbnailHalf().get().name())
                        && Objects.equals(aVideo.getCreatedAt(), currentVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt())
        ));
    }

    @Test
    void givenAValidInputWithoutResources_whenCallsUpdateVideo_shouldReturnVideoId() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.marlonBrando().getId(),
                Fixture.CastMembers.alPacino().getId()
        );
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var currentResult = useCase.execute(anInput);

        // then
        Assertions.assertNotNull(currentResult);
        Assertions.assertNotNull(currentResult.id());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(videoGateway).update(argThat(currentVideo ->
                Objects.equals(expectedTitle, currentVideo.getTitle())
                        && Objects.equals(expectedDescription, currentVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, currentVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, currentVideo.getDuration())
                        && Objects.equals(expectedOpened, currentVideo.getOpened())
                        && Objects.equals(expectedPublished, currentVideo.getPublished())
                        && Objects.equals(expectedRating, currentVideo.getRating())
                        && Objects.equals(expectedCategories, currentVideo.getCategories())
                        && Objects.equals(expectedGenres, currentVideo.getGenres())
                        && Objects.equals(expectedMembers, currentVideo.getCastMembers())
                        && currentVideo.getVideo().isEmpty()
                        && currentVideo.getTrailer().isEmpty()
                        && currentVideo.getBanner().isEmpty()
                        && currentVideo.getThumbnail().isEmpty()
                        && currentVideo.getThumbnailHalf().isEmpty()
                        && Objects.equals(aVideo.getCreatedAt(), currentVideo.getCreatedAt())
                        && aVideo.getUpdatedAt().isBefore(currentVideo.getUpdatedAt())
        ));
    }

    @Test
    void givenANullTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        // when
        final var currentException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeVideo(any(), any());
        verify(videoGateway, times(0)).update(any());
    }

    @Test
    void givenAEmptyTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final String expectedTitle = " ";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        // when
        final var currentException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeVideo(any(), any());
        verify(videoGateway, times(0)).update(any());
    }

    @Test
    void givenANullRating_whenCallsUpdateVideo_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        // when
        final var currentException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeVideo(any(), any());
        verify(videoGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidRating_whenCallsUpdateVideo_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = "ADASDA";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        // when
        final var currentException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeVideo(any(), any());
        verify(videoGateway, times(0)).update(any());
    }

    @Test
    void givenANullLaunchedAt_whenCallsUpdateVideo_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();

        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchYear = null;
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        // when
        final var currentException = Assertions.assertThrows(DomainException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(videoGateway).findById(eq(aVideo.getId()));

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(mediaResourceGateway, times(0)).storeVideo(any(), any());
        verify(videoGateway, times(0)).update(any());
    }

    @Test
    void givenAValidInput_whenCallsUpdateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var moviesId = Fixture.Categories.movies().getId();

        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(moviesId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(moviesId);
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.marlonBrando().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).update(any());
    }

    @Test
    void givenAValidInput_whenCallsUpdateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var techId = Fixture.Genres.crime().getId();

        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(techId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.of(techId);
        final var expectedMembers = Set.of(Fixture.CastMembers.marlonBrando().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    void givenAValidInput_whenCallsUpdateVideoAndSomeCastMembersDoesNotExists_shouldReturnDomainException() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var marlonBrandoId = Fixture.CastMembers.marlonBrando().getId();

        final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(marlonBrandoId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.of(marlonBrandoId);
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>());

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        // when
        final var currentException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentException);
        Assertions.assertEquals(expectedErrorCount, currentException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, currentException.getErrors().get(0).message());

        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
        verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
        verify(mediaResourceGateway, times(0)).storeImage(any(), any());
        verify(videoGateway, times(0)).create(any());
    }

    @Test
    void givenAValidInput_whenCallsCreateVideoThrowsException_shouldCallClearResources() {
        // given
        final var aVideo = Fixture.Videos.theGodfather();
        final var expectedErrorMessage = "An error on create video was observed [videoId:";

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.movies().getId());
        final var expectedGenres = Set.of(Fixture.Genres.crime().getId());
        final var expectedMembers = Set.of(
                Fixture.CastMembers.marlonBrando().getId(),
                Fixture.CastMembers.alPacino().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

        final var anInput = UpdateVideoInput.with(
                aVideo.getId().getValue(),
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedMembers),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockVideoMedia();

        when(videoGateway.update(any()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        // when
        final var currentResult = Assertions.assertThrows(InternalErrorException.class, () -> useCase.execute(anInput));

        // then
        Assertions.assertNotNull(currentResult);
        Assertions.assertTrue(currentResult.getMessage().startsWith(expectedErrorMessage));

        verify(mediaResourceGateway, times(0)).clearResources(any());
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any())).thenAnswer(t -> {
            final var resource = t.getArgument(1, Resource.class);
            return ImageMedia.with(UUID.randomUUID().toString(), resource.name(), "/img");
        });
    }

    private void mockVideoMedia() {
        when(mediaResourceGateway.storeVideo(any(), any())).thenAnswer(t -> {
            final var resource = t.getArgument(1, Resource.class);
            return VideoMedia.with(
                    IdUtils.uuid(),
                    IdUtils.uuid(),
                    resource.name(),
                    "/img",
                    "",
                    MediaStatus.PENDING
            );
        });
    }

}