package com.example.ebeat.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ebeat.Dashboard;
import com.example.ebeat.Database.Database;
import com.example.ebeat.Database.OfficerList;
import com.example.ebeat.Database.ReportList;
import com.example.ebeat.MainActivity;
import com.example.ebeat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView name, id, result, officer_id, rank, phone_no, supervisor;
    Button logout;
    OfficerList officerList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dashboard dashboard = (Dashboard) getActivity();

        name = view.findViewById(R.id.name);
        id = view.findViewById(R.id.id);
        logout = view.findViewById(R.id.logout);
        result = view.findViewById(R.id.result);
        officer_id = view.findViewById(R.id.officer_id);
        rank = view.findViewById(R.id.rank);
        phone_no = view.findViewById(R.id.phone_no);
        supervisor = view.findViewById(R.id.supervisor);

        name.setText("Hi, "+dashboard.name);
        id.setText(dashboard.id+"(id)");



        Database db = new Database();
        officerList = db.get_profile(dashboard.id);

        result.setText(String.valueOf(officerList.getNo_of_patrols()));
        officer_id.setText(officerList.getBeat_id());
        rank.setText(officerList.getRank());
        phone_no.setText(String.valueOf(officerList.getPhone_no()));
        supervisor.setText(officerList.getSupervisor_name());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}