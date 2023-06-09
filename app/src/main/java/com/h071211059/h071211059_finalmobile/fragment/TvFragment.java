package com.h071211059.h071211059_finalmobile.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h071211059.h071211059_finalmobile.R;
import com.h071211059.h071211059_finalmobile.databinding.FragmentTvBinding;

public class TvFragment extends Fragment {
    private FragmentTvBinding binding;

    private static TvFragment instance;

    private TvFragment() {}

    public static TvFragment getInstance() {
        if (instance == null) {
            instance = new TvFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTvBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}