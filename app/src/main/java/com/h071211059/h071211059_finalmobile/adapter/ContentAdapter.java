package com.h071211059.h071211059_finalmobile.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.h071211059.h071211059_finalmobile.databinding.ItemLayoutBinding;
import com.h071211059.h071211059_finalmobile.databinding.ItemMiniLayoutBinding;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    ArrayList<ContentItem> contentItems;

    public ContentAdapter(ArrayList<ContentItem> contentItems) {
        this.contentItems = contentItems;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMiniLayoutBinding binding = ItemMiniLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ContentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        holder.onBind(contentItems.get(position));
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {
        ItemMiniLayoutBinding binding;
        public ContentViewHolder(@NonNull ItemMiniLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void onBind(ContentItem contentItem) {
            binding.ivTitle.setText(contentItem.getOriginalTitle());
            Glide.with(binding.getRoot()).load(ApiInstance.IMAGE_BASE_URL + contentItem.getPosterPath()).into(binding.ivPoster);
        }
    }
}
