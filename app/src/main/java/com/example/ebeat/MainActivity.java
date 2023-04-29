package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ebeat.Database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    RelativeLayout login;
    EditText loginid, loginpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        loginid = findViewById(R.id.loginid);
        loginpassword = findViewById(R.id.loginpassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = loginid.getText().toString().trim();
                String login_password = loginpassword.getText().toString().trim();

                Database db = new Database();
                String name = db.login(id, login_password);
                //put the connection in a function and return pass/fail for login
                if(!name.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MainActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, Dashboard.class);
//                startActivity(intent);

            }
        });
    }
}