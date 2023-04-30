package com.example.ebeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebeat.Fragments.AddReportFragment;
import com.example.ebeat.Fragments.HomeFragment;
import com.example.ebeat.Fragments.ProfileFragment;
import com.example.ebeat.Fragments.ReportFragment;
import com.example.ebeat.Fragments.ScheduleFragment;
import com.example.ebeat.databinding.ActivityDashboardBinding;
//import com.google.android.gms.location.LocationListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Locale;

public class Dashboard extends AppCompatActivity implements LocationListener {
    public ActivityDashboardBinding binding;
    public String id, name;
    public FloatingActionButton add;
    public TextView title;
    LocationManager locationManager;



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
                getLocation();
            }
        });

        if(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }


    }

    public void replacefragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("MissingPermission")
    private void getLocation()
    {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, Dashboard.this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Toast.makeText(this, ""+location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        try {
            Geocoder geocoder = new Geocoder(Dashboard.this, Locale.getDefault());
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

}