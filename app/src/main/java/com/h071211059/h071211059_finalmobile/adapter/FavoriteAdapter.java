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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{
    private ArrayList<ContentItem> contentItems;

    private OnItemClickCallback onItemClickListener;

    public FavoriteAdapter(ArrayList<ContentItem> contentItems) {
        this.contentItems = contentItems;
    }

    public void setOnItemClickListener(OnItemClickCallback onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutBinding binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        holder.onBind(contentItems.get(position));
    }

    @Override
    public int getItemCount() {
        return contentItems.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ItemLayoutBinding binding;
        public FavoriteViewHolder(@NonNull ItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(ContentItem contentItem) {
            if (contentItem.getTitle() != null) {
                binding.ivTitle.setText(contentItem.getTitle());
                binding.ivYear.setText(contentItem.getReleaseDate());
            } else {
                binding.ivTitle.setText(contentItem.getName());
                binding.ivYear.setText(contentItem.getFirstAirDate());
            }

            Glide.with(itemView.getContext())
                    .load(ApiInstance.IMAGE_BASE_URL + contentItem.getPosterPath())
                    .into(binding.ivImage);
            binding.getRoot().setOnClickListener(v -> onItemClickListener.onItemClicked(contentItem));
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(ContentItem contentItem);
    }
}
