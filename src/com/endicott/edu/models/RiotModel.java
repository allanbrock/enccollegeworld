package com.endicott.edu.models;

//Ryan Gallagher
public class RiotModel {
    private int riotCost = 0;
    private String runId = "unknown";
    private String description;

    public RiotModel(SportModel sport) {
        if (sport.getDivision() == 3) {
            this.setRiotCost(2500);
        } else if (sport.getDivision() == 2) {
            this.setRiotCost(7500);
        } else if (sport.getDivision() == 1) {
            this.setRiotCost(15000);
        }

        if (sport.getSportName().equals("$50,000 - Men's Basketball")) {
            this.setDescription("Your Men's Basketball team has won a conference championship! A riot has broke out on campus, it will cost you $" + this.getRiotCost());
        }
        else if (sport.getSportName().equals("$50,000 - Women's Basketball")){
            this.setDescription("Your Women's Basketball team has won a conference championship! A riot has broke out on campus, it will cost you $" + this.getRiotCost());
        }
        else if (sport.getSportName().equals("$50,000 - Men's Soccer")){
            this.setDescription("Your Men's Soccer team has won a conference championship! A riot has broke out on campus, it will cost you $" + this.getRiotCost());
        }
        else if (sport.getSportName().equals("$50,000 - Women's Soccer")){
            this.setDescription("Your Women's Soccer team has won a conference championship! A riot has broke out on campus, it will cost you $" + this.getRiotCost());
        }
        else if (sport.getSportName().equals("$50,000 - Men's Football")){
            this.setDescription("Your Football team has won a conference championship! A riot has broke out on campus, it will cost you $" + this.getRiotCost());
        }
        else if (sport.getSportName().equals("$50,000 - Baseball")){
            this.setDescription("Your Baseball team has won a conference championship! A riot has broke out on campus, it will cost you $" + this.getRiotCost());
        }
        else if (sport.getSportName().equals("$50,000 - Softball")){
            this.setDescription("Your Softball team has won a conference championship! A riot has broke out on campus, it will cost you $" + this.getRiotCost());
        }





    }

    public void setDescription(String description) {this.description = description;}
    public String getDescription() {return description; }

    public void setRiotCost(int cost) {this.riotCost = cost;}
    public int getRiotCost() {return this.riotCost;}
}
