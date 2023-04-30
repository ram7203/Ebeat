package com.example.ebeat.API;

import com.example.ebeat.Database.NewsHeadlines;

import java.util.List;

public interface OnFetchDataListener<NewsApiResponse> {
    void onFetchData(List<NewsHeadlines> list, String message);
    void OnError(String message);
}
