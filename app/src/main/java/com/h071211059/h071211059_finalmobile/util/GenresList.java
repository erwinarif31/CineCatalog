package com.h071211059.h071211059_finalmobile.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.h071211059.h071211059_finalmobile.db.GenreDBContract;
import com.h071211059.h071211059_finalmobile.db.DataHelper;
import com.h071211059.h071211059_finalmobile.model.Genre;
import com.h071211059.h071211059_finalmobile.model.GenreResponse;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;
import com.h071211059.h071211059_finalmobile.network.ApiInterface;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GenresList {
    public static void getGenres(Context applicationContext) {
        Retrofit retrofit = ApiInstance.getInstance();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<GenreResponse> tvGenres = apiInterface.getTVGenres(ApiInstance.API_KEY);
        Call<GenreResponse> movieGenres = apiInterface.getMovieGenres(ApiInstance.API_KEY);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(() -> {
            tvGenres.enqueue(new Callback<GenreResponse>() {
                @Override
                public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                    GenreResponse genreResponse = response.body();
                    ArrayList<Genre> genres = genreResponse.getGenres();
                    insertToDB(genres, applicationContext);
                }

                @Override
                public void onFailure(Call<GenreResponse> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        });

        executorService.execute(() -> {
            movieGenres.enqueue(new Callback<GenreResponse>() {
                @Override
                public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                    GenreResponse genreResponse = response.body();
                    ArrayList<Genre> genres = genreResponse.getGenres();
                    insertToDB(genres, applicationContext);
                }

                @Override
                public void onFailure(Call<GenreResponse> call, Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
        });
    }

    private static void insertToDB(ArrayList<Genre> genres, Context applicationContext) {
        DataHelper dataHelper = DataHelper.getInstance(applicationContext);
        dataHelper.open();

        for (Genre genre : genres) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(GenreDBContract.GenreColumns.GENRE_ID, genre.getId());
            contentValues.put(GenreDBContract.GenreColumns.NAME, genre.getName());

            dataHelper.insertGenre(contentValues);
        }

        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstRun", false);
        editor.apply();

        dataHelper.deleteDuplicateData();
    }
}
