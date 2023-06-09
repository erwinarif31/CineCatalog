package com.h071211059.h071211059_finalmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.os.Bundle;

import com.h071211059.h071211059_finalmobile.databinding.ActivityMainBinding;
import com.h071211059.h071211059_finalmobile.fragment.MovieFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       fragmentManager = getSupportFragmentManager();
       MovieFragment movieFragment = MovieFragment.getInstance();
    }
}