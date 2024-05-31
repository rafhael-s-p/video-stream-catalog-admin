package com.studies.catalog.admin.infrastructure.castmember.models;

import com.studies.catalog.admin.domain.castmember.CastMemberType;

public record CreateCastMemberApiRequest(String name, CastMemberType type) {
}