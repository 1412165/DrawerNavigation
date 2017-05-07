package com.nthieuitus.findplaces.GoogleMapWebService;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public class DownloadRawData extends AsyncTask<String,Void, String> {
    final String TAG_LOG = DownloadRawData.class.getSimpleName();
    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader = null;
        Log.d(TAG_LOG, params[0]);

        try {
            URL url = new URL(params[0]);
            InputStream is = url.openConnection().getInputStream();
            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            //Log.d(TAG_LOG, buffer.toString());
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
