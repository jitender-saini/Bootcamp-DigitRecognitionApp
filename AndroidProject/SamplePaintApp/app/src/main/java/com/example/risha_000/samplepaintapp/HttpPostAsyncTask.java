package com.example.risha_000.samplepaintapp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by risha_000 on 18-May-18.
 */

public class HttpPostAsyncTask extends AsyncTask<String, Void, Void> {
    // This is the JSON body of the post
    HttpClient client;
    HttpPost post;
    // This is a constructor that allows you to pass in the JSON body
    public HttpPostAsyncTask(HttpClient client, HttpPost post) {
        this.client = client;
        this.post = post;
    }

    // This is a function that we are overriding from AsyncTask. It takes Strings as parameters because that is what we defined for the parameters of our async task
    @Override
    protected Void doInBackground(String... str) {

        try {
            Log.v("Step","3");
            HttpResponse response = this.client.execute(this.post);
            HttpEntity httpEntity = response.getEntity();
            String result = EntityUtils.toString(httpEntity);
            JSONObject json_result = new JSONObject(result);
            Log.v("Step","It's a "+json_result.getString("prediction"));
            //Toast.makeText(getApplicationContext(),"It's a "+json_result.getString("prediction"),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.d("Error", e.getLocalizedMessage());
        }
        return null;
    }
}