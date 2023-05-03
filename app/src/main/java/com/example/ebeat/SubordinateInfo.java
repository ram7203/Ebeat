package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ebeat.Database.Database;
import com.example.ebeat.Database.OfficerList;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class SubordinateInfo extends AppCompatActivity{
    TextView name, id, result, officer_id, rank, phone_no, supervisor;
    OfficerList officerList;
    Button check_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate_info);

        Intent intent = getIntent();

        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        result = findViewById(R.id.result);
        officer_id = findViewById(R.id.officer_id);
        rank = findViewById(R.id.rank);
        phone_no = findViewById(R.id.phone_no);
        supervisor = findViewById(R.id.supervisor);
        check_location = findViewById(R.id.check_location);


        name.setText(intent.getStringExtra("name"));
        id.setText(intent.getStringExtra("id"));

        Database db = new Database();
        officerList = db.get_profile(intent.getStringExtra("id"));

        result.setText(String.valueOf(officerList.getNo_of_patrols()));
        officer_id.setText(officerList.getBeat_id());
        rank.setText(officerList.getRank());
        phone_no.setText(String.valueOf(officerList.getPhone_no()));
        supervisor.setText(officerList.getSupervisor_name());

        check_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SubordinateInfo.this, MapsActivity.class);
                intent1.putExtra("id", intent.getStringExtra("id"));
                startActivity(intent1);
            }
        });
    }
}