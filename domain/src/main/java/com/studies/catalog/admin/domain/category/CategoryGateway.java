package com.studies.catalog.admin.domain.category;

import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Pagination<Category> findAll(SearchQuery aQuery);
    Optional<Category> findById(CategoryID anId);
    Category create(Category aCategory);
    Category update(Category aCategory);
    void deleteById(CategoryID anId);
    List<CategoryID> existsByIds(Iterable<CategoryID> ids);

}
