package com.h071211059.h071211059_finalmobile;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.h071211059.h071211059_finalmobile.databinding.ActivityFavoriteBinding;
import com.h071211059.h071211059_finalmobile.db.ContentDBContract;
import com.h071211059.h071211059_finalmobile.db.DataHelper;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;

public class FavoriteActivity extends AppCompatActivity {

    public static final String EXTRA_CONTENT = "extra_content";

    private ActivityFavoriteBinding binding;

    private ContentItem contentItem;

    private boolean overviewExpanded = false;

    private boolean isFavorite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contentItem = getIntent().getParcelableExtra(EXTRA_CONTENT);

        setContent();

        binding.clToggle.setOnClickListener(v -> expandOverview());
        binding.ivFavorite.setOnClickListener(v -> addToFavorites());
        binding.ivBack.setOnClickListener(v -> onBackPressed());
    }

    private void expandOverview() {
        if (overviewExpanded) {
            binding.tvOverview.setMaxLines(2);
            binding.ivToggle.setImageResource(R.drawable.ic_arrow_down);
            binding.tvToggle.setText("Show More");
            overviewExpanded = false;
        } else {
            binding.tvOverview.setMaxLines(Integer.MAX_VALUE);
            binding.ivToggle.setImageResource(R.drawable.ic_arrow_up);
            binding.tvToggle.setText("Show Less");
            overviewExpanded = true;
        }
    }

    private void setContent() {
        binding.ivFavorite.setImageResource(R.drawable.ic_favorite);
        if (contentItem.getTitle() != null) {
            binding.tvTitle.setText(contentItem.getTitle());
            binding.ivType.setImageResource(R.drawable.ic_movie);
        } else {
            binding.tvTitle.setText(contentItem.getName());
            binding.ivType.setImageResource(R.drawable.ic_tv);
        }

        binding.rbRating.setRating(Float.valueOf(contentItem.getVoteAverage()) / 2);

        if (contentItem.getOverview().equals("")) {
            binding.tvOverview.setText("No overview available");
            binding.clToggle.setVisibility(View.GONE);
        } else {
            binding.tvOverview.setText(contentItem.getOverview());
        }

        Glide.with(binding.getRoot()).load(ApiInstance.IMAGE_BASE_URL + contentItem.getPosterPath()).into(binding.ivImage);
        Glide.with(binding.getRoot()).load(ApiInstance.IMAGE_BASE_URL + contentItem.getBackdropPath()).into(binding.ivBanner);
    }

    private void addToFavorites() {
        if (!isFavorite) {
            DataHelper dataHelper = new DataHelper(FavoriteActivity.this);
            dataHelper.open();

            ContentValues values = new ContentValues();
            values.put(ContentDBContract.ContentColumns.CONTENT_ID, contentItem.getId());
            values.put(ContentDBContract.ContentColumns.BACKDROP_PATH, contentItem.getBackdropPath());
            values.put(ContentDBContract.ContentColumns.FIRST_AIR_DATE, contentItem.getFirstAirDate());
            values.put(ContentDBContract.ContentColumns.RELEASE_DATE, contentItem.getReleaseDate());
            values.put(ContentDBContract.ContentColumns.NAME, contentItem.getName());
            values.put(ContentDBContract.ContentColumns.TITLE, contentItem.getTitle());
            values.put(ContentDBContract.ContentColumns.OVERVIEW, contentItem.getOverview());
            values.put(ContentDBContract.ContentColumns.POSTER_PATH, contentItem.getPosterPath());
            values.put(ContentDBContract.ContentColumns.VOTE_AVERAGE, contentItem.getVoteAverage());

            long insert = dataHelper.insertContent(values);

            if (insert > 0) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite);
                isFavorite = true;
            }
        } else {
            DataHelper dataHelper = new DataHelper(FavoriteActivity.this);
            dataHelper.open();

            long delete = dataHelper.deleteContent(String.valueOf(contentItem.getId()));

            if (delete > 0) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_unfilled_2);
                isFavorite = false;
            }
        }
    }
}