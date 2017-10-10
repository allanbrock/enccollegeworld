package com.endicott.edu.models.models;

import java.io.Serializable;

/**
 * Created by nScrivani on 10/2/2017.
 */
public class SportModel implements Serializable {
    private int minPlayers = 0;
    private int maxPlayers = 0;
    private int costPerDay = 0;
    private int hourLastUpdated = 0;
    private int reputation = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private int gamesTied = 0;
    private int numGames = 0;
    private int startupCost = 0;
    private String runId = "unknown";
    private String sportName = "unknown";
    private String note = "no note";

    public SportModel() {
    }

    public SportModel(int minPlayers, int maxPlayers, int costPerDay, int gamesLost, int gamesTied, int gamesWon, int numGames, int startupCost, int reputation, int hourLastUpdated, String sportName, String runId) {
        this.minPlayers = minPlayers;
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
}
