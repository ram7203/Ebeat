package com.example.ebeat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebeat.Adapter;
import com.example.ebeat.Dashboard;
import com.example.ebeat.Database.Database;
import com.example.ebeat.Database.ReportList;
import com.example.ebeat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment implements Adapter.OnItemListener{

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ReportList> reportlist;
    Adapter adapter;
    TextView warning;
    EditText search;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dashboard dashboard = (Dashboard) getActivity();

        dashboard.title.setText("Recent Patrols");

        recyclerView = view.findViewById(R.id.recyclerView1);
        warning = view.findViewById(R.id.warning);
        search = view.findViewById(R.id.search);

        Database db = new Database();
        reportlist = db.get_reports(dashboard.beat_id, false);
        Log.d("beat", "onViewCreated: "+reportlist.get(0).getDate());

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
//        Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();

        Dashboard dashboard = (Dashboard) getActivity();
        dashboard.replacefragment(new ReportInfoFragment(reportlist, position));
    }

    public void addtoRecycletView(List<ReportList> list)
    {
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new Adapter(list, this));
        linearLayoutManager = new LinearLayoutManager(getContext());
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
            if(item.getPlace_name().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}