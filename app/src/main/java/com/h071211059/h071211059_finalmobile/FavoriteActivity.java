package com.h071211059.h071211059_finalmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.h071211059.h071211059_finalmobile.adapter.CastAdapter;
import com.h071211059.h071211059_finalmobile.adapter.GenreAdapter;
import com.h071211059.h071211059_finalmobile.databinding.ActivityFavoriteBinding;
import com.h071211059.h071211059_finalmobile.db.ContentDBContract;
import com.h071211059.h071211059_finalmobile.db.DataHelper;
import com.h071211059.h071211059_finalmobile.model.Cast;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.model.Genre;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;
import com.h071211059.h071211059_finalmobile.util.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        getContentGenres();
        getContentCast();

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
            dataHelper.deleteContentGenre(String.valueOf(contentItem.getId()));
            dataHelper.deleteCast(String.valueOf(contentItem.getId()));

            if (delete > 0) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_unfilled_2);
                isFavorite = false;
                finish();
            }
        }
    }

    private void getContentGenres() {
        new LoadGenreAsync(this, new LoadGenreCallback() {
            @Override
            public void postExecute(ArrayList<Genre> genres) {
                binding.rvGenre.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this, LinearLayoutManager.HORIZONTAL, false));
                GenreAdapter genreAdapter = new GenreAdapter(genres);
                binding.rvGenre.setAdapter(genreAdapter);
            }
        }).execute(contentItem.getId());
    }

    private void getContentCast() {
        new LoadCastAsync(this, new LoadCastCallback() {
            @Override
            public void postExecute(ArrayList<Cast> casts) {
                binding.rvCast.setLayoutManager(new LinearLayoutManager(FavoriteActivity.this, LinearLayoutManager.HORIZONTAL, false));
                CastAdapter castAdapter = new CastAdapter(casts);
                binding.rvCast.setAdapter(castAdapter);
            }
        }).execute(contentItem.getId());
    }


    private static class LoadGenreAsync {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadGenreCallback> weakCallback;

        private LoadGenreAsync(Context context, LoadGenreCallback callback) {
            this.weakContext = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(callback);
        }

        void execute(int id) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executorService.execute(() -> {
                DataHelper dataHelper = new DataHelper(weakContext.get());
                dataHelper.open();

                Cursor cursor = dataHelper.queryGenreOfContent(String.valueOf(id));
                ArrayList<Genre> genres = MappingHelper.mapCursorToGenreArrayList(cursor);

                handler.post(() -> weakCallback.get().postExecute(genres));
            });
        }
    }

    private static class LoadCastAsync {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCastCallback> weakCallback;

        private LoadCastAsync(Context context, LoadCastCallback callback) {
            this.weakContext = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(callback);
        }

        void execute(int id) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executorService.execute(() -> {
                DataHelper dataHelper = new DataHelper(weakContext.get());
                dataHelper.open();

                Cursor cursor = dataHelper.queryCastOf(String.valueOf(id));
                ArrayList<Cast> casts = MappingHelper.mapCursorToArrayCast(cursor);

                handler.post(() -> weakCallback.get().postExecute(casts));
            });
        }
    }
    interface LoadGenreCallback {
        void postExecute(ArrayList<Genre> genres);
    }

    interface LoadCastCallback {
        void postExecute(ArrayList<Cast> casts);
    }
}