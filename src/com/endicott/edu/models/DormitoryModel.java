//package com.endicott.edu.models;
//
//
//import java.io.Serializable;
//
//public class DormitoryModel extends BuildingModel implements Serializable{
//    private int capacity = 0;
//    private String size;
//    private int costPerDay = 0;
//    private int hourLastUpdated = 0;
//    private String name = "unknown";
//    private String runId = "unknown";
//    private String note = "no note";
//    private int numStudents = 0;
//    private String curDisaster = "";
//    private int reputation = 0;
//    private int numRooms = 0;
//    private int lengthOfDisaster = 0;
//    private int hoursToComplete = 300;
//    private int totalBuildCost = 0;
//
//    private int dormType;
//
//    public DormitoryModel() {
//    }
//
////    public DormitoryModel(String size, int numStudents, int reputation, int costPerDay, int hourLastUpdated, String name, String runId) {
////        this.size = size;
////        this.numStudents = numStudents;
////        this.reputation = reputation;
////        this.capacity = BuildingModel.setCapacityBasedOnSize(size);
////        this.costPerDay = costPerDay;
////        this.hourLastUpdated = hourLastUpdated;
////        this.name = name;
////        this.runId = runId;
////    }
//    public DormitoryModel(String name, String size, int numStudents) {
//        super(name, numStudents, BuildingModel.getDormConst(), size);
//    }
//
//    public DormitoryModel(int capacity, int hourLastUpdated, String name, int numStudents,
//                          String curDisaster, int reputation, String runId, int numRooms){
//        this.capacity=capacity;
//        this.hourLastUpdated=hourLastUpdated;
//        this.name=name;
//        this.numStudents=numStudents;
//        this.curDisaster=curDisaster;
//        this.reputation=reputation;
//        this.runId=runId;
//        this.numRooms=numRooms;
//    }
//
//    public int getDormType() {
//        return dormType;
//    }
//
//    public void setDormType(int dormType) {
//        this.dormType = dormType;
//    }
//
//    public int getCapacity() {
//        return capacity;
//    }
//
//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
//
//    public int getCostPerDay() {
//        return costPerDay;
//    }
//
//    public int getHourLastUpdated() {
//        return hourLastUpdated;
//    }
//
//    public void setHourLastUpdated(int hourLastUpdated) {
//        this.hourLastUpdated = hourLastUpdated;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getRunId() {
//        return runId;
//    }
//
//    public void setRunId(String runId) {
//        this.runId = runId;
//    }
//
//    public String getNote() {
//        return note;
//    }
//
//    public void setNote(String note) {
//        this.note = note;
//    }
//
//    public int getMaintenanceCostPerDay() {
//        return costPerDay;
//    }
//    public void setMaintenanceCostPerDay(int numRooms){
//        this.costPerDay = (((numRooms * 150))/(365*24));
//
//    }
//
//    public void setCostPerDay(int costPerDay) {
//        this.costPerDay = costPerDay;
//    }
//
//    public void setHoursToComplete(int hoursToComplete) {
//        this.hoursToComplete = hoursToComplete;
//    }
//
//    public int getHoursToComplete() {
//        return this.hoursToComplete;
//    }
//    public void setTotalBuildCost(int numRooms){
//        this.totalBuildCost = numRooms * 1000;
//    }
//    public int getTotalBuildCost(){
//        return this.totalBuildCost;
//    }
//
//    public int getNumStudents() {
//        return numStudents;
//    }
//
//    public void setNumStudents(int numStudents) {
//        this.numStudents = numStudents;
//    }
//
//    public String getCurDisaster() {
//        return curDisaster;
//    }
//
//    public void setCurDisaster(String curDisaster) {
//        this.curDisaster = curDisaster;
//    }
//
//    public int getReputation() {
//        return reputation;
//    }
//
//    public void setReputation(int reputation) {
//        this.reputation = reputation;
//    }
//
//    public int getNumRooms() {
//        return numRooms;
//    }
//
//    public void setNumRooms(int numRooms) {
//        this.numRooms = numRooms;
//    }
//
//    public int getLengthOfDisaster() {
//        return lengthOfDisaster;
//    }
//
//    public void setLengthOfDisaster(int lengthOfDisaster) {
//        this.lengthOfDisaster = lengthOfDisaster;
//    }
//
//    public void incrementNumStudents(int increment){
//        this.numStudents += increment;
//    }
//    public String checkIfBeingBuilt(){
//        if(this.getHoursToComplete() > 0){
//            return Integer.toString(this.getHoursToComplete()) + " hours remaining";
//        }
//        else
//            return "Built";
//    }
//
//}