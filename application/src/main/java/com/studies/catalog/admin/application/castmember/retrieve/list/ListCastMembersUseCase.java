package com.studies.catalog.admin.application.castmember.retrieve.list;

import com.studies.catalog.admin.application.UseCase;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;

public abstract sealed class ListCastMembersUseCase
        extends UseCase<SearchQuery, Pagination<CastMemberListOutput>>
        permits ListCastMembersUseCaseImpl {
}