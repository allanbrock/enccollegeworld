package com.endicott.edu.models;

public class TutorialModel {
    private String title = "";
    private String body = "";
    private String page = "unknown";
    private int refNum = 0;

    public TutorialModel(){
    }

    public TutorialModel(String title, String body, String page, int refNum){
        this.title = title;
        this.body = body;
        this.page = page;
        this.refNum = refNum;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRefNum() {
        return refNum;
    }

    public void setRefNum(int refNum) {
        this.refNum = refNum;
    }
}
