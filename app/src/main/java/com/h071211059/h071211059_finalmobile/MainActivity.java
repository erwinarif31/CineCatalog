package com.h071211059.h071211059_finalmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;

import com.h071211059.h071211059_finalmobile.databinding.ActivityMainBinding;
import com.h071211059.h071211059_finalmobile.fragment.FavoriteFragment;
import com.h071211059.h071211059_finalmobile.fragment.MovieFragment;
import com.h071211059.h071211059_finalmobile.fragment.TvFragment;

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

       Fragment fragment = fragmentManager.findFragmentByTag(MovieFragment.class.getSimpleName());

       if (!(fragment instanceof MovieFragment)) {
           switchFragment(movieFragment);
       }

       binding.llMovie.setOnClickListener(v -> switchFragment(MovieFragment.getInstance()));
       binding.llTvShow.setOnClickListener(v -> switchFragment(TvFragment.getInstance()));
       binding.llFavorite.setOnClickListener(v -> switchFragment(FavoriteFragment.getInstance()));
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        binding.ivMovie.setImageResource(R.drawable.ic_movie_unfilled);
        binding.ivTvShow.setImageResource(R.drawable.ic_tv_unfilled);
        binding.ivFavorite.setImageResource(R.drawable.ic_favorite_unfilled);

        switch (fragment.getClass().getSimpleName()) {
            case "MovieFragment":
                MovieFragment.currentPage = 1;
                binding.tvAppbarTitle.setText(R.string.movie);
                binding.ivMovie.setImageResource(R.drawable.ic_movie);
                break;
            case "TvFragment":
                TvFragment.currentPage = 1;
                binding.ivTvShow.setImageResource(R.drawable.ic_tv);
                binding.tvAppbarTitle.setText(R.string.tv_show);
                break;
            case "FavoriteFragment":
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite);
                binding.tvAppbarTitle.setText(R.string.favorite);
                break;
        }

        if (fragment instanceof MovieFragment) {
            transaction
                    .replace(R.id.fl_container, fragment,
                            MovieFragment.class.getSimpleName())
                    .commit();
        } else {
            transaction
                    .replace(R.id.fl_container, fragment,
                            MovieFragment.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }
}