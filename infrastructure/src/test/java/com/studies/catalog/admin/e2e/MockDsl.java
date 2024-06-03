package com.studies.catalog.admin.e2e;

import com.studies.catalog.admin.domain.Identifier;
import com.studies.catalog.admin.domain.castmember.CastMemberID;
import com.studies.catalog.admin.domain.castmember.CastMemberType;
import com.studies.catalog.admin.domain.category.CategoryID;
import com.studies.catalog.admin.domain.genre.GenreID;
import com.studies.catalog.admin.infrastructure.castmember.models.CreateCastMemberApiRequest;
import com.studies.catalog.admin.infrastructure.category.models.CategoryApiResponse;
import com.studies.catalog.admin.infrastructure.category.models.CreateCategoryApiRequest;
import com.studies.catalog.admin.infrastructure.category.models.UpdateCategoryApiRequest;
import com.studies.catalog.admin.infrastructure.configuration.json.Json;
import com.studies.catalog.admin.infrastructure.genre.models.CreateGenreApiRequest;
import com.studies.catalog.admin.infrastructure.genre.models.GenreApiResponse;
import com.studies.catalog.admin.infrastructure.genre.models.UpdateGenreApiRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    /**
     * Cast Member
     */

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search) throws Exception {
        return listCastMembers(page, perPage, search, "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/cast_members", page, perPage, search, sort, direction);
    }

    default ResultActions givenACastMemberResult(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberApiRequest(aName, aType);

        return this.givenResult("/cast_members", aRequestBody);
    }

    default CastMemberID givenACastMember(final String aName, final CastMemberType aType) throws Exception {
        final var aRequestBody = new CreateCastMemberApiRequest(aName, aType);
        final var currentId = this.given("/cast_members", aRequestBody);

        return CastMemberID.from(currentId);
    }

    /**
     * Genre
     */

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(page, perPage, search, "", "");
    }

    default GenreApiResponse retrieveAGenre(final Identifier anId) throws Exception {
        return this.retrieve("/genres/", anId, GenreApiResponse.class);
    }

    default ResultActions updateAGenre(final Identifier anId, final UpdateGenreApiRequest aRequest) throws Exception {
        return this.update("/genres/", anId, aRequest);
    }

    default ResultActions deleteAGenre(final Identifier anId) throws Exception {
        return this.delete("/genres/", anId);
    }

    default ResultActions listGenres(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/genres", page, perPage, search, sort, direction);
    }

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreApiRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var currentId = this.given("/genres", aRequestBody);

        return GenreID.from(currentId);
    }

    /**
     * Category
     */

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryApiRequest(aName, aDescription, isActive);
        final var currentId = this.given("/categories", aRequestBody);

        return CategoryID.from(currentId);
    }

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return this.delete("/categories/", anId);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/categories", page, perPage, search, sort, direction);
    }

    default CategoryApiResponse retrieveACategory(final Identifier anId) throws Exception {
        return this.retrieve("/categories/", anId, CategoryApiResponse.class);
    }

    default ResultActions updateACategory(final Identifier anId, final UpdateCategoryApiRequest aRequest) throws Exception {
        return this.update("/categories/", anId, aRequest);
    }

    default <A, D> List<D> mapTo(final List<A> current, final Function<A, D> mapper) {
        return current.stream()
                .map(mapper)
                .toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return this.mvc().perform(aRequest);
    }

    private ResultActions list(final String url, final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        final var aRequest = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest = put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }

}