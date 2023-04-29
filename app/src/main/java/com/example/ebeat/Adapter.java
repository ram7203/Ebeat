package com.example.ebeat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebeat.Database.ReportList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private List<ReportList> reportlist;
    private OnItemListener onItemListener;

    public Adapter(List<ReportList> reportlist, OnItemListener onItemListener) {
        this.reportlist = reportlist;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String officer_name = reportlist.get(position).getOfficer_name();
        String officer_id = reportlist.get(position).getOfficer_id();
        Date date = reportlist.get(position).getDate();
        String place_name = reportlist.get(position).getPlace_name();

        holder.setData(officer_name, officer_id, date, place_name);
    }

    @Override
    public int getItemCount() {
        return reportlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView officer_name, officer_id, date, place_name;
        OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            this.onItemListener = onItemListener;
            officer_name = itemView.findViewById(R.id.officer_name);
            officer_id = itemView.findViewById(R.id.officer_id);
            date = itemView.findViewById(R.id.date);
            place_name = itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener(this);
        }

        public void setData(String officer_name_txt, String officer_id_txt, Date date_txt, String place_name_txt) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // choose a desired date format to match the format of the Date object
            String dateString = dateFormat.format(date_txt); // convert Date to String

            officer_name.setText(officer_name_txt);
            officer_id.setText(officer_id_txt);
            date.setText(dateString);
            place_name.setText(place_name_txt);
        }


        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAbsoluteAdapterPosition());
        }
    }
    public interface OnItemListener{
        void onItemClick(int position);
    }
}
