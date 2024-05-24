package com.studies.catalog.admin.application.castmember.retrieve.get;

import com.studies.catalog.admin.application.UseCase;

public abstract sealed class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits GetCastMemberByIdUseCaseImpl {
}