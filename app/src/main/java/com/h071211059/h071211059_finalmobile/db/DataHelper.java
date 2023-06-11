package com.h071211059.h071211059_finalmobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper {
    private static final String GENRE_TABLE = GenreDBContract.TABLE_NAME;

    private static final String CONTENT_TABLE = ContentDBContract.TABLE_NAME;

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

}
