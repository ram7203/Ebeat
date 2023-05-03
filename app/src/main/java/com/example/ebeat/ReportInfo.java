package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class ReportInfo extends AppCompatActivity {
    TextView type, report_name, report_officer, report_number, time_stamp, remarks;
    ImageView image;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info);

        type = findViewById(R.id.type);
        report_name = findViewById(R.id.report_name);
        report_officer = findViewById(R.id.report_officer);
        report_number = findViewById(R.id.report_number);
        image = findViewById(R.id.imageView2);
        time_stamp = findViewById(R.id.time_stamp);
        remarks = findViewById(R.id.remarks);
        Intent intent = getIntent();

        type.setText("Place Type: "+intent.getStringExtra("place_type"));
        report_name.setText("Place Name: "+intent.getStringExtra("place_name"));
        report_officer.setText("Officer Incharge: "+intent.getStringExtra("officer_name"));
        report_number.setText("Officer Incharge ID: "+intent.getStringExtra("number"));
        time_stamp.setText("Visited On: "+intent.getStringExtra("time_stamp"));
        remarks.setText(intent.getStringExtra("remarks"));

        byte[] imagedata = intent.getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
        image.setImageBitmap(bitmap);
    }
}