package com.studies.catalog.admin.application.castmember.retrieve.list;

import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;

import java.util.Objects;

public non-sealed class ListCastMembersUseCaseImpl extends ListCastMembersUseCase {

    private final CastMemberGateway castMemberGateway;

    public ListCastMembersUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(final SearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery)
                .map(CastMemberListOutput::from);
    }

}