package com.h071211059.h071211059_finalmobile.util;

import android.database.Cursor;

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
}
