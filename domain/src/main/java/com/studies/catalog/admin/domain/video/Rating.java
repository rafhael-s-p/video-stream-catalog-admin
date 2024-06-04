package com.studies.catalog.admin.domain.video;

import java.util.Arrays;
import java.util.Optional;

public enum Rating {

    TV_Y("All Children"),
    TV_Y7("Directed to Older Children"),
    TV_G("General Audience"),
    TV_PG("Parental Guidance Suggested"),
    TV_14("Parents Strongly Cautioned"),
    TV_MA("Mature Audience Only");

    private final String name;

    Rating(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<Rating> of(final String label) {
        return Arrays.stream(Rating.values())
                .filter(rating -> rating.name.equalsIgnoreCase(label))
                .findFirst();
    }

}