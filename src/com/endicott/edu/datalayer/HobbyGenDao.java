package com.endicott.edu.datalayer;
import java.util.Random;
/**
 * Implemented 11-18-2020 by Matt Cruz
 * mcruz772@mail.endicott.edu
 */
public class HobbyGenDao {
    private static final String[] hobbies = {"Watching Sports","Video Games","Coding","Movies","Reading","Calligraphy","Stamp Collecting",
            "Cooking","Drinking","Writing","Dancing","Singing","Acting","Painting","Kayaking","Camping","Fishing","Fly Fishing",
            "Hunting", "Swimming", "Tanning", "Exercising","Walks on the Beach", "Curling", "Shopping","Smoking","Gardening","Bobsledding",
            "Bird Watching", "Building Legos", "Origami", "Water Polo", "Parkour", "Podcasting", "Eating", "Astrology", "Photography",
            "Karate", "Judo", "Pankration", "Coloring", "Gambling", "Driving", "Surfing", "Sport Stacking",
            "Stand up Comedy", "Golfing", "Pranks", "Magic", "Cleaning", "Film Making", "Baking", "Social Media", "Hacking",
            "Puzzles", "Ice Skating", "Anime", "Metal Detecting", "Whale Watching", "Rock Climbing", "Marbles",
            "Club Penguin", "Sky Diving", "Car Enthusiast", "Watching Soap Operas", "Netflix", "Folding Laundry",
            "Bowling", "Clock Collecting", "Jump Roping", "Knitting", "Yoga", "Traveling", "Music", "Vibing", "Gymnastics", "Skateboarding",
            "Star Gazing", "Jewelry Making", "Poetry", "Farming", "Coffee Making", "Street Racing", "Board Games", "Tie Dyeing"};

    /**
     * This function returns an array of three hobbies,
     * @return a first and last name concat.. with a string.
     */
    public static String[] generateHobbies(){
        String[] temp = new String[3];
        for(int i = 0; i < 3; i++){
            Random r = new Random();
            temp[i] = hobbies[r.nextInt(hobbies.length)];
        }
        while(temp[0]==temp[1] || temp[0] == temp[2] || temp[1] == temp[2]){
            if(temp[0]==temp[1]){
                Random r = new Random();
                temp[0] = hobbies[r.nextInt(hobbies.length)];
            }
            if(temp[1] == temp[2]){
                Random r = new Random();
                temp[1] = hobbies[r.nextInt(hobbies.length)];
            }
            if(temp[0]==temp[2]){
                Random r = new Random();
                temp[2] = hobbies[r.nextInt(hobbies.length)];
            }
        }
        return temp;
    }
}
