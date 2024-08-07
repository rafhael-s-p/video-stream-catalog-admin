package com.studies.catalog.admin.domain;

import com.github.javafaker.Faker;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberType;
import com.studies.catalog.admin.domain.category.Category;
import com.studies.catalog.admin.domain.genre.Genre;
import com.studies.catalog.admin.domain.utils.IdUtils;
import com.studies.catalog.admin.domain.video.*;

import java.time.Year;
import java.util.Set;

import static io.vavr.API.*;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static String description255() {
        return FAKER.lorem().fixedString(255);
    }

    public static String description258() {
        return FAKER.lorem().fixedString(258);
    }

    public static String description4000() {
        return FAKER.lorem().fixedString(4000);
    }

    public static String description4003() {
        return FAKER.lorem().fixedString(4003);
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

    public static String checksum() {
        return "03fe62de";
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

        private static final Category TRAILERS =
                Category.newCategory("Trailers", "Some description", true);

        public static Category movies() {
            return MOVIES.clone();
        }

        public static Category trailers() {
            return TRAILERS.clone();
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

        private static final Genre DRAMA =
                Genre.newGenre("drama", true);

        public static Genre crime() {
            return Genre.with(CRIME);
        }

        public static Genre drama() {
            return Genre.with(DRAMA);
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

        public static VideoMediaType mediaType() {
            return FAKER.options().option(VideoMediaType.values());
        }

        public static Resource resource(final VideoMediaType type) {
            final String contentType = Match(type).of(
                    Case($(List(VideoMediaType.VIDEO, VideoMediaType.TRAILER)::contains), "video/mp4"),
                    Case($(), "image/jpg")
            );

            final String checksum = IdUtils.uuid();
            final byte[] content = "Content".getBytes();

            return Resource.with(content, checksum, contentType, type.name().toLowerCase());
        }

        public static String description() {
            return FAKER.options().option(
                    FAKER.lorem().fixedString(1000),
                    FAKER.lorem().fixedString(2000)
            );
        }

        public static VideoMedia video(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return VideoMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/videos/" + checksum
            );
        }

        public static ImageMedia image(final VideoMediaType type) {
            final var checksum = Fixture.checksum();
            return ImageMedia.with(
                    checksum,
                    type.name().toLowerCase(),
                    "/images/" + checksum
            );
        }
    }

}