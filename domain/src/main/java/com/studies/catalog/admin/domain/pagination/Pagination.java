package com.studies.catalog.admin.domain.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<GenericType>(int currentPage, int perPage, long total, List<GenericType> items) {

    public <GenericResult> Pagination<GenericResult> map(final Function<GenericType, GenericResult> mapper) {
        final List<GenericResult> aNewList = this.items.stream()
                .map(mapper)
                .toList();

        return new Pagination<>(currentPage(), perPage(), total(), aNewList);
    }

}