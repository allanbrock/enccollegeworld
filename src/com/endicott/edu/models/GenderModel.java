package com.endicott.edu.models;

public final class GenderModel {
    public final String gender;
    public static final GenderModel FEMALE = new GenderModel("FEMALE");
    public static final GenderModel MALE = new GenderModel("MALE");
    private GenderModel(String gender) {
        this.gender = gender;
    }
}
