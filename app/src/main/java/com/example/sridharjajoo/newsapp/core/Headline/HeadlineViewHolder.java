package com.example.sridharjajoo.newsapp.core.Headline;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sridharjajoo.newsapp.R;

import javax.inject.Inject;

public class HeadlineViewHolder extends RecyclerView.ViewHolder {

    public TextView description;
    public ImageView newsImage;
    public CardView cardView;
    public TextView newsTime;
    public TextView newsSource;

    public HeadlineViewHolder(View itemView) {
        super(itemView);
        description = (TextView) itemView.findViewById(R.id.description);
        newsImage = (ImageView) itemView.findViewById(R.id.image_news);
        cardView = (CardView) itemView.findViewById(R.id.item_headline_card);
        newsTime = (TextView) itemView.findViewById(R.id.news_time);
        newsSource = (TextView) itemView.findViewById(R.id.news_source);
    }

    public void handleClickAction(View view, String description, String urlToImage) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
        Fragment fragment = new HeadlineDetail();
        appCompatActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}