package com.h071211059.h071211059_finalmobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.h071211059.h071211059_finalmobile.databinding.ItemGenreBinding;
import com.h071211059.h071211059_finalmobile.model.Genre;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

    ArrayList<Genre> genreItems;

    public GenreAdapter(ArrayList<Genre> genreItems) {
        this.genreItems = genreItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGenreBinding binding = ItemGenreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        holder.onBind(genreItems.get(position));
    }

    @Override
    public int getItemCount() {
        return genreItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemGenreBinding binding;
        public ViewHolder(@NonNull ItemGenreBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void onBind(Genre genre) {
            binding.tvGenre.setText(genre.getName());
        }
    }
}
