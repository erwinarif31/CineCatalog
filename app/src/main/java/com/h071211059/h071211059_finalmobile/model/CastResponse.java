package com.h071211059.h071211059_finalmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CastResponse {
    @SerializedName("cast")
    private ArrayList<Cast> casts;

    public ArrayList<Cast> getCasts() {
        return casts;
    }
}
