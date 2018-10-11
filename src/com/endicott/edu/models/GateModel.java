package com.endicott.edu.models;

import java.io.Serializable;

public class GateModel implements Serializable {
    private String  key;
    private String  description;
    private int     goal;
    private String  runId = "unknown";

    public GateModel(String _key, String _description, int _gate){
            this.key            = _key;
            this.description    = _description;
            this.goal           = _gate;
    };

    public String getKey() { return this.key; }

    public String getDescription() { return this.description; }

    public int getGoal() { return this.goal; }

    public void setRunId(String _runId) { this.runId = _runId; }
}
