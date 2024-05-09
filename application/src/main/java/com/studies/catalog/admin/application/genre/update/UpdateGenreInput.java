package com.studies.catalog.admin.application.genre.update;

import java.util.List;

public record UpdateGenreInput(
        String id,
        String name,
        boolean isActive,
        List<String> categories
) {

    public static UpdateGenreInput with(
            final String id,
            final String name,
            final Boolean isActive,
            final List<String> categories
    ) {
        return new UpdateGenreInput(id, name, isActive != null ? isActive : true, categories);
    }

}