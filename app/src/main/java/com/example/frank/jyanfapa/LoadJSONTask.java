package com.example.frank.jyanfapa;

import android.os.AsyncTask;
import android.util.Log;

import com.example.frank.jyanfapa.callbacks.GetProductResponse;
import com.example.frank.jyanfapa.entities.Product;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Frank on 2018/1/29.
 */

public class LoadJSONTask extends AsyncTask<String, Void, GetProductResponse> {

    public LoadJSONTask(Listener listener) {

        mListener = listener;
    }

    public interface Listener {

        void onLoaded(List<Product> androidList);

        void onError();
    }

    private Listener mListener;

    @Override
    protected GetProductResponse doInBackground(String... strings) {
        try {

            //test
            String stringResponse = loadJSON(strings[0]);
            Gson gson = new Gson();
            Log.d("Json",stringResponse);
            return gson.fromJson(stringResponse,  GetProductResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute( GetProductResponse response) {

        if (response != null) {

            mListener.onLoaded(response.getProduct());

        } else {

            mListener.onError();
        }
    }

    private String loadJSON(String jsonURL) throws IOException {

        URL url = new URL(jsonURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null) {

            response.append(line);
        }

        in.close();

        return response.toString();
    }
}