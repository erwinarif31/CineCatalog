package com.h071211059.h071211059_finalmobile.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInstance {
    public static Retrofit retrofit;

    public static String API_KEY = "cacfac2830d5e7060a8dc34c753b4791";

    public static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
