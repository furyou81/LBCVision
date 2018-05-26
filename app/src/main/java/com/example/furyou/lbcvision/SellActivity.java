package com.example.furyou.lbcvision;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class SellActivity extends AppCompatActivity {

    private ImageView mPic;
    private EditText desc;
    Bitmap imageBitmap;
    private String desc1;
    private String desc2;
    private String desc3;
    private CharSequence descrs[] = new String[3];
    private EditText cattv;
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Intent intent = getIntent();
        imageBitmap = (Bitmap) intent.getParcelableExtra("image");
        desc1 = intent.getStringExtra("desc1");
        desc2 = intent.getStringExtra("desc2");
        desc3 = intent.getStringExtra("desc3");
        descrs[0] = desc1;
        descrs[1] = desc2;
        descrs[2] = desc3;
        mPic = (ImageView) findViewById(R.id.mPic);
        mPic.setImageBitmap(imageBitmap);
        desc = (EditText) findViewById(R.id.desc);
        desc.setText(desc1);
        map.put("laptop", "Computer & Equipment");
        map.put("computer", "Computer & Equipment");
        map.put("netbook", "Computer & Equipment");
        map.put("electronic device", "Computer & Equipment");
        map.put("mobile phone", "Multimedia");
        map.put("telephony", "Multimedia");
        map.put("gadget", "Multimedia");
        map.put("portable communications device", "Multimedia");
        cattv = (EditText) findViewById(R.id.cattv);
        cattv.setText(map.get(desc1));
    }

    public void description(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Description:");
        builder.setItems(descrs, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                desc.setText(descrs[which]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
