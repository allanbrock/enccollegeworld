package com.endicott.edu.models;

//Class is an enum class that makes it so you can set gender easier in code (GenderModel.theGenderHere)
public final class GenderModel {
    public final String personGender;
    public static final GenderModel FEMALE = new GenderModel("FEMALE");
    public static final GenderModel MALE = new GenderModel("MALE");
    private GenderModel(String gender) {
        this.personGender = gender;
    }
}
