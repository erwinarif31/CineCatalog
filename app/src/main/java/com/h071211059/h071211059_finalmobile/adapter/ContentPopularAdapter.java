package com.h071211059.h071211059_finalmobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.h071211059.h071211059_finalmobile.databinding.ItemLayoutBinding;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;

import java.util.ArrayList;

public class ContentPopularAdapter extends RecyclerView.Adapter<ContentPopularAdapter.MoviePopularViewHolder> {

    ArrayList<ContentItem> contentItems;

    public ContentPopularAdapter(ArrayList<ContentItem> contentItems) {
        this.contentItems = contentItems;
    }

    @NonNull
    @Override
    public MoviePopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutBinding binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MoviePopularViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviePopularViewHolder holder, int position) {
        holder.onBind(contentItems.get(position));
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public class MoviePopularViewHolder extends RecyclerView.ViewHolder {
        ItemLayoutBinding binding;
        public MoviePopularViewHolder(@NonNull ItemLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void onBind(ContentItem contentItem) {
            if (contentItem.getTitle() != null) {
                binding.ivTitle.setText(contentItem.getTitle());
                binding.ivYear.setText(contentItem.getReleaseDate());
            } else {
                binding.ivTitle.setText(contentItem.getName());
                binding.ivYear.setText(contentItem.getFirstAirDate());
            }
            Glide.with(binding.getRoot()).load(ApiInstance.IMAGE_BASE_URL + contentItem.getPosterPath()).into(binding.ivImage);
        }
    }
}
