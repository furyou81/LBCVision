package com.example.furyou.lbcvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BuyActivity extends AppCompatActivity {

    private ListView listView;
    private String lab = "";
    List<Product> prods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        listView = (ListView) findViewById(R.id.listView);
        lab = getIntent().getStringExtra("desc1");
        prods = new ArrayList<Product>();

        genererProducts(lab);


    }

    private void genererProducts(String lab){


        //List<Product> tweets = new ArrayList<Product>();
        new getProducts().execute(lab);/*
        tweets.add(new Tweet(Color.BLACK, "Florent", "Mon premier tweet !"));
        tweets.add(new Tweet(Color.BLUE, "Kevin", "C'est ici que ça se passe !"));
        tweets.add(new Tweet(Color.GREEN, "Logan", "Que c'est beau..."));
        tweets.add(new Tweet(Color.RED, "Mathieu", "Il est quelle heure ??"));
        tweets.add(new Tweet(Color.GRAY, "Willy", "On y est presque"));*/
        //return tweets;
    }

    public class getProducts extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... label) {

            byte[] out = "e".getBytes(StandardCharsets.UTF_8);
            try {
                URL url = new URL("https://vivatech.leboncoin.io/search");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST"); // PUT is another valid option
                http.setDoOutput(true);
                http.setRequestProperty("Content-Type", "application/json");
                http.setRequestProperty("api_key", "team5-50a39");
                out = ("{\"filters\": {\"keywords\": {\"text\": \"" + label[0] + "\"}},\"sort_by\": \"date\",\"sort_order\": \"desc\",\"limit\": 5}").getBytes(StandardCharsets.UTF_8);
                int length = out.length;

                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                http.connect();
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                } catch (Exception e) {
                    return (e.toString());
                }
                // Do something with http.getInputStream()

                BufferedInputStream in = new BufferedInputStream(http.getInputStream());
                byte[] contents = new byte[1024];

                int bytesRead = 0;
                String strFileContents = "";
                while((bytesRead = in.read(contents)) != -1) {
                    strFileContents += new String(contents, 0, bytesRead);
                }
                Log.i("VISIONVISION", strFileContents);

                JSONObject jObj = new JSONObject(strFileContents);

                JSONArray res = jObj.getJSONArray("ads");
                Log.i("SIZE", "LEN" + res.length());

                for (int i = 0; i < res.length(); i++)
                {
                    Log.i("LLLLL", "JJJ");
                    JSONObject c = res.getJSONObject(i);
                    JSONObject images = c.getJSONObject("images");
                    Log.i("IMAGGGE", "I" + images.get("thumb_url"));
                    JSONObject addr = c.getJSONObject("location");
                    // Log.i("PPPPHO", "P" + c.getString("images"));
                  //  JSONArray images = c.getJSONArray("images");
                 //   JSONObject im = images.getJSONObject(0);
                 //   JSONArray loc = c.getJSONArray("location");
                 //   JSONObject l = images.getJSONObject(0);
                    Product p = new Product(images.getString("thumb_url"), c.getString("subject"), c.getString("price"), c.getString("category_name"), addr.getString("city_label"), c.getString("index_date"));
                    prods.add(p);

                }

                return ("");
            }
            catch (Exception e) {
                Log.i("EEREUR", "EE");
                return (e.toString());
            }
        }

        protected void onPostExecute(String result) {
            Log.i("CCCCC", "C" + prods.size());
            ItemAdapter adapter = new ItemAdapter(BuyActivity.this, prods);
            listView.setAdapter(adapter);
            if (result.equals("EXPLICIT"))
            {
                //Toast.makeText(MainActivity.this, "Invalid picture, explicit content", Toast.LENGTH_LONG).show();
                //Drawable lock = ContextCompat.getDrawable(MainActivity.this, R.drawable.lock);
                //mlock.setImageDrawable(lock);
            }
        }

    }



}
