package com.studies.catalog.admin.application.castmember.retrieve.list;

import com.studies.catalog.admin.Fixture;
import com.studies.catalog.admin.IntegrationTest;
import com.studies.catalog.admin.domain.castmember.CastMember;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.studies.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@IntegrationTest
class ListCastMembersUseCaseIT {

    @Autowired
    private ListCastMembersUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        // given
        final var members = List.of(
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type()),
                CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
        );

        this.castMemberRepository.saveAllAndFlush(
                members.stream()
                        .map(CastMemberJpaEntity::from)
                        .toList()
        );

        Assertions.assertEquals(2, this.castMemberRepository.count());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertTrue(expectedItems.size() == currentOutput.items().size());
        Assertions.assertTrue(expectedItems.containsAll(currentOutput.items()));

        verify(castMemberGateway).findAll(any());
    }

    @Test
    void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<CastMemberListOutput>of();

        Assertions.assertEquals(0, this.castMemberRepository.count());

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, currentOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, currentOutput.perPage());
        Assertions.assertEquals(expectedTotal, currentOutput.total());
        Assertions.assertEquals(expectedItems, currentOutput.items());

        verify(castMemberGateway).findAll(any());
    }

    @Test
    void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(castMemberGateway).findAll(any());

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var currentException = Assertions.assertThrows(IllegalStateException.class, () -> {
            useCase.execute(aQuery);
        });

        // then
        Assertions.assertEquals(expectedErrorMessage, currentException.getMessage());

        verify(castMemberGateway).findAll(any());
    }

}