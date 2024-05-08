package com.studies.catalog.admin.domain.genre;

import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Pagination<Genre> findAll(SearchQuery aQuery);

    Optional<Genre> findById(GenreID anId);

    Genre create(Genre aGenre);

    Genre update(Genre aGenre);

    void deleteById(GenreID anId);

}