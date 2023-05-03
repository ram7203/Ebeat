package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.ebeat.Database.Database;
import com.example.ebeat.Database.OfficerList;
import com.example.ebeat.Database.ReportList;

import java.text.SimpleDateFormat;
import java.util.List;

public class Subordinates extends AppCompatActivity implements OfficerAdapter.OnItemListener{
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<OfficerList> officerList;
    OfficerAdapter adapter;
    TextView warning;
    String sid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinates);

        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");

        recyclerView = findViewById(R.id.recyclerView);
        warning = findViewById(R.id.warning);

        Database db = new Database();
        officerList = db.get_subordinates(sid);


//        Log.d("beat", "onViewCreated: "+reportlist.get(0).getDate());

        if(!officerList.isEmpty())
            addtoRecycletView(officerList);
        else
            warning.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onItemClick(int position) {
        Log.d("click", "onItemClick: ");
        Intent intent = new Intent(Subordinates.this, SubordinateInfo.class);
        intent.putExtra("id", officerList.get(position).getId());
        intent.putExtra("name", officerList.get(position).getName());
        startActivity(intent);
    }

    public void addtoRecycletView(List<OfficerList> list)
    {
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new Adapter(list, this));
        Log.d("raghuram", "onCreate: !!!");
        linearLayoutManager = new LinearLayoutManager(Subordinates.this);

        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new OfficerAdapter(list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}