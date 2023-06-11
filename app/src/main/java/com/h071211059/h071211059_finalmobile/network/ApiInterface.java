package com.h071211059.h071211059_finalmobile.network;

import com.h071211059.h071211059_finalmobile.model.CastResponse;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
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

    @GET("tv/on_the_air")
    Call<DataResponse> getTvOnTheAir(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("tv/popular")
    Call<DataResponse> getTvPopular(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("tv/airing_today")
    Call<DataResponse> getTvAiringToday(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("tv/top_rated")
    Call<DataResponse> getTvTopRated(
            @Query("page") int page,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}")
    Call<ContentItem> getMovieDetail(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/credits")
    Call<CastResponse> getMovieCast(
            @Path("movie_id") int movie_id,
            @Query("api_key") String apiKey
    );

    @GET("tv/{series_id}")
    Call<ContentItem> getTVDetail(
            @Path("series_id") int series_id,
            @Query("api_key") String apiKey
    );

    @GET("tv/{series_id}/credits")
    Call<CastResponse> getTVCasts(
            @Path("series_id") int series_id,
            @Query("api_key") String apiKey
    );
}
