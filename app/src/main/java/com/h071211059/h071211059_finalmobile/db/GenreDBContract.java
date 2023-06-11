package com.h071211059.h071211059_finalmobile.db;

import android.provider.BaseColumns;

public class GenreDBContract {
    public static String TABLE_NAME = "genres";

    public static final class GenreColumns implements BaseColumns {
        public static String GENRE_ID = "genre_id";
        public static String NAME = "name";
    }
}
