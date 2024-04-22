package com.studies.catalog.admin.domain.category;

import com.studies.catalog.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Pagination<Category> findAll(CategorySearchQuery aQuery);
    Optional<Category> findById(CategoryID anId);
    Category create(Category aCategory);
    Category update(Category aCategory);
    void deleteById(CategoryID anId);

}
