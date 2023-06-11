package com.h071211059.h071211059_finalmobile.model;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    @SerializedName("character")
    private String character;

    public Cast(int id, String name, String character, String profilePath) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.profilePath = profilePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getCharacter() {
        return character;
    }
}
