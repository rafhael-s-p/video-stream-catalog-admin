package com.studies.catalog.admin.application.genre.create;

import java.util.List;

public record CreateGenreInput(
        String name,
        boolean isActive,
        List<String> categories
) {

    public static CreateGenreInput with(
            final String aName,
            final Boolean isActive,
            final List<String> categories
    ) {
        return new CreateGenreInput(aName, isActive != null ? isActive : true, categories);
    }
}