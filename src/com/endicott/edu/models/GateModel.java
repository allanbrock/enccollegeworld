package com.endicott.edu.models;

import java.io.Serializable;

public class GateModel implements Serializable {
    private String  key;
    private String  description;
    private String  iconPath;
    private int     level;
    private String  runId = "unknown";

    public GateModel(String _key, String _description, String _iconPath, int _level){
            this.key            = _key;
            this.description    = _description;
            this.iconPath       = _iconPath;
            this.level          = _level;
    };

    public String getKey() { return this.key; }

    public String getDescription() { return this.description; }

    public String getIconPath() { return this.iconPath; }

    public int getLevel() { return this.level; }

    public void setRunId(String _runId) { this.runId = _runId; }
}
