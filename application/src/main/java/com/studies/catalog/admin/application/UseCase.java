package com.studies.catalog.admin.application;

public abstract class UseCase<In, Out> {

    public abstract Out execute(In anIn);

}