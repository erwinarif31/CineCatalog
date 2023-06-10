package com.h071211059.h071211059_finalmobile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DataResponse {
    @SerializedName("results")
    private ArrayList<ContentItem> results;

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    public ArrayList<ContentItem> getResults() {
        return results;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
