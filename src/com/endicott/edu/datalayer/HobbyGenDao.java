package com.endicott.edu.datalayer;
import java.util.Random;

public class HobbyGenDao {
    private static final String[] hobbies = {"Watching Sports","Video Games","Coding","Movies","Reading","Calligraphy","Stamp Collecting",
            "Cooking","Drinking","Writing","Dancing","Singing","Acting","Painting","Kayaking","Camping","Fishing","Fly Fishing",
            "Hunting", "Swimming", "Tanning", "Exercising","Walks on the Beach", "Curling", "Shopping","Smoking","Gardening","Bobsledding",
            "Bird Watching", "Building Legos", "Origami", "Water Polo", "Parkour", "Podcasting", "Eating", "Astrology", "Photography",
            "Karate", "Judo", "Pankration", "Jellyfishing", "Coloring", "Gambling", "Rubik's Cube", "Driving", "Surfing", "Sport Stacking",
            "Stand up Comedy", "Golfing", "Pranks", "Magic", "Cleaning", "Filmmaking", "Baking", "Social Media", "Hacking", "Tax evasion",
            "Puzzles", "Ice Skating", "Anime", "Metal Detecting", "Herping", "Whale Watching", "Rock Climbing", "Marbles", "Beyblades",
            "Pokémon", "Club Penguin", "Sky Diving", "Car Enthusiast", "Strangling", "Watching Soap Operas", "Netflix", "Folding Laundry"};

    /**
     * This function returns a first and last name, then combines them
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
