package com.example.risha_000.samplepaintapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    // PaintView Variable
    private PaintView paintView;

    // Permission related variables
    private final int REQUEST_PERMISSION_INTERNET=1;
    private final int REQUEST_PERMISSION_WRITE=1;
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    // Http Asynchronous Task
    HttpPostAsyncTask task;

    // Change this accordingly
    String API_URL = "http://ec2-34-217-190-195.us-west-2.compute.amazonaws.com/model/predict";


    // This function runs when the activity is created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);


    }


    // This function checks for all the permissions and triggers the request to the api only if all the permissions are granted
    public void verifyStoragePermissions(Activity activity) throws IOException,JSONException {
        // Check if we have write permission
        int permission_1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission_3 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

        if (permission_1 != PackageManager.PERMISSION_GRANTED || permission_2 != PackageManager.PERMISSION_GRANTED || permission_3 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        else{
            Bitmap mBitmap = paintView.save();
            send_request(mBitmap);
        }
    }

    // This function saves the canvas digit image to the phone and then transfers that file to the api server (our ec2 instance)
    // and retrieves the response back. It uses HttpPostAsyncTask to achieve this as it is required to do any IO operation on a
    // seperate thread rather than on the main UI thread.
    public void send_request(Bitmap mBitmap) throws IOException,JSONException{
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(API_URL);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

            Log.v("Step","1");

            File file = new File(Environment.getExternalStorageDirectory()+ "/test_image.png");
            file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bitmapdata = bos.toByteArray();
            Log.v("Step","1.2");

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            Log.v("Step","1");

            File _file = new File(Environment.getExternalStorageDirectory()+ "/test_image.png");

            if(_file != null)
            {
                Log.v("Step","1.5");
                FileBody body = new FileBody(_file);
                entityBuilder.addPart("test_image", body);
            }

            HttpEntity entity = entityBuilder.build();
            post.setEntity(entity);

            Log.v("Step","2");

            task = new HttpPostAsyncTask(client,post,this);
            task.execute();

        }
    }

    public void recognize(View view) throws FileNotFoundException, MalformedURLException, IOException,JSONException {
        verifyStoragePermissions(this);
    }

    public void clear(View view){
        paintView.clear();
    }

}