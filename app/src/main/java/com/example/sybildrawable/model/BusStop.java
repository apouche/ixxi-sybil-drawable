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
    public BusPosition position;

    @SerializedName("mnemoPosition")
    public BusPosition mnemoPosition;
}
