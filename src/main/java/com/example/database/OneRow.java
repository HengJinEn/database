package com.example.database;

import java.io.Serializable;

public class OneRow implements Serializable {
    public String index;
    public String type;
    public String value;

    public OneRow(String index, String type, String value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

