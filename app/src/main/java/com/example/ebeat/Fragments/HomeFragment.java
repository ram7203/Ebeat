package com.example.ebeat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ebeat.Adapter;
import com.example.ebeat.Dashboard;
import com.example.ebeat.Database.Database;
import com.example.ebeat.Database.ReportList;
import com.example.ebeat.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements Adapter.OnItemListener{

    TextView name, id, warning;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<ReportList> reportlist;
    Adapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dashboard dashboard = (Dashboard) getActivity();

        name = view.findViewById(R.id.name);
        id = view.findViewById(R.id.id);
        recyclerView = view.findViewById(R.id.recyclerView_home);
        warning = view.findViewById(R.id.warning);

        name.setText("Hi, "+dashboard.name);
        id.setText(dashboard.id+"(id)");

        Database db = new Database();
        reportlist = db.get_reports(dashboard.id, true);
        if(reportlist.isEmpty())
            warning.setVisibility(View.VISIBLE);
//        Log.d("beat", "onViewCreated: "+reportlist.get(0).getDate());

        addtoRecycletView(reportlist);

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
}