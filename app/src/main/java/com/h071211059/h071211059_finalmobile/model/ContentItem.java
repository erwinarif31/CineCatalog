package com.h071211059.h071211059_finalmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ContentItem {
    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("genre_ids")
    private ArrayList<Integer> genreIds;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private String popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("vote_count")
    private String voteCount;

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public ArrayList<Integer> getGenreIds() {
        return genreIds;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
