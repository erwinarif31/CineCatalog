package com.h071211059.h071211059_finalmobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ContentItem implements Parcelable {
    @SerializedName("id")
    private int id;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("genres")
    private ArrayList<Genre> genres;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("title")
    private String title;

    @SerializedName("name")
    private String name;

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

    public ContentItem(int id, String backdropPath, String firstAirDate, String releaseDate, String name, String title, String overview, String posterPath, String voteAverage) {
        this.id = id;
        this.backdropPath = backdropPath;
        this.firstAirDate = firstAirDate;
        this.releaseDate = releaseDate;
        this.name = name;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
    }

    protected ContentItem(Parcel in) {
        id = in.readInt();
        backdropPath = in.readString();
        firstAirDate = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        name = in.readString();
        overview = in.readString();
        popularity = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        voteCount = in.readString();
    }

    public static final Creator<ContentItem> CREATOR = new Creator<ContentItem>() {
        @Override
        public ContentItem createFromParcel(Parcel in) {
            return new ContentItem(in);
        }

        @Override
        public ContentItem[] newArray(int size) {
            return new ContentItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
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

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(backdropPath);
        parcel.writeString(firstAirDate);
        parcel.writeString(releaseDate);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeString(name);
        parcel.writeString(overview);
        parcel.writeString(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(voteAverage);
        parcel.writeString(voteCount);
    }
}
