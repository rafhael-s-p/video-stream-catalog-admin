package com.studies.catalog.admin.infrastructure.configuration.usecases;

import com.studies.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.studies.catalog.admin.application.castmember.create.CreateCastMemberUseCaseImpl;
import com.studies.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.studies.catalog.admin.application.castmember.delete.DeleteCastMemberUseCaseImpl;
import com.studies.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.studies.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCaseImpl;
import com.studies.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.studies.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCaseImpl;
import com.studies.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.studies.catalog.admin.application.castmember.update.UpdateCastMemberUseCaseImpl;
import com.studies.catalog.admin.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new ListCastMembersUseCaseImpl(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new GetCastMemberByIdUseCaseImpl(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new CreateCastMemberUseCaseImpl(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new UpdateCastMemberUseCaseImpl(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DeleteCastMemberUseCaseImpl(castMemberGateway);
    }

}