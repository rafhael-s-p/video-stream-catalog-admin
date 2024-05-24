package com.studies.catalog.admin.application.castmember.create;

import com.studies.catalog.admin.domain.castmember.CastMemberType;

public record CreateCastMemberInput(
        String name,
        CastMemberType type
) {

    public static CreateCastMemberInput with(final String aName, final CastMemberType aType) {
        return new CreateCastMemberInput(aName, aType);
    }

}