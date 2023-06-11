package com.h071211059.h071211059_finalmobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.h071211059.h071211059_finalmobile.databinding.ItemCastBinding;
import com.h071211059.h071211059_finalmobile.model.Cast;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {

    private ArrayList<Cast> casts;

    public CastAdapter(ArrayList<Cast> casts) {
        this.casts = casts;
    }


    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCastBinding binding = ItemCastBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CastHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, int position) {
        holder.onBind(casts.get(position));
    }

    @Override
    public int getItemCount() {
        return casts.size();
    }

    public class CastHolder extends RecyclerView.ViewHolder{
        private ItemCastBinding binding;

        public CastHolder(@NonNull ItemCastBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void onBind(Cast cast) {
            binding.tvActor.setText(cast.getName());
            binding.tvCharacter.setText(cast.getCharacter());
            if (cast.getProfilePath() != null) {
                Glide.with(binding.getRoot()).load(ApiInstance.IMAGE_BASE_URL + cast.getProfilePath()).into(binding.ivProfile);
            }
        }
    }

}
