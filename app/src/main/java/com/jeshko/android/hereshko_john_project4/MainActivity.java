package com.jeshko.android.hereshko_john_project4;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null)
        {
            if (networkInfo.isConnected())
            {
                String urlString = "http://api.openweathermap.org/data/2.5/weather?q=Winter_Park,fl";
                try {
                    URL url = new URL(urlString);
                    if (url != null)
                    {
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();

                }
            }
        }
    }

}



