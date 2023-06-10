package com.h071211059.h071211059_finalmobile.network;

import com.h071211059.h071211059_finalmobile.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<DataResponse> getMoviePopular(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("movie/now_playing")
    Call<DataResponse> getMovieNowPlaying(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("movie/top_rated")
    Call<DataResponse> getMovieTopRated(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("movie/upcoming")
    Call<DataResponse> getMovieUpcoming(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

}
