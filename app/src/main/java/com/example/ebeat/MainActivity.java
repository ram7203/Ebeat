package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ebeat.Database.Database;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    RelativeLayout login;
    EditText loginid, loginpassword;
    String [] details;
    AutoCompleteTextView autoCompleteTextView;
    String [] item = {"Admin", "Supervisor", "Beat Officer"};
    ArrayAdapter<String> adapterItems;
    String rank = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        loginid = findViewById(R.id.loginid);
        loginpassword = findViewById(R.id.loginpassword);
        autoCompleteTextView = findViewById(R.id.auto_complete_text);

        adapterItems = new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rank = parent.getItemAtPosition(position).toString();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = loginid.getText().toString().trim();
                String login_password = loginpassword.getText().toString().trim();
                if(id.isEmpty() || login_password.isEmpty() || rank.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(rank=="Admin"||rank=="Supervisor")
                    {
                        details = new String[1];
                    }
                    else if(rank=="Beat Officer")
                    {
                        details = new String[2];
                    }
                    Database db = new Database();
                    details = db.login(id, login_password, rank);
                    //put the connection in a function and return pass/fail for login
                    switch (rank)
                    {
                        case "Admin":
                            if(details[0]!=null)
                            {
                                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, DashboardAdmin.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", details[0]);
                                intent.putExtra("rank", rank);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Supervisor":
                            if(details[0]!=null)
                            {
                                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, DashboardSupervisor.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", details[0]);
                                intent.putExtra("rank", rank);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "Beat Officer":
                            Log.d("raghuram", "onClick: "+details[0]);
                            if(details[0]!=null)
                            {
                                Log.d("Database", "run: !!!");

                                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                intent.putExtra("id", id);
                                intent.putExtra("name", details[0]);
                                intent.putExtra("rank", rank);
                                intent.putExtra("beat", details[1]);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                            break;
                    }
                }
//                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, Dashboard.class);
//                startActivity(intent);
            }
        });


    }

}