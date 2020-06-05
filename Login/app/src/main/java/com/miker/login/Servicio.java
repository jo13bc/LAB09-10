package com.miker.login;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Servicio {

    public static String run(String apiUrl) throws Exception {
        String current = "";
        try {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(apiUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader isw = new InputStreamReader(in);
                int data = isw.read();
                while (data != -1) {
                    current += (char) data;
                    data = isw.read();
                    System.out.print(current);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

        } catch (Exception e) {
            throw e;
        }
        // return the data to onPostExecute method
        return current;
    }
}
