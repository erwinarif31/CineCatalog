package com.h071211059.h071211059_finalmobile.util;

import android.database.Cursor;

import com.h071211059.h071211059_finalmobile.db.ContentDBContract;
import com.h071211059.h071211059_finalmobile.db.ContentGenreContract;
import com.h071211059.h071211059_finalmobile.db.GenreDBContract;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.model.Genre;

import java.util.ArrayList;

public class MappingHelper {
    public static ArrayList<Integer> mapCursorToArrayIds(Cursor cursor) {
        ArrayList<Integer> idsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("content_id"));
            idsList.add(id);
        }
        return idsList;
    }

    public static ArrayList<ContentItem> mapCursorToArrayList(Cursor cursor) {
        ArrayList<ContentItem> contentList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.CONTENT_ID));
            String backdropPath = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.BACKDROP_PATH));
            String firstAirDate = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.FIRST_AIR_DATE));
            String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.RELEASE_DATE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.NAME));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.TITLE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.OVERVIEW));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.POSTER_PATH));
            String voteAverage = cursor.getString(cursor.getColumnIndexOrThrow(ContentDBContract.ContentColumns.VOTE_AVERAGE));

            contentList.add(new ContentItem(
                    id,
                    backdropPath,
                    firstAirDate,
                    releaseDate,
                    name,
                    title,
                    overview,
                    posterPath,
                    voteAverage
            ));
        }
        return contentList;
    }

    public static ArrayList<Genre> mapCursorToGenreArrayList(Cursor cursor) {
        ArrayList<Genre> genreList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(GenreDBContract.GenreColumns.GENRE_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(GenreDBContract.GenreColumns.NAME));
            genreList.add(new Genre(id, name));
        }
        return genreList;
    }
}
