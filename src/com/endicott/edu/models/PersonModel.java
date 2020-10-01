package com.endicott.edu.models;

//Authors
//Ryan Kelley, Marissa Patti, Giana Nekitopoulos

//Method is the Superclass for all people in the game (students, faculty, eventually coaches?)
//Implement this class in any class where your defining a new type of person in the college
public abstract class PersonModel {
    protected String fullName;
    protected String firstName;           //The first name of the person
    protected String lastName;            //The last name of the person
    protected GenderModel gender;         //The gender of the person (male/female)
    protected int id;                     //ID of the person
    protected int happiness;              //The overall happiness of the person
    protected AvatarModel avatarIcon;     //The image icon of a person (https) (store all the attributes not just a string)

    protected PersonModel() {

    }

    protected PersonModel(String firstName, String lastName, GenderModel gender, int id, int happiness) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.id = id;
        this.happiness = happiness;
        fullName = firstName + " " + lastName;
    }

    public void setFirstName(String s) {this.firstName = s; }
    public String getFirstName() {return this.firstName;}

    public void setLastName(String s) {this.lastName = s; }
    public String getLastName() {return this.lastName;}

    public String getFullName() {return fullName;}
    public void setFullName(String firstName, String lastName) {this.fullName = firstName + " " + lastName;}
    public void setFullName(String fullName) {this.fullName = fullName;}

    public void setGender(GenderModel gender) { this.gender = gender; }
    public GenderModel getGender() {return gender;}

    public void setId(int i) {this.id = i;}
    public int getId() {return this.id;}

    public void setHappiness(int i) {this.happiness = i;}
    public int getHappiness() {return this.happiness;}

    public void setAvatarIcon(AvatarModel am) {this.avatarIcon = am;}
    public AvatarModel getAvatarIcon() {return this.avatarIcon; }
}
