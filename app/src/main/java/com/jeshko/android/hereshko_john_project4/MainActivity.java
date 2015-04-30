package com.jeshko.android.hereshko_john_project4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.io.IOUtil;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String TAG = MainActivity.class.getSimpleName();

    }

    public void onClick(View v)
    {
        EditText searchField = (EditText) findViewById(R.id.editText);
        final String searchText = searchField.getText().toString();

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        if (NetworkFunctions.isConnected(MainActivity.this))
        {
            ThisTask thisTask = new ThisTask();
            if (searchText.equals(""))
            {
                thisTask.execute("http://api.openweathermap.org/data/2.5/weather?q=Winter_Park,fl&units=imperial");
            }
            else {

                thisTask.execute("http://api.openweathermap.org/data/2.5/weather?q="+ searchText+ "," + spinner.getSelectedItem() + "&units=imperial");

            }
        }
        else
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Network Error");
            alertDialog.setMessage("This device seems to be disconnected from the internet. Reconnect to a cellular network or Wi-Fi, and try again.");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private class ThisTask extends AsyncTask<String, Void, String>
    {
        final String TAG = MainActivity.class.getSimpleName();
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Loading");
            progressDialog.setIcon(R.mipmap.ic_launcher);
            progressDialog.setTitle("Network Check");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String results = "";

            try {
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                results = IOUtil.toString(inputStream);
                inputStream.close();
                Log.d(TAG, "Collected Info!");


            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG,"There was an error");
            }

            return results;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView cityView = (TextView) findViewById(R.id.textView);
            TextView lowTempView = (TextView) findViewById(R.id.textView2);
            TextView highTempView = (TextView) findViewById(R.id.textView3);
            TextView currentTempView = (TextView) findViewById(R.id.textView4);


            try {
                JSONObject json = new JSONObject(s);

                JSONArray conditionArray = json.getJSONArray("weather");
                JSONObject arrayObject = conditionArray.getJSONObject(0);
                String condition = arrayObject.getString("description");

                String city = json.getString("name");
                String tempBase = json.getJSONObject("main").getString("temp");
                String windBase = json.getJSONObject("wind").getString("speed");


                cityView.setText(city);
                lowTempView.setText(tempBase + (char) 0x00B0 + " current temperature");
                highTempView.setText(windBase + " m/s speed wind");
                currentTempView.setText(condition);
                progressDialog.setMessage("Loaded!");


            } catch (JSONException e) {
                e.printStackTrace();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Search Error");
                alertDialog.setMessage("There seems to have been an error searching that location. Check that your location is spelled correctly, and try again.");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            }



            progressDialog.cancel();
        }
    }

}



