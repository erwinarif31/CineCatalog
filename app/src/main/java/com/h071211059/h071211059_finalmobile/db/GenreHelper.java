package com.h071211059.h071211059_finalmobile.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenreHelper {
    private static final String DATABASE_TABLE = GenreDBContract.TABLE_NAME;

    private static DBHelper databaseHelper;

    private static SQLiteDatabase database;

    private static volatile GenreHelper INSTANCE;

    private GenreHelper(Context context) {
        databaseHelper = new DBHelper(context);
    }

    public static GenreHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GenreHelper(context);
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

    public long insert(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

}
