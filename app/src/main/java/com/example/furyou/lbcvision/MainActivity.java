package com.example.furyou.lbcvision;

import org.json.*;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView mlock;
    private TextView mtext;
    private Spinner objet;
    private ArrayList<String> descr;
    private Map<String, String> cat;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.mImageView);
        mtext = (TextView) findViewById(R.id.mtext);
        mtext.setMovementMethod(new ScrollingMovementMethod());
        mlock = (ImageView) findViewById(R.id.mlock);
        objet = (Spinner) findViewById(R.id.objet);
        cat = new HashMap<String, String>();
       // cat.put("table")
    }

    public void dispatchTakePictureIntent(View v) {
        mlock.setImageDrawable(null);
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.INTERNET ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.INTERNET  },
                    0 );
        }else{

            if (ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.CAMERA  },
                        0 );
            }else{
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            String base64 = to_base_64(imageBitmap);

            Log.i("EEEEEEEEEE", "RRRRRRRRR" + base64);
            new Send64().execute(base64);
            //send_json(base64);

        }
    }

    public void send_data()
    {
        try {
            String rawData = "id=10";
            String type = "application/x-www-form-urlencoded";
            String encodedData = URLEncoder.encode(rawData, "UTF-8");
            URL u = new URL("http://www.example.com/page.php");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", type);
            conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
            OutputStream os = conn.getOutputStream();
            os.write(encodedData.getBytes());
        }catch (Exception e){}
    }

    private String to_base_64(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return (encoded);
    }




    public class Send64 extends AsyncTask<String, Void, String> {
        private Exception exception;

        protected String doInBackground(String... base64) {

            byte[] out = "e".getBytes(StandardCharsets.UTF_8);
            try {
                URL url = new URL("https://vision.googleapis.com/v1/images:annotate?key=AIzaSyD7sTHRHHWqu7m1LbD_Q_qahauCVSQmC2c");
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST"); // PUT is another valid option
                http.setDoOutput(true);
                Log.i("ss", "LLLLLLLLLLLL" + base64[0]);
                //    Log.i("jj",base64 + base64 +base64 +base64);
                out = ("{\"requests\": [ {\"image\": { \"content\":\"" + base64[0] + "\"}, \"features\": [ { \"type\": \"LABEL_DETECTION\"}, { \"type\": \"SAFE_SEARCH_DETECTION\"} ]}]}").getBytes(StandardCharsets.UTF_8);
                //     Log.i("out", "{\"requests\": [ {\"image\": { \"content\":" + base64 + "}, \"features\": [ { \"type\": \"LABEL_DETECTION\"}]}]}");
                //     byte[] out = "{\"username\":\"root\",\"password\":\"password\"}".getBytes(StandardCharsets.UTF_8);
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
                descr = new ArrayList<String>();
                //String descr = "";

                // PARSING JSON RESPONSE FROM GOOGLE VISION
                    Log.i("VISIONVISION", strFileContents);
                    JSONObject jObj = new JSONObject(strFileContents);

                    JSONArray res = jObj.getJSONArray("responses");
                   // String description = jObject.getString("description");
               // JSONArray arr = jObject.getJSONArray("posts");
                JSONObject o = res.getJSONObject(0);
                JSONArray labels = o.getJSONArray("labelAnnotations");
                Log.i("SIZE", "S=" + strFileContents);
                //  JSONObject o2 = res.getJSONObject(1);
                String invalid = o.getString("safeSearchAnnotation");
                Log.i("RRRRR", invalid);


                if (invalid.contains("\"POSSIBLE") || invalid.contains("\"LIKELY") || invalid.contains("\"VERY_LIKELY"))
                    return ("EXPLICIT");
                   // Log.i("EXPLICIT", "POSSIBLE");


           /*     JSONObject inv = invalid.getJSONObject(0);
                if (inv.getString("adult").contains("POSSIBLE") || inv.getString("adult").contains("LIKELY") || inv.getString("adult").contains("VERY_LIKELY"))
                    Log.i("", "POSSIBLE");
                if (inv.getString("spoof").contains("POSSIBLE") || inv.getString("spoof").contains("LIKELY") || inv.getString("spoof").contains("VERY_LIKELY"))
                    Log.i("", "POSSIBLE");
                if (inv.getString("medical").contains("POSSIBLE") || inv.getString("medical").contains("LIKELY") || inv.getString("medical").contains("VERY_LIKELY"))
                    Log.i("", "POSSIBLE");
                if (inv.getString("violence").contains("POSSIBLE") || inv.getString("violence").contains("LIKELY") || inv.getString("violence").contains("VERY_LIKELY"))
                    Log.i("", "POSSIBLE");
                if (inv.getString("racy").contains("POSSIBLE") || inv.getString("racy").contains("LIKELY") || inv.getString("racy").contains("VERY_LIKELY"))
                    Log.i("", "POSSIBLE");

*/

                for (int i = 0; i < labels.length(); i++)
                {
                    JSONObject c = labels.getJSONObject(i);
                    descr.add(c.getString("description"));
                  //  descr = descr + "/" + c.getString("description");
                }

                    return ("");
                //return (strFileContents);
                // Toast.makeText(this,http.getInputStream().toString(), Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                return (e.toString());
            }
        }

        protected void onPostExecute(String result) {
            // TODO: check this.exception
            // TODO: do something with the feed
            if (result.equals("EXPLICIT"))
            {
                Toast.makeText(MainActivity.this, "Invalid picture, explicit content", Toast.LENGTH_LONG).show();
                Drawable lock = ContextCompat.getDrawable(MainActivity.this, R.drawable.lock);
                mlock.setImageDrawable(lock);
            }
            else
            {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, descr);
                objet.setAdapter(adapter);
            }
            mtext.setText(result);
        }

    }

}
