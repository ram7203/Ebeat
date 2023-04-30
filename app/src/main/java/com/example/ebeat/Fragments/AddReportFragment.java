package com.example.ebeat.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ebeat.Dashboard;
import com.example.ebeat.Database.Database;
import com.example.ebeat.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReportFragment extends Fragment {
    String [] item = {"School", "Govt. Buildings", "Temples", "Malls"};
    String type="";
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    ImageView image;
    RelativeLayout upload_image, save_report;
    ActivityResultLauncher activityResultLauncher;
    EditText report_name, report_officer, report_number;
    boolean is_set;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddReportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddReportFragment newInstance(String param1, String param2) {
        AddReportFragment fragment = new AddReportFragment();
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
        return inflater.inflate(R.layout.fragment_add_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        is_set = false;
        image = view.findViewById(R.id.imageView2);
        upload_image = view.findViewById(R.id.upload_image);
        save_report = view.findViewById(R.id.save_report);
        report_name = view.findViewById(R.id.report_name);
        report_number = view.findViewById(R.id.report_number);
        report_officer = view.findViewById(R.id.report_officer);

        Dashboard dashboard = (Dashboard) getActivity();
        dashboard.title.setText("Add Report");

        autoCompleteTextView = view.findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
                Toast.makeText(dashboard, "Type"+type, Toast.LENGTH_SHORT).show();
            }
        });

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                image.setImageBitmap(bitmap);
                is_set = true;
            }
        });

        save_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rname = report_name.getText().toString();
                String rofficer = report_officer.getText().toString();
                String rno = report_number.getText().toString();
                Log.d("confirm", "onClick: "+rname);

                if(type.isEmpty() || is_set==false || rname.isEmpty() || rofficer.isEmpty() || rno.isEmpty())
                {
                    Toast.makeText(dashboard, "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    image.buildDrawingCache();
                    Bitmap bitmap = image.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();

                    Database db = new Database();
                    boolean state = db.add_report(dashboard.id, rname, type, imageBytes);
                    if(state)
                    {
                        Toast.makeText(dashboard, "Report added successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), Dashboard.class);
                        intent.putExtra("id", dashboard.id);
                        intent.putExtra("name", dashboard.name);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(dashboard, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}