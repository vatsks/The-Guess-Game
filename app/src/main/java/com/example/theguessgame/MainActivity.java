package com.example.theguessgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String > celeburl=new ArrayList<String>();
    ArrayList<String>names=new ArrayList<String>();int chosen;
    public class ImageDownloader extends AsyncTask<String,Void, Bitmap>{


        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream=urlConnection.getInputStream();
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }catch (Exception e){
                e.printStackTrace();return null;
            }
        }
    }
    public class DownloaTask extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection urlConnection=null;
            try {
                url =new URL(urls[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while (data!=-1){
                    char current= (char) data;
                    result+= current;
                    data =reader.read();
                }return result;
            }catch(Exception e) {
                e.printStackTrace();
                return null;

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloaTask task=new DownloaTask();
        String result=null;
        try {
            result=task.execute("https://commons.wikimedia.org/wiki/Category:Programming_language_logos").get();
            String[] splitResult =result.split("<form action=");
            Pattern p=Pattern.compile("png\">(.*?)</a>");
            Matcher m =p.matcher(splitResult[0]);
            while(m.find()){
                celeburl.add(m.group(1));
            }
            p=Pattern.compile("a href=\"(.*?)\"");
            m=p.matcher(splitResult[0]);
            while (m.find()){
                names.add(m.group(1));
            }

            Random random=new Random();
            chosen=random.nextInt(celeburl.size());
            ImageDownloader imagetask=new ImageDownloader();
            Bitmap celebimage=imagetask.execute(celeburl.get(chosen)).get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}