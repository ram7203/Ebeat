package com.example.ebeat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebeat.Database.OfficerList;

import java.util.List;

public class OfficerAdapter extends RecyclerView.Adapter<OfficerAdapter.ViewHolder> {

    private List<OfficerList> officerList;
    private OnItemListener onItemListener;

    public OfficerAdapter(List<OfficerList> officerList, OnItemListener onItemListener) {
        this.officerList = officerList;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public OfficerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.officer_item, parent, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficerAdapter.ViewHolder holder, int position) {
        String officer_name = officerList.get(position).getName();
        String officer_id = officerList.get(position).getId();
        String rank = officerList.get(position).getRank();
        long phone_no = officerList.get(position).getPhone_no();

        holder.setData(officer_name, officer_id, rank, phone_no);
    }

    @Override
    public int getItemCount() {
        return officerList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView officer_name, officer_id, rank, phone_no;
        OnItemListener onItemListener;
        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            this.onItemListener = onItemListener;
            officer_name = itemView.findViewById(R.id.officer_name);
            officer_id = itemView.findViewById(R.id.officer_id);
            rank = itemView.findViewById(R.id.rank);
            phone_no = itemView.findViewById(R.id.phone_no);
            itemView.setOnClickListener(this);
        }

        public void setData(String officer_name_txt, String officer_id_txt, String rank_txt, long phone_no_txt) {
            officer_id.setText(officer_id_txt);
            officer_name.setText(officer_name_txt);
            rank.setText(rank_txt);
            phone_no.setText(String.valueOf(phone_no_txt));
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
