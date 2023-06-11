package com.h071211059.h071211059_finalmobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkUtil {
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
