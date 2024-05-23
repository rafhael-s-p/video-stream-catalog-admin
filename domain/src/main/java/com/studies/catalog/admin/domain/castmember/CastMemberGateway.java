package com.studies.catalog.admin.domain.castmember;

import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;

import java.util.Optional;

public interface CastMemberGateway {

    Pagination<CastMember> findAll(SearchQuery aQuery);

    Optional<CastMember> findById(CastMemberID anId);

    CastMember create(CastMember aCastMember);

    CastMember update(CastMember aCastMember);

    void deleteById(CastMemberID anId);

}