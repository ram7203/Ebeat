package com.example.ebeat.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ebeat.Dashboard;
import com.example.ebeat.Database.ReportList;
import com.example.ebeat.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportInfoFragment extends Fragment {
    private List<ReportList> reportlist;
    int pos;
    TextView type, report_name, report_officer, report_number, time_stamp, remarks;
    ImageView image;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportInfoFragment() {
        // Required empty public constructor
    }

    public ReportInfoFragment(List<ReportList> reportlist, int pos)
    {
        this.reportlist = reportlist;
        this.pos = pos;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportInfoFragment newInstance(String param1, String param2) {
        ReportInfoFragment fragment = new ReportInfoFragment();
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
        return inflater.inflate(R.layout.fragment_report_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dashboard dashboard = (Dashboard) getActivity();

        dashboard.title.setText("Recent Reports");

        type = view.findViewById(R.id.type);
        report_name = view.findViewById(R.id.report_name);
        report_officer = view.findViewById(R.id.report_officer);
        report_number = view.findViewById(R.id.report_number);
        image = view.findViewById(R.id.imageView2);
        time_stamp = view.findViewById(R.id.time_stamp);
        remarks = view.findViewById(R.id.remarks);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // choose a desired date format to match the format of the Date object
        String dateString = dateFormat.format(reportlist.get(pos).getTime_stamp()); // convert Date to String

        type.setText("Place Type: "+reportlist.get(pos).getPlace_type());
        report_name.setText("Place Name: "+reportlist.get(pos).getPlace_name());
        report_officer.setText("Officer Incharge: "+reportlist.get(pos).getOfficer_name());
        report_number.setText("Officer Incharge ID: "+reportlist.get(pos).getOfficer_id());
        remarks.setText(reportlist.get(pos).getRemarks());
        time_stamp.setText("Visited On: "+dateString);

        byte[] imagedata = reportlist.get(pos).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
        image.setImageBitmap(bitmap);
    }
}