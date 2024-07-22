package com.studies.catalog.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studies.catalog.admin.ControllerTest;
import com.studies.catalog.admin.application.video.create.CreateVideoInput;
import com.studies.catalog.admin.application.video.create.CreateVideoOutput;
import com.studies.catalog.admin.application.video.create.CreateVideoUseCase;
import com.studies.catalog.admin.domain.Fixture;
import com.studies.catalog.admin.domain.video.VideoID;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

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

}