package com.h071211059.h071211059_finalmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.h071211059.h071211059_finalmobile.adapter.CastAdapter;
import com.h071211059.h071211059_finalmobile.adapter.GenreAdapter;
import com.h071211059.h071211059_finalmobile.databinding.ActivityDetailBinding;
import com.h071211059.h071211059_finalmobile.db.ContentDBContract;
import com.h071211059.h071211059_finalmobile.db.ContentGenreContract;
import com.h071211059.h071211059_finalmobile.db.DataHelper;
import com.h071211059.h071211059_finalmobile.model.CastResponse;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.model.Genre;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;
import com.h071211059.h071211059_finalmobile.network.ApiInterface;
import com.h071211059.h071211059_finalmobile.util.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";

    public static final String EXTRA_TYPE = "type";

    public static final boolean EXTRA_IS_FAVORITE = false;

    public static final String TV_TYPE = "tv";

    public static final String MOVIE_TYPE = "movie";

    private ActivityDetailBinding binding;

    private boolean overviewExpanded = false;

    private Retrofit retrofit;

    private ApiInterface apiInterface;

    private Call<ContentItem> client;

    private Call<CastResponse> castClient;

    private ContentItem contentItem;

    private int id;

    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        id = getIntent().getIntExtra(EXTRA_ID, 0);
        String type = getIntent().getStringExtra(EXTRA_TYPE);

        retrofit = ApiInstance.getInstance();
        apiInterface = retrofit.create(ApiInterface.class);

        switch (type) {
            case TV_TYPE:
                client = apiInterface.getTVDetail(id, ApiInstance.API_KEY);
                castClient = apiInterface.getTVCasts(id, ApiInstance.API_KEY);
                binding.ivType.setImageResource(R.drawable.ic_twotone_tv);
                break;
            case MOVIE_TYPE:
                client = apiInterface.getMovieDetail(id, ApiInstance.API_KEY);
                castClient = apiInterface.getMovieCast(id, ApiInstance.API_KEY);
                break;
        }

        getContentDetails(client);
        getCasts(castClient);

        checkFavorites();

        binding.clToggle.setOnClickListener(v -> expandOverview());
        binding.ivBack.setOnClickListener(v -> onBackPressed());
        binding.ivFavorite.setOnClickListener(v -> addToFavorites());
    }


    private void addToFavorites() {
        if (!isFavorite) {
            DataHelper dataHelper = new DataHelper(DetailActivity.this);
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

            ContentValues genreValues = new ContentValues();

            for (Genre genre : contentItem.getGenres()) {
                genreValues.put(ContentGenreContract.ContentGenreColumns.CONTENT_ID, contentItem.getId());
                genreValues.put(ContentGenreContract.ContentGenreColumns.GENRE_ID, genre.getId());
                long genreInsert = dataHelper.insertContentGenre(genreValues);
            }

            if (insert > 0) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite);
                isFavorite = true;
            }
        } else {
            DataHelper dataHelper = new DataHelper(DetailActivity.this);
            dataHelper.open();

            long delete = dataHelper.deleteContent(String.valueOf(contentItem.getId()));

            if (delete > 0) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_unfilled_2);
                isFavorite = false;
            }
        }
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

    private void getCasts(Call<CastResponse> castClient) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> castClient.enqueue(new retrofit2.Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, retrofit2.Response<CastResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        CastResponse castResponse = response.body();
                        System.out.println(castResponse.getCasts().size());
                        handler.post(() -> {
                            binding.rvCast.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            binding.rvCast.setAdapter(new CastAdapter(castResponse.getCasts()));
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                Log.e("DetailActivity", "onFailure: " + t.getMessage());
            }
        }));
    }

    private void getContentDetails(Call<ContentItem> client) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> client.enqueue(new retrofit2.Callback<ContentItem>() {
            @Override
            public void onResponse(Call<ContentItem> call, retrofit2.Response<ContentItem> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        contentItem = response.body();
                        handler.post(() -> {
                            if (contentItem.getTitle() != null) {
                                binding.tvTitle.setText(contentItem.getTitle());
                            } else {
                                binding.tvTitle.setText(contentItem.getName());
                            }
                            binding.rbRating.setRating(Float.valueOf(contentItem.getVoteAverage()) / 2);

                            Glide.with(binding.getRoot()).load(ApiInstance.IMAGE_BASE_URL + contentItem.getPosterPath()).into(binding.ivImage);
                            Glide.with(binding.getRoot()).load(ApiInstance.IMAGE_BASE_URL + contentItem.getBackdropPath()).into(binding.ivBanner);
                            binding.ivBanner.setColorFilter(Color.parseColor("#80000000"));
                            binding.rbRating.setRating(Float.valueOf(contentItem.getVoteAverage()) / 2);

                            System.out.println("Overview : " + contentItem.getOverview());
                            if (contentItem.getOverview().equals("")) {
                                binding.tvOverview.setText("No overview available");
                                binding.clToggle.setVisibility(View.GONE);
                            } else {
                                binding.tvOverview.setText(contentItem.getOverview());
                            }

                            binding.rvGenre.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            binding.rvGenre.setAdapter(new GenreAdapter(contentItem.getGenres()));
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ContentItem> call, Throwable t) {
                Log.e("error", "onFailure: Failed");
            }
        }));
    }

    private void checkFavorites() {
        new LoadContentAsync(this, ids -> {
            if (ids.contains(id)) {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite);
                isFavorite = true;
            } else {
                binding.ivFavorite.setImageResource(R.drawable.ic_favorite_unfilled_2);
                isFavorite = false;
            }
        }).getFavoritesId();
    }

    private static class LoadContentAsync {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadContentCallback> weakCallback;

        private LoadContentAsync(Context context, LoadContentCallback callback) {
            this.weakContext = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(callback);
        }

        void getFavoritesId() {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executorService.execute(() -> {
                DataHelper dataHelper = new DataHelper(weakContext.get());
                dataHelper.open();
                Cursor cursor = dataHelper.queryAllFavoritesId();
                ArrayList<Integer> ids = MappingHelper.mapCursorToArrayIds(cursor);
                handler.post(() -> weakCallback.get().postExecuteId(ids));
            });
        }

    }

    interface LoadContentCallback {
        void postExecuteId(ArrayList<Integer> ids);
    }

}