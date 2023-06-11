package com.h071211059.h071211059_finalmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GenreResponse {
    @SerializedName("genres")
    private ArrayList<Genre> genres;

    public ArrayList<Genre> getGenres() {
        return genres;
    }
}
