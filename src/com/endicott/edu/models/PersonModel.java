package com.endicott.edu.models;

/**
 * Created by Ryan Kelley, Marissa Patti, and Giana Nekitopoulos on 9/28/20
 */

//Class is the Superclass for all people in the game
//Implement this class in any class where your defining a new type of person in the college
public abstract class PersonModel {
    protected String name = "";                //The full name of the person
    protected String firstName = "";           //The first name of the person
    protected String lastName = "";            //The last name of the person
    protected GenderModel gender;         //The gender of the person (male/female)
    protected int id = 0;                     //ID of the person
    protected int happiness = 0;              //The overall happiness of the person
    protected AvatarModel avatarIcon;     //The image icon of a person (https) (store all the attributes not just a string)

    //Default constructor for making empty people of any type (it is assumed you will set these right after creation!)
    protected PersonModel() {

    }

    //Constructor to create a PersonModel, can only be done while constructing a more specific type of person
    protected PersonModel(String firstName, String lastName, GenderModel gender, int id, int happiness) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.id = id;
        this.happiness = happiness;
        name = firstName + " " + lastName;
    }

    public void setFirstName(String s) {this.firstName = s; }
    public String getFirstName() {return this.firstName;}

    public void setLastName(String s) {this.lastName = s; }
    public String getLastName() {return this.lastName;}

    public String getName() {return name;}
    public void setName(String firstName, String lastName) {this.name = firstName + " " + lastName;}
    public void setName(String fullName) {this.name = fullName;}

    public void setGender(GenderModel gender) { this.gender = gender; }
    public String getGender() { //We don't want a GenderModel, instead just a string of the gender
        if(this.gender == GenderModel.FEMALE) {
            return "Female";
        }
        else {
            return "Male";
        }
    }

    public void setId(int i) {this.id = i;}
    public int getId() {return this.id;}

    public void setHappiness(int i) {this.happiness = i;}
    public int getHappiness() {return this.happiness;}

    public void setAvatarIcon(AvatarModel am) {this.avatarIcon = am;}
    public AvatarModel getAvatarIcon() {return this.avatarIcon; }
}
