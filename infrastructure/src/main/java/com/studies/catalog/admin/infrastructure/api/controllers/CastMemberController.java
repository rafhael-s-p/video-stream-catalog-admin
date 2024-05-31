package com.studies.catalog.admin.infrastructure.api.controllers;

import com.studies.catalog.admin.application.castmember.create.CreateCastMemberInput;
import com.studies.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.studies.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.studies.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.studies.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.studies.catalog.admin.application.castmember.update.UpdateCastMemberInput;
import com.studies.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.studies.catalog.admin.domain.pagination.Pagination;
import com.studies.catalog.admin.domain.pagination.SearchQuery;
import com.studies.catalog.admin.infrastructure.api.CastMemberAPI;
import com.studies.catalog.admin.infrastructure.castmember.models.CastMemberApiResponse;
import com.studies.catalog.admin.infrastructure.castmember.models.CastMemberListApiResponse;
import com.studies.catalog.admin.infrastructure.castmember.models.CreateCastMemberApiRequest;
import com.studies.catalog.admin.infrastructure.castmember.models.UpdateCastMemberApiRequest;
import com.studies.catalog.admin.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final ListCastMembersUseCase listCastMembersUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;

    public CastMemberController(final ListCastMembersUseCase listCastMembersUseCase,
                                final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
                                final CreateCastMemberUseCase createCastMemberUseCase,
                                final UpdateCastMemberUseCase updateCastMemberUseCase,
                                final DeleteCastMemberUseCase deleteCastMemberUseCase) {
        this.listCastMembersUseCase = listCastMembersUseCase;
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
    }

    @Override
    public Pagination<CastMemberListApiResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listCastMembersUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberPresenter::present);
    }

    @Override
    public CastMemberApiResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberApiRequest input) {
        final var anInput =
                CreateCastMemberInput.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(anInput);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberApiRequest aBody) {
        final var anInput =
                UpdateCastMemberInput.with(id, aBody.name(), aBody.type());

        final var output = this.updateCastMemberUseCase.execute(anInput);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }

}