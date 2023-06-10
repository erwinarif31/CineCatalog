package com.h071211059.h071211059_finalmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.h071211059.h071211059_finalmobile.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}