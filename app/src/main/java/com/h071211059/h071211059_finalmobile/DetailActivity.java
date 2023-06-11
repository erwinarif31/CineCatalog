package com.h071211059.h071211059_finalmobile;

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
import com.h071211059.h071211059_finalmobile.model.CastResponse;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;
import com.h071211059.h071211059_finalmobile.network.ApiInterface;

import java.util.Objects;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int id = getIntent().getIntExtra(EXTRA_ID, 0);
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

        binding.clToggle.setOnClickListener(v -> expandOverview());
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
                        ContentItem contentItem = response.body();
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
}