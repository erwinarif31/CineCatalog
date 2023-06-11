package com.h071211059.h071211059_finalmobile.db;

import android.provider.BaseColumns;

public class ContentDBContract{
    public static String TABLE_NAME = "contents";

    public static final class ContentColumns implements BaseColumns {
        public static String CONTENT_ID = "content_id";

        public static String BACKDROP_PATH = "backdrop_path";

        public static String FIRST_AIR_DATE = "first_air_date";

        public static String RELEASE_DATE = "release_date";

        public static String NAME = "name";

        public static String TITLE = "title";

        public static String OVERVIEW = "overview";

        public static String POSTER_PATH = "poster_path";

        public static String VOTE_AVERAGE = "vote_average";
    }
}
