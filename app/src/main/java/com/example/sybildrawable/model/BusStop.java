package com.example.sybildrawable.model;

import com.google.gson.annotations.SerializedName;

public class BusStop {
    @SerializedName("boxType")
    public String type;

    @SerializedName("typeBoite")
    public int boxIndex;

    @SerializedName("mnemo")
    public String mnemo;

    @SerializedName("ptPosition")
    public Vector2f position;

    @SerializedName("mnemoPosition")
    public Vector2f mnemoPosition;

    public boolean isStop() {
        return type.equals("ARRET");
    }
}
