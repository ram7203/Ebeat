package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.ebeat.Database.ReportList;
import com.example.ebeat.Fragments.ReportInfoFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DashboardSupervisor extends AppCompatActivity implements Adapter.OnItemListener{
    ConstraintLayout all_reports, my_subordinates;
    RecyclerView recyclerView;
    String sid, sname;
    TextView name, id, warning;
    LinearLayoutManager linearLayoutManager;
    List<ReportList> reportlist;
    Adapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_supervisor);

        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        all_reports = findViewById(R.id.all_reports);
        my_subordinates = findViewById(R.id.my_subordinates);
        recyclerView = findViewById(R.id.recyclerView);
        warning = findViewById(R.id.warning);

        Intent intent = getIntent();
        sid = intent.getStringExtra("id");
        sname = intent.getStringExtra("name");

        name.setText("Hi, "+sname);
        id.setText(sid+"(id)");

        Database db = new Database();
        reportlist = db.get_subordinate_reports(sid, true);
        if(reportlist.isEmpty())
            warning.setVisibility(View.VISIBLE);
        addtoRecycletView(reportlist);

        all_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DashboardSupervisor.this, AllReports.class);
                intent1.putExtra("sid", sid);
                startActivity(intent1);
            }
        });

        my_subordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DashboardSupervisor.this, Subordinates.class);
                intent1.putExtra("sid", sid);
                startActivity(intent1);
            }
        });

    }
    @Override
    public void onItemClick(int position) {
        Log.d("click", "onItemClick: ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // choose a desired date format to match the format of the Date object
        String dateString = dateFormat.format(reportlist.get(position).getTime_stamp()); // convert Date to String
        Intent intent = new Intent(DashboardSupervisor.this, ReportInfo.class);
        intent.putExtra("place_type", reportlist.get(position).getPlace_type());
        intent.putExtra("place_name", reportlist.get(position).getPlace_name());
        intent.putExtra("officer_name", reportlist.get(position).getOfficer_name());
        intent.putExtra("number", reportlist.get(position).getOfficer_id());
        intent.putExtra("time_stamp", dateString);
        intent.putExtra("image", reportlist.get(position).getImage());
        intent.putExtra("remarks", reportlist.get(position).getRemarks());
        startActivity(intent);

    }

    public void addtoRecycletView(List<ReportList> list)
    {
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new Adapter(list, this));
        linearLayoutManager = new LinearLayoutManager(DashboardSupervisor.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}