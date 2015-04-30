package com.jeshko.android.hereshko_john_project4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkFunctions {
    public static Boolean isConnected(Context context) {
        Boolean isConnected = false;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null)
        {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected())
            {
                isConnected = true;
            }
            else {
                isConnected = false;
            }
        }
        return isConnected;
    }


}
