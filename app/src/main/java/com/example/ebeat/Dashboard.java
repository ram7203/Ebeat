package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ebeat.Fragments.AddReportFragment;
import com.example.ebeat.Fragments.HomeFragment;
import com.example.ebeat.Fragments.ProfileFragment;
import com.example.ebeat.Fragments.ReportFragment;
import com.example.ebeat.Fragments.ScheduleFragment;
import com.example.ebeat.databinding.ActivityDashboardBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Dashboard extends AppCompatActivity {
    public ActivityDashboardBinding binding;
    public String id, name;
    public FloatingActionButton add;
    public TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        add = findViewById(R.id.add);
        title = findViewById(R.id.textView);

        replacefragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home:
                    replacefragment(new HomeFragment());
                    break;
                case R.id.schedule:
                    replacefragment(new ScheduleFragment());
                    break;
                case R.id.report:
                    replacefragment(new ReportFragment());
                    break;
                case R.id.profile:
                    replacefragment(new ProfileFragment());
                    break;
            }
            return true;
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replacefragment(new AddReportFragment());
            }
        });
    }

    public void replacefragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }
}