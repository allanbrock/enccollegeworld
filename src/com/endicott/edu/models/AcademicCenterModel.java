package com.endicott.edu.models;

public class AcademicCenterModel extends BuildingModel {
    //inherits from BuildingModel
    public AcademicCenterModel(String name, int numStudents, String size){
        super(name, numStudents, BuildingType.academic().getType(), size);
        this.getUpgrades().add(new Upgrade("Tables",0, 0));
        this.getUpgrades().add(new Upgrade("Air Conditioning", 0, 0));
        this.getUpgrades().add(new Upgrade("Vending Machines", 0, 0));
        this.getUpgrades().add(new Upgrade("Projectors", 0, 0));
        this.getUpgrades().add(new Upgrade("Seats", 0, 0));
    }

    public void upgradeAcademicCenter(Upgrade upgrade) {
        for(int i = 0; i < upgrade.getMaxLevel(); i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).increaseLevel();
        }
    }

    public void downgradeAcademicCenter(Upgrade upgrade) {
        for(int i = 0; i > 0; i++) {
            if(upgrade.getName()==getUpgrades().get(i).getName())
                getUpgrades().get(i).decreaseLevel();
        }
    }
}
