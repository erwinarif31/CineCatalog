package com.h071211059.h071211059_finalmobile.db;

import android.provider.BaseColumns;

public class ContentGenreContract {
    public static String TABLE_NAME = "content_genre";

    public static final class ContentGenreColumns implements BaseColumns {
        public static String CONTENT_ID = "content_id";
        public static String GENRE_ID = "genre_id";
    }
}
