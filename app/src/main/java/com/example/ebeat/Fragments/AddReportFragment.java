package com.example.ebeat.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
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
import android.text.Editable;
import android.util.Log;
import android.util.SparseArray;
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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.ByteArrayOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReportFragment extends Fragment {
    String [] item = {"Schools", "Govt. Buildings", "Temples", "Malls"};
    String type="", place="";
    AutoCompleteTextView autoCompleteTextView, report_name;
    ArrayAdapter<String> adapterItems, placeAdapter;
    ImageView image;
    RelativeLayout upload_image, save_report, check;
    ActivityResultLauncher activityResultLauncher;
    EditText report_officer, report_number, remarks;
    boolean is_set, is_valid;
    Bitmap bitmap1;


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
        is_valid = false;
        image = view.findViewById(R.id.imageView2);
        check = view.findViewById(R.id.check);
        upload_image = view.findViewById(R.id.upload_image);
        save_report = view.findViewById(R.id.save_report);
        report_name = view.findViewById(R.id.report_name);
        report_number = view.findViewById(R.id.report_number);
        report_officer = view.findViewById(R.id.report_officer);
        remarks = view.findViewById(R.id.remarks);

        Dashboard dashboard = (Dashboard) getActivity();
        dashboard.title.setText("Add Report");

        report_officer.setText(dashboard.name);
        report_number.setText(dashboard.id);

        autoCompleteTextView = view.findViewById(R.id.auto_complete_text);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
                Toast.makeText(dashboard, "Type"+type, Toast.LENGTH_SHORT).show();

                Database db = new Database();
                String [] places = db.get_places(type, dashboard.beat_id);

                placeAdapter = new ArrayAdapter<>(getContext(), R.layout.list_item, places);
                report_name.setAdapter(placeAdapter);

                report_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        place = parent.getItemAtPosition(position).toString();
                    }
                });
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
                bitmap1 = (Bitmap) bundle.get("data");
                image.setImageBitmap(bitmap1);
                is_set = true;
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                image.buildDrawingCache();
//                Bitmap bitmap1 = image.getDrawingCache();
                detectFace(bitmap1);
            }
        });

        save_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rname = place;
                String rofficer = report_officer.getText().toString();
                String rno = report_number.getText().toString();
                String remark_txt = remarks.getText().toString();
                Log.d("confirm", "onClick: "+rname);

                if(type.isEmpty() || is_set==false || rname.isEmpty() || rofficer.isEmpty() || rno.isEmpty() || remark_txt.isEmpty())
                {
                    Toast.makeText(dashboard, "Fill all fields!", Toast.LENGTH_SHORT).show();
                }
                else if(is_valid==false)
                {
                    Toast.makeText(dashboard, "Image should be verified", Toast.LENGTH_SHORT).show();
                }
                else
                {
//                    image.buildDrawingCache();
//                    Bitmap bitmap = image.getDrawingCache();

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();

                    Database db = new Database();
                    boolean state = db.add_report(dashboard.id, rname, type, imageBytes, dashboard.latitude, dashboard.longitude, remark_txt);
                    if(state)
                    {
                        Toast.makeText(dashboard, "Report added successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), Dashboard.class);
                        intent.putExtra("id", dashboard.id);
                        intent.putExtra("name", dashboard.name);
                        intent.putExtra("rank", dashboard.rank);
                        intent.putExtra("beat", dashboard.beat_id);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(dashboard, "Some error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void detectFace(Bitmap bitmap)
    {
        Paint boxPaint = new Paint();
        boxPaint.setStrokeWidth(5);
        boxPaint.setColor(Color.GREEN);
        boxPaint.setStyle(Paint.Style.STROKE);

        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);

        FaceDetector faceDetector = new FaceDetector.Builder(getContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if(!faceDetector.isOperational())
        {
            Toast.makeText(getContext(), "Face Detector needs to set up. Please RESTART your app", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);

        if(sparseArray.size()==0)
            Toast.makeText(getContext(), "Face not visible!", Toast.LENGTH_SHORT).show();
        else
        {
            is_valid = true;
            Toast.makeText(getContext(), "Face Detected!", Toast.LENGTH_SHORT).show();
        }
//        Log.d("anushka", "detectFace: "+sparseArray.size());

        for(int i=0;i<sparseArray.size();i++) {
            Face face = sparseArray.valueAt(i);
            float x1 = face.getPosition().x;
            float y1 = face.getPosition().y;
            float x2 = x1 + face.getWidth();
            float y2 = y1 + face.getHeight();
            RectF rectF = new RectF(x1, y1, x2, y2);
            canvas.drawRoundRect(rectF, 2, 2, boxPaint);
        }
        image.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
//        return true;

    }

}