package com.h071211059.h071211059_finalmobile.db;

import android.provider.BaseColumns;

public class CastDBContract {
    public static String TABLE_NAME = "cast";

    public static final class CastColumns implements BaseColumns {
        public static String CONTENT_ID = "content_id";

        public static String NAME = "name";

        public static String PROFILE_PATH = "profile_path";

        public static String CHARACTER = "character";
    }
}
