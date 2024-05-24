package com.studies.catalog.admin.application.castmember.create;

import com.studies.catalog.admin.domain.castmember.CastMember;

public record CreateCastMemberOutput(
        String id
) {

    public static CreateCastMemberOutput from(final CastMember aMember) {
        return new CreateCastMemberOutput(aMember.getId().getValue());
    }

}