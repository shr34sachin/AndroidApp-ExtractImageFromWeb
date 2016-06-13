package com.sachin.sachinshrestha.extractimagefromweb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button btnLoad;
    ImageView ivImageURL;
    EditText inputTextURL;
    private String imageURL = "http://www.worldbank.org/content/dam/Worldbank/Flags/nepal_texture_1.gif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImageURL = (ImageView) findViewById(R.id.ivImageURL);
        inputTextURL =(EditText) findViewById(R.id.inputTextURL);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        inputTextURL.setText(imageURL);

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageURL = inputTextURL.getText().toString();
                if (imageURL.isEmpty()){
                    Toast.makeText(getApplicationContext(),"URL input text field empty", Toast.LENGTH_LONG).show();
                } else{
                    if (isOnline()){
                        requestData(imageURL);
                    } else{
                        Toast.makeText(getApplicationContext(),"Network isn't available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void displayImage(Bitmap img){
        ivImageURL.setImageBitmap(img);
    }

    // check whether the network is availed or not
    // Note that the permissions, ACCESS_NETWORK_STATE and INTERNET should be set first in manifest file
    private boolean isOnline(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        } else{
            return false;
        }
    }

    private void requestData(String imageURL) {
        asyncTasks tasks = new asyncTasks();
        tasks.execute(imageURL);
    }

    private class asyncTasks extends AsyncTask<String,String,Bitmap> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image = null;
            try{
                InputStream imageBlob = (InputStream) new URL(params[0]).getContent();
                image = BitmapFactory.decodeStream(imageBlob);
                imageBlob.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap imagePassedFromDoInBckGnd) {
            if(imagePassedFromDoInBckGnd == null){
                Toast.makeText(getApplicationContext(),"Invalid URL address.", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Note: Don't forget to use https://", Toast.LENGTH_LONG).show();
            } else{
                displayImage(imagePassedFromDoInBckGnd);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
