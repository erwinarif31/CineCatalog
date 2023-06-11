package com.h071211059.h071211059_finalmobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class DataHelper {
    private static final String GENRE_TABLE = GenreDBContract.TABLE_NAME;

    private static final String CONTENT_TABLE = ContentDBContract.TABLE_NAME;

    private static final String CONTENT_GENRE_TABLE = ContentGenreContract.TABLE_NAME;

    private static DBHelper databaseHelper;

    private static SQLiteDatabase database;

    private static volatile DataHelper INSTANCE;

    public DataHelper(Context context) {
        databaseHelper = new DBHelper(context);
    }

    public static DataHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DataHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) {
            database.close();
        }
    }

    public long insertGenre(ContentValues values) {
        return database.insert(GENRE_TABLE, null, values);
    }

    public long insertContent(ContentValues values) {
        return database.insert(CONTENT_TABLE, null, values);
    }

    public long deleteContent(String id) {
        return database.delete(CONTENT_TABLE, ContentDBContract.ContentColumns.CONTENT_ID + " = '" + id + "'", null);
    }

    public Cursor queryAllFavoritesId() {
        return database.query(
                CONTENT_TABLE,
                new String[]{ContentDBContract.ContentColumns.CONTENT_ID},
                null,
                null,
                null,
                null,
                null
        );
    }

    public Cursor queryAllFavoritesContent() {
        return database.query(
                CONTENT_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public long insertContentGenre(ContentValues values) {
        return database.insert(CONTENT_GENRE_TABLE, null, values);
    }

    public long deleteContentGenre(String id) {
        return database.delete(CONTENT_GENRE_TABLE, ContentGenreContract.ContentGenreColumns.CONTENT_ID + " = '" + id + "'", null);
    }

    public Cursor queryGenreOfContent(String id) {
        return database.rawQuery("SELECT " +
                GENRE_TABLE + "." + GenreDBContract.GenreColumns.GENRE_ID + ", " +
                GENRE_TABLE + "." + GenreDBContract.GenreColumns.NAME +
                " FROM " + CONTENT_GENRE_TABLE +
                " JOIN " + GENRE_TABLE + " ON " +
                CONTENT_GENRE_TABLE + "." + ContentGenreContract.ContentGenreColumns.GENRE_ID + " = " +
                GENRE_TABLE + "." + GenreDBContract.GenreColumns.GENRE_ID +
                " WHERE " + CONTENT_GENRE_TABLE + "." + ContentGenreContract.ContentGenreColumns.CONTENT_ID + " = '" + id + "'", null);
    }

    public long insertCast(ContentValues values) {
        return database.insert(CastDBContract.TABLE_NAME, null, values);
    }

    public long deleteCast(String id) {
        return database.delete(CastDBContract.TABLE_NAME, CastDBContract.CastColumns.CONTENT_ID + " = '" + id + "'", null);
    }

    public Cursor queryCastOf(String id) {
        return database.query(
                CastDBContract.TABLE_NAME,
                null,
                CastDBContract.CastColumns.CONTENT_ID + " = '" + id + "'",
                null,
                null,
                null,
                null
        );
    }

    public void deleteDuplicateData() {
        database.execSQL("DELETE FROM " + GENRE_TABLE + " WHERE " + GenreDBContract.GenreColumns._ID + " NOT IN (SELECT MIN(" + GenreDBContract.GenreColumns._ID + ") FROM " + GENRE_TABLE + " GROUP BY " + GenreDBContract.GenreColumns.GENRE_ID + ")");
    }
}
