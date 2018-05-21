package com.example.risha_000.samplepaintapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
    Context context;
    String result;

    private ProgressDialog pdia;

    // This is a constructor that allows you to pass in the JSON body
    public HttpPostAsyncTask(HttpClient client, HttpPost post, Context context) {
        this.client = client;
        this.post = post;
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        pdia = new ProgressDialog(context);
        pdia.setMessage("Fetching Results from Remote Model.....");
        pdia.show();
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
        pdia.dismiss();
        Toast.makeText(this.context,"It's a "+this.result,Toast.LENGTH_LONG).show();
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
            this.result = json_result.getString("prediction");
        } catch (Exception e) {
            Log.d("Error", e.getLocalizedMessage());
        }
        return null;
    }
}