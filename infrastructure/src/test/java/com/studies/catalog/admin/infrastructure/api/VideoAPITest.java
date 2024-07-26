package com.studies.catalog.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.catalog.admin.ControllerTest;
import com.studies.catalog.admin.application.video.create.CreateVideoInput;
import com.studies.catalog.admin.application.video.create.CreateVideoOutput;
import com.studies.catalog.admin.application.video.create.CreateVideoUseCase;
import com.studies.catalog.admin.application.video.delete.DeleteVideoUseCase;
import com.studies.catalog.admin.application.video.media.get.GetMediaUseCase;
import com.studies.catalog.admin.application.video.media.get.MediaOutput;
import com.studies.catalog.admin.application.video.media.upload.UploadMediaInput;
import com.studies.catalog.admin.application.video.media.upload.UploadMediaOutput;
import com.studies.catalog.admin.application.video.media.upload.UploadMediaUseCase;
import com.studies.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.studies.catalog.admin.application.video.retrieve.get.VideoOutput;
import com.studies.catalog.admin.application.video.retrieve.list.ListVideosUseCase;
import com.studies.catalog.admin.application.video.retrieve.list.VideoListOutput;
import com.studies.catalog.admin.application.video.update.UpdateVideoInput;
import com.studies.catalog.admin.application.video.update.UpdateVideoOutput;
import com.studies.catalog.admin.application.video.update.UpdateVideoUseCase;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.exceptions.NotificationException;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.validation.Error;
import com.studies.catalog.admin.domain.video.*;
import com.studies.catalog.admin.infrastructure.video.models.CreateVideoApiRequest;
import com.studies.catalog.admin.infrastructure.video.models.UpdateVideoApiRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.studies.catalog.admin.domain.utils.CollectionUtils.mapTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ListVideosUseCase listVideosUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private GetMediaUseCase getMediaUseCase;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private UploadMediaUseCase uploadMediaUseCase;

    @Test
    void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
        // given
        final var aVideo = new VideoPreview(Fixture.video());

        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Some";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCategories = "cat1";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedCastMembers)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var currentQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, currentQuery.page());
        Assertions.assertEquals(expectedPerPage, currentQuery.perPage());
        Assertions.assertEquals(expectedDirection, currentQuery.direction());
        Assertions.assertEquals(expectedSort, currentQuery.sort());
        Assertions.assertEquals(expectedTerms, currentQuery.terms());
        Assertions.assertEquals(Set.of(CategoryID.from(expectedCategories)), currentQuery.categories());
        Assertions.assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), currentQuery.castMembers());
        Assertions.assertEquals(Set.of(GenreID.from(expectedGenres)), currentQuery.genres());
    }

    @Test
    void givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() throws Exception {
        // given
        final var aVideo = new VideoPreview(Fixture.video());

        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "";
        final var expectedGenres = "";
        final var expectedCategories = "";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos")
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var currentQuery = captor.getValue();

        Assertions.assertEquals(expectedPage, currentQuery.page());
        Assertions.assertEquals(expectedPerPage, currentQuery.perPage());
        Assertions.assertEquals(expectedDirection, currentQuery.direction());
        Assertions.assertEquals(expectedSort, currentQuery.sort());
        Assertions.assertEquals(expectedTerms, currentQuery.terms());
        Assertions.assertTrue(currentQuery.categories().isEmpty());
        Assertions.assertTrue(currentQuery.castMembers().isEmpty());
        Assertions.assertTrue(currentQuery.genres().isEmpty());
    }

    @Test
    void givenAValidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        // given
        final var marlonBrando = Fixture.CastMembers.marlonBrando();
        final var movies = Fixture.Categories.movies();
        final var crime = Fixture.Genres.crime();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(movies.getId().getValue());
        final var expectedGenres = Set.of(crime.getId().getValue());
        final var expectedMembers = Set.of(marlonBrando.getId().getValue());

        final var expectedVideo = Fixture.Videos.video(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.video(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        mapTo(expectedCategories, CategoryID::from),
                        mapTo(expectedGenres, GenreID::from),
                        mapTo(expectedMembers, CastMemberID::from)
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any()))
                .thenReturn(VideoOutput.from(aVideo));

        // when
        final var aRequest = get("/videos/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum())))
                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList(expectedMembers))));
    }

    @Test
    void givenAValidVideoIdAndFileType_whenCallsGetMediaById_shouldReturnContent() throws Exception {
        // given
        final var expectedId = VideoID.unique();

        final var expectedMediaType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedMediaType);

        final var expectedMedia = new MediaOutput(expectedResource.content(), expectedResource.contentType(), expectedResource.name());

        when(getMediaUseCase.execute(any())).thenReturn(expectedMedia);

        // when
        final var aRequest = get("/videos/{id}/medias/{type}", expectedId.getValue(), expectedMediaType.name());

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string(CONTENT_TYPE, expectedMedia.contentType()))
                .andExpect(header().string(CONTENT_LENGTH, String.valueOf(expectedMedia.content().length)))
                .andExpect(header().string(CONTENT_DISPOSITION, "attachment; filename=%s".formatted(expectedMedia.name())))
                .andExpect(content().bytes(expectedMedia.content()));
    }

    @Test
    void givenAValidInput_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        // given
        final var marlonBrando = Fixture.CastMembers.marlonBrando();
        final var movies = Fixture.Categories.movies();
        final var crime = Fixture.Genres.crime();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(movies.getId().getValue());
        final var expectedGenres = Set.of(crime.getId().getValue());
        final var expectedMembers = Set.of(marlonBrando.getId().getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());

        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());

        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());

        final var expectedThumb =
                new MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".getBytes());

        final var expectedThumbHalf =
                new MockMultipartFile("thumb_half_file", "thumbnailHalf.jpg", "image/jpg", "THUMBHALF".getBytes());

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        // when
        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", expectedDuration.toString())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", marlonBrando.getId().getValue())
                .param("categories_id", movies.getId().getValue())
                .param("genres_id", crime.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        // then
        final var inputCaptor = ArgumentCaptor.forClass(CreateVideoInput.class);

        verify(createVideoUseCase).execute(inputCaptor.capture());

        final var currentInput = inputCaptor.getValue();

        Assertions.assertEquals(expectedTitle, currentInput.title());
        Assertions.assertEquals(expectedDescription, currentInput.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), currentInput.launchedAt());
        Assertions.assertEquals(expectedDuration, currentInput.duration());
        Assertions.assertEquals(expectedOpened, currentInput.opened());
        Assertions.assertEquals(expectedPublished, currentInput.published());
        Assertions.assertEquals(expectedRating.getName(), currentInput.rating());
        Assertions.assertEquals(expectedCategories, currentInput.categories());
        Assertions.assertEquals(expectedGenres, currentInput.genres());
        Assertions.assertEquals(expectedMembers, currentInput.members());
        Assertions.assertEquals(expectedVideo.getOriginalFilename(), currentInput.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), currentInput.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), currentInput.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.getOriginalFilename(), currentInput.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.getOriginalFilename(), currentInput.getThumbnailHalf().get().name());
    }

    @Test
    void givenAValidInput_whenCallsCreatePartial_shouldReturnId() throws Exception {
        // given
        final var marlonBrando = Fixture.CastMembers.marlonBrando();
        final var movies = Fixture.Categories.movies();
        final var crime = Fixture.Genres.crime();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(movies.getId().getValue());
        final var expectedGenres = Set.of(crime.getId().getValue());
        final var expectedMembers = Set.of(marlonBrando.getId().getValue());

        final var anInput = new CreateVideoApiRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        // when

        final var aRequest = post("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        // then
        final var inputCaptor = ArgumentCaptor.forClass(CreateVideoInput.class);

        verify(createVideoUseCase).execute(inputCaptor.capture());

        final var currentInput = inputCaptor.getValue();

        Assertions.assertEquals(expectedTitle, currentInput.title());
        Assertions.assertEquals(expectedDescription, currentInput.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), currentInput.launchedAt());
        Assertions.assertEquals(expectedDuration, currentInput.duration());
        Assertions.assertEquals(expectedOpened, currentInput.opened());
        Assertions.assertEquals(expectedPublished, currentInput.published());
        Assertions.assertEquals(expectedRating.getName(), currentInput.rating());
        Assertions.assertEquals(expectedCategories, currentInput.categories());
        Assertions.assertEquals(expectedGenres, currentInput.genres());
        Assertions.assertEquals(expectedMembers, currentInput.members());
        Assertions.assertTrue(currentInput.getVideo().isEmpty());
        Assertions.assertTrue(currentInput.getTrailer().isEmpty());
        Assertions.assertTrue(currentInput.getBanner().isEmpty());
        Assertions.assertTrue(currentInput.getThumbnail().isEmpty());
        Assertions.assertTrue(currentInput.getThumbnailHalf().isEmpty());
    }

    @Test
    void givenAValidInput_whenCallsUpdateVideo_shouldReturnVideoId() throws Exception {
        // given
        final var marlonBrando = Fixture.CastMembers.marlonBrando();
        final var movies = Fixture.Categories.movies();
        final var crime = Fixture.Genres.crime();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(movies.getId().getValue());
        final var expectedGenres = Set.of(crime.getId().getValue());
        final var expectedMembers = Set.of(marlonBrando.getId().getValue());

        final var anInput = new UpdateVideoApiRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(updateVideoUseCase.execute(any()))
                .thenReturn(new UpdateVideoOutput(expectedId.getValue()));

        // when

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        this.mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var inputCaptor = ArgumentCaptor.forClass(UpdateVideoInput.class);

        verify(updateVideoUseCase).execute(inputCaptor.capture());

        final var currentInput = inputCaptor.getValue();

        Assertions.assertEquals(expectedTitle, currentInput.title());
        Assertions.assertEquals(expectedDescription, currentInput.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), currentInput.launchedAt());
        Assertions.assertEquals(expectedDuration, currentInput.duration());
        Assertions.assertEquals(expectedOpened, currentInput.opened());
        Assertions.assertEquals(expectedPublished, currentInput.published());
        Assertions.assertEquals(expectedRating.getName(), currentInput.rating());
        Assertions.assertEquals(expectedCategories, currentInput.categories());
        Assertions.assertEquals(expectedGenres, currentInput.genres());
        Assertions.assertEquals(expectedMembers, currentInput.members());
        Assertions.assertTrue(currentInput.getVideo().isEmpty());
        Assertions.assertTrue(currentInput.getTrailer().isEmpty());
        Assertions.assertTrue(currentInput.getBanner().isEmpty());
        Assertions.assertTrue(currentInput.getThumbnail().isEmpty());
        Assertions.assertTrue(currentInput.getThumbnailHalf().isEmpty());
    }

    @Test
    void givenAnInvalidInput_whenCallsUpdateVideo_shouldReturnNotification() throws Exception {
        // given
        final var marlonBrando = Fixture.CastMembers.marlonBrando();
        final var movies = Fixture.Categories.movies();
        final var crime = Fixture.Genres.crime();

        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(movies.getId().getValue());
        final var expectedGenres = Set.of(crime.getId().getValue());
        final var expectedMembers = Set.of(marlonBrando.getId().getValue());

        final var anInput = new UpdateVideoApiRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(updateVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when

        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        // given
        final var expectedId = VideoID.unique();

        doNothing().when(deleteVideoUseCase).execute(any());

        // when
        final var aRequest = delete("/videos/{id}", expectedId.getValue());

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isNoContent());

        verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    void givenAValidVideoIdAndFile_whenCallsUploadMedia_shouldStoreIt() throws Exception {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        when(uploadMediaUseCase.execute(any()))
                .thenReturn(new UploadMediaOutput(expectedId.getValue(), expectedType));

        // when
        final var aRequest = multipart("/videos/{id}/medias/{type}", expectedId.getValue(), expectedType.name())
                .file(expectedVideo)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/videos/%s/medias/%s".formatted(expectedId.getValue(), expectedType.name())))
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.video_id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.media_type", equalTo(expectedType.name())));

        final var captor = ArgumentCaptor.forClass(UploadMediaInput.class);

        verify(this.uploadMediaUseCase).execute(captor.capture());

        final var currentInput = captor.getValue();

        Assertions.assertEquals(expectedId.getValue(), currentInput.videoId());
        Assertions.assertEquals(expectedResource.content(), currentInput.videoResource().resource().content());
        Assertions.assertEquals(expectedResource.name(), currentInput.videoResource().resource().name());
        Assertions.assertEquals(expectedResource.contentType(), currentInput.videoResource().resource().contentType());
        Assertions.assertEquals(expectedType, currentInput.videoResource().type());
    }

    @Test
    void givenAnInvalidMediaType_whenCallsUploadMedia_shouldReturnError() throws Exception {
        // given
        final var expectedId = VideoID.unique();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var expectedVideo =
                new MockMultipartFile("media_file", expectedResource.name(), expectedResource.contentType(), expectedResource.content());

        // when
        final var aRequest = multipart("/videos/{id}/medias/INVALID", expectedId.getValue())
                .file(expectedVideo)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo("Invalid INVALID for VideoMediaType")));
    }

}