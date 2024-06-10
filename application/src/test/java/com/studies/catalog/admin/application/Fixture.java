package com.studies.catalog.admin.application;

import com.github.javafaker.Faker;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberType;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.video.Rating;
import com.studies.catalog.admin.domain.video.Resource;
import com.studies.catalog.admin.domain.video.Video;

import java.time.Year;
import java.util.Set;

import static io.vavr.API.*;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(175.0, 202.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "The Godfather",
                "The Godfather Part II",
                "The Dark Knight"
        );
    }

    public static Video video() {
        return Video.newVideo(
                Fixture.title(),
                Videos.description(),
                Year.of(Fixture.year()),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                Videos.rating(),
                Set.of(Categories.movies().getId()),
                Set.of(Genres.crime().getId()),
                Set.of(CastMembers.marlonBrando().getId(), CastMembers.alPacino().getId())
        );
    }

    public static final class Categories {

        private static final Category MOVIES =
                Category.newCategory("Movies", "Some description", true);

        public static Category movies() {
            return MOVIES.clone();
        }
    }

    public static final class CastMembers {

        private static final CastMember MARLON_BRANDO =
                CastMember.newMember("Marlon Brando", CastMemberType.ACTOR);

        private static final CastMember AL_PACINO =
                CastMember.newMember("Al Pacino", CastMemberType.ACTOR);

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember marlonBrando() {
            return CastMember.with(MARLON_BRANDO);
        }

        public static CastMember alPacino() {
            return CastMember.with(AL_PACINO);
        }
    }

    public static final class Genres {

        private static final Genre CRIME =
                Genre.newGenre("Crime", true);

        public static Genre crime() {
            return Genre.with(CRIME);
        }
    }

    public static final class Videos {

        private static final Video THE_GODFATHER = Video.newVideo(
                "The Godfather",
                description(),
                Year.of(1972),
                Fixture.duration(),
                Fixture.bool(),
                Fixture.bool(),
                rating(),
                Set.of(Categories.movies().getId()),
                Set.of(Genres.crime().getId()),
                Set.of(CastMembers.marlonBrando().getId(), CastMembers.alPacino().getId())
        );

        public static Video theGodfather() {
            return Video.with(THE_GODFATHER);
        }

        public static Rating rating() {
            return FAKER.options().option(Rating.values());
        }

        public static Resource resource(final Resource.Type type) {
            final String contentType = Match(type).of(
                    Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final byte[] content = "Content".getBytes();

            return Resource.with(content, contentType, type.name().toLowerCase(), type);
        }

        public static String description() {
            return FAKER.options().option(
                    FAKER.lorem().fixedString(1000),
                    FAKER.lorem().fixedString(2000)
            );
        }
    }

}