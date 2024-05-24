package com.studies.catalog.admin.application.castmember.update;

import com.studies.catalog.admin.domain.castmember.CastMemberType;

public record UpdateCastMemberInput(
        String id,
        String name,
        CastMemberType type
) {

    public static UpdateCastMemberInput with(
            final String anId,
            final String aName,
            final CastMemberType aType
    ) {
        return new UpdateCastMemberInput(anId, aName, aType);
    }

}