package com.endicott.edu.models;

import java.io.Serializable;

public class PotentialStudentModel extends PersonModel implements Serializable {
    private String feedback = "";                   //The student's thoughts about the college (Based off of happiness)
    private String[] hobbies;

    private String nature = "unknown";              //Trait of the student (a general idea of what kind of person the student is)

    private QualityModel quality = null;            // the quality (stats) of the student -- how much the student contributes
    private PersonalityModel personality = null;    // the personality of the student -- how much the student needs to remain happy


    public PotentialStudentModel(String firstName, String lastName, GenderModel gender, int id, int happiness,
                                 PersonalityModel pm, QualityModel qm) {

        super(firstName,lastName, gender,id, happiness);
        setQuality(qm);
        setPersonality(pm);
    }
    public void setHobbies(String[] hobbies){this.hobbies = hobbies;}
    public String[] getHobbies(){return this.hobbies;}
    public void setPersonality(PersonalityModel pm){ this.personality = pm; }
    public PersonalityModel getPersonality() { return this.personality; }
    public void setQuality(QualityModel qm){ this.quality = qm; }
    public QualityModel getQuality() { return this.quality; }
    public String getNature() { return nature; }
    public void setNature(String nature) { this.nature = nature; }
}
