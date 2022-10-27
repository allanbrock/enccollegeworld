package com.endicott.edu.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Superclass that every type of person in the college inherits from.
 * Created by Ryan Kelley, Marissa Patti, and Giana Nekitopoulos on 9/28/20.
 */
public abstract class Person implements Serializable {

    protected int id;
    protected String firstName;
    protected String lastName;
    protected String fullName;
    protected String avatarCode;
    protected Gender gender;
    protected int happiness;

    // Default constructor for making empty people of any type (it is assumed you will set these right after creation!)
    protected Person() {}

    // Constructor to create a Person, can only be done while constructing a more specific type of person
    protected Person(String firstName, String lastName, Gender gender, int id, int happiness) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.gender = gender;
        this.id = id;
        this.happiness = happiness;
        generateNewAvatar();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName, String lastName) {
        this.fullName = firstName + " " + lastName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public String getAvatarCode() {
        return avatarCode;
    }

    public void setAvatarCode(String avatarCode) {
        this.avatarCode = avatarCode;
    }

    public void generateNewAvatar() {
        String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Map<String, Integer> attributeToNumStylesMap = new HashMap<String, Integer>() {{
            put("accessory", 7);
            put("clothesColor", 15);
            put("clothesType", 9);
            put("eyeType", 12);
            put("eyebrowType", 12);
            put("facialHairColor", 8);
            put("facialHairType", 6);
            put("graphicType", 11);
            put("hairColor", 11);
            put("mouthType", 12);
            put("skinColor", 7);
            put("topType", 35);
        }};
        StringBuilder code = new StringBuilder();
        for (int numStyles : attributeToNumStylesMap.values()) {
            code.append(alphanumeric.charAt((int) (Math.random() * numStyles)));
        }
        avatarCode = code.toString();
    }

    // Sets the eyes (index 3), eyebrows (index 4), and mouth (index 9) to a "happy" style.
    public void makeAvatarHappy() {
        if (avatarCode == null) {
            generateNewAvatar();
        }
        Map<Integer, String> attributeIndexToHappyStyleCharsMap = new HashMap<Integer, String>() {{
            put(3, "CFGIKL");
            put(4, "CDFGKL");
            put(9, "BIJK");
        }};
        StringBuilder code = new StringBuilder(avatarCode);
        for (Map.Entry<Integer, String> indexToChars : attributeIndexToHappyStyleCharsMap.entrySet()) {
            String newHappyStyle = String.valueOf(indexToChars.getValue().charAt((int) (Math.random() * indexToChars.getValue().length())));
            code.replace(indexToChars.getKey(), indexToChars.getKey() + 1, newHappyStyle);
        }
        avatarCode = code.toString();
    }

    // Sets the eyes (index 3), eyebrows (index 4), and mouth (index 9) to an "unhappy" style.
    public void makeAvatarUnhappy() {
        if (avatarCode == null) {
            generateNewAvatar();
        }
        Map<Integer, String> attributeIndexToUnhappyStyleCharsMap = new HashMap<Integer, String>() {{
            put(3, "BDEH");
            put(4, "ABHI");
            put(9, "ACFGHL");
        }};
        StringBuilder code = new StringBuilder(avatarCode);
        for (Map.Entry<Integer, String> indexToChars : attributeIndexToUnhappyStyleCharsMap.entrySet()) {
            String newUnhappyStyle = String.valueOf(indexToChars.getValue().charAt((int) (Math.random() * indexToChars.getValue().length())));
            code.replace(indexToChars.getKey(), indexToChars.getKey() + 1, newUnhappyStyle);
        }
        avatarCode = code.toString();
    }
}
