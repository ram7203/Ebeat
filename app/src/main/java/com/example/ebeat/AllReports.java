package com.example.ebeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ebeat.Database.Database;
import com.example.ebeat.Database.ReportList;
import com.example.ebeat.Fragments.ReportInfoFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AllReports extends AppCompatActivity implements Adapter.OnItemListener{
    String id;
    RecyclerView recyclerView;
    TextView warning;
    LinearLayoutManager linearLayoutManager;
    List<ReportList> reportlist = new ArrayList<>();
    Adapter adapter;
    EditText search;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);

        Log.d("all reports", "onCreate: !!");

        Intent intent = getIntent();
        id = intent.getStringExtra("sid");

        recyclerView = findViewById(R.id.recyclerView);
        warning = findViewById(R.id.warning);
        search = findViewById(R.id.search);

        Database db = new Database();
        reportlist = db.get_subordinate_reports(id, false);
//        Log.d("beat", "onViewCreated: "+reportlist.get(0).getDate());

        if(!reportlist.isEmpty())
            addtoRecycletView(reportlist);
        else
            warning.setVisibility(View.INVISIBLE);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }
    @Override
    public void onItemClick(int position) {
        Log.d("click", "onItemClick: ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // choose a desired date format to match the format of the Date object
        String dateString = dateFormat.format(reportlist.get(position).getTime_stamp()); // convert Date to String
        Intent intent = new Intent(AllReports.this, ReportInfo.class);
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
        linearLayoutManager = new LinearLayoutManager(AllReports.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new Adapter(list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void filter(String text)
    {
        ArrayList<ReportList> filteredList = new ArrayList<>();
        for (ReportList item: reportlist)
        {
            if(item.getOfficer_name().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}