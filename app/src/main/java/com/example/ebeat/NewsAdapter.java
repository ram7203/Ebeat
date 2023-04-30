package com.example.ebeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebeat.Database.NewsHeadlines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private Context context;
    private List<NewsHeadlines> headlines;

    public NewsAdapter(Context context, List<NewsHeadlines> headlines) {
        this.context = context;
        this.headlines = headlines;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.headline_list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.text_title.setText(headlines.get(position).getTitle());
        holder.text_source.setText(headlines.get(position).getSource().getName());
        if(headlines.get(position).getUrlToImage()!=null)
        {
            Picasso.get().load(headlines.get(position).getUrlToImage()).into(holder.img_headline);
        }
    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }
}
