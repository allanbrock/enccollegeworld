package com.endicott.edu.models;

import java.io.Serializable;

/**
 * Created by abrocken on 7/10/2017.
 */
public class SportModel implements Serializable {
    private int minPlayers = 0;
    private String gender = "unknown";
    private int currentPlayers = 0;
    private int maxPlayers = 0;
    private int costPerDay = 0;
    private int hourLastUpdated = 0;
    private int reputation = 50;
    private int overallRep = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int gamesTied = 0;
    private int numGames = 0;
    private int startupCost = 0;
    private String runId = "unknown";
    private String sportName = "unknown";
    private String note = "no note";
    private boolean isActive = false;
    private int hoursUntilNextGame = 0;

    public SportModel() {

    }

    public SportModel(int minPlayers, int currentPlayers, int maxPlayers, int costPerDay, int gamesLost, int gamesTied, int gamesWon, int numGames, int startupCost, int reputation, int hourLastUpdated, String sportName, String runId, Boolean isActive, int hoursUntilNextGame, String gender) {
        this.minPlayers = minPlayers;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.costPerDay = costPerDay;
        this.hourLastUpdated = hourLastUpdated;
        this.reputation = reputation;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
        this.gamesTied = gamesTied;
        this.numGames = numGames;
        this.startupCost = startupCost;
        this.sportName = sportName;
        this.runId = runId;
        this.isActive = isActive;
        this.hoursUntilNextGame = hoursUntilNextGame;
        this.gender = gender;
    }

    public SportModel(String sportName, String runId){
        this.sportName = sportName;
        this.runId = runId;
    }

    public int getCapacity() {
        return minPlayers;
    }

    public void setCapacity(int capacity) {
        this.minPlayers = capacity;
    }

    public int getCostPerHour() {
        return costPerDay;
    }

    public void setCostPerHour(int costPerHour) {
        this.costPerDay = costPerHour;
    }

    public int getHourLastUpdated() {
        return hourLastUpdated;
    }

    public void setHourLastUpdated(int hourLastUpdated) {
        this.hourLastUpdated = hourLastUpdated;
    }

    public String getName() {
        return sportName;
    }

    public void setName(String name) {
        this.sportName = name;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getCostPerDay() {
        return costPerDay;
    }

    public void setCostPerDay(int costPerDay) {
        this.costPerDay = costPerDay;
    }

    public int getReputation() { return reputation; }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public int getGamesTied() {
        return gamesTied;
    }

    public void setGamesTied(int gamesTied) {
        this.gamesTied = gamesTied;
    }

    public int getNumGames() {
        return numGames;
    }

    public void setNumGames(int numGames) {
        this.numGames = numGames;
    }

    public int getStartupCost() {
        return startupCost;
    }

    public void setStartupCost(int startupCost) {
        this.startupCost = startupCost;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public int getHoursUntilNextGame() { return hoursUntilNextGame; }

    public void setHoursUntilNextGame(int hoursUntilNextGame) {
        this.hoursUntilNextGame = hoursUntilNextGame;
    }

    public int getCurrentPlayers() { return currentPlayers; }

    public void setCurrentPlayers(int currentPlayers) { this.currentPlayers = currentPlayers; }


    public boolean getActive() { return isActive;
    }

    public void setActive(boolean active) { isActive = active; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public int getOverallRep() { return overallRep; }

    public void setOverallRep(int overallRep) { this.overallRep = overallRep; }

}
