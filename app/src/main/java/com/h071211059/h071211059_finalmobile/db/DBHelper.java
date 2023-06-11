package com.h071211059.h071211059_finalmobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "Cine.db";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_GENRE =
            String.format(
                    "CREATE TABLE %s"
                            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + " %s INTEGER NOT NULL,"
                            + " %s TEXT NOT NULL)",
                    GenreDBContract.TABLE_NAME,
                    GenreDBContract.GenreColumns._ID,
                    GenreDBContract.GenreColumns.GENRE_ID,
                    GenreDBContract.GenreColumns.NAME
            );

    private static final String SQL_DROP_TABLE_GENRE = "DROP TABLE IF EXISTS " + GenreDBContract.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_GENRE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GenreDBContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE_GENRE);
    }
}
