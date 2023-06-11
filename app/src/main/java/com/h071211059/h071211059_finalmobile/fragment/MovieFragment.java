package com.h071211059.h071211059_finalmobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.h071211059.h071211059_finalmobile.DetailActivity;
import com.h071211059.h071211059_finalmobile.adapter.ContentAdapter;
import com.h071211059.h071211059_finalmobile.adapter.ContentPopularAdapter;
import com.h071211059.h071211059_finalmobile.databinding.FragmentMovieBinding;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.model.DataResponse;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;
import com.h071211059.h071211059_finalmobile.network.ApiInterface;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MovieFragment extends Fragment {
    private FragmentMovieBinding binding;

    private static MovieFragment instance;
    private static ApiInterface apiInterface;
    public static int currentPage = 1;
    private static int totalPage;

    private static ArrayList<ContentItem> popularMovies = new ArrayList<>();
    private static ArrayList<ContentItem> nowPlayingMovies = new ArrayList<>();
    private static ArrayList<ContentItem> topRatedMovies = new ArrayList<>();
    private static ArrayList<ContentItem> upcomingMovies = new ArrayList<>();

    public MovieFragment() {
    }

    public static MovieFragment getInstance() {
        if (instance == null) {
            instance = new MovieFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofit = ApiInstance.getInstance();
        apiInterface = retrofit.create(ApiInterface.class);

        binding.rvNowPlaying.setLayoutManager(new LinearLayoutManager(instance.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ContentAdapter nowPlayingAdapter = new ContentAdapter(nowPlayingMovies);
        nowPlayingAdapter.setOnItemClickListener(contentItem -> detailIntent(contentItem));
        binding.rvNowPlaying.setAdapter(nowPlayingAdapter);

        Call<DataResponse> clientTopRated = apiInterface.getMovieTopRated(1, ApiInstance.API_KEY);
        getMovies(binding.rvTopRated, binding.shimmerTopRated, clientTopRated, nowPlayingAdapter);

        binding.rvTopRated.setLayoutManager(new LinearLayoutManager(instance.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ContentAdapter topRatedAdapter = new ContentAdapter(topRatedMovies);
        topRatedAdapter.setOnItemClickListener(contentItem -> detailIntent(contentItem));
        binding.rvTopRated.setAdapter(topRatedAdapter);

        Call<DataResponse> clientUpcoming = apiInterface.getMovieUpcoming(1, ApiInstance.API_KEY);
        getMovies(binding.rvUpcoming, binding.shimmerUpcoming, clientUpcoming, topRatedAdapter);

        binding.rvUpcoming.setLayoutManager(new LinearLayoutManager(instance.getContext(), LinearLayoutManager.HORIZONTAL, false));
        ContentAdapter upcomingAdapter = new ContentAdapter(upcomingMovies);
        upcomingAdapter.setOnItemClickListener(contentItem -> detailIntent(contentItem));
        binding.rvUpcoming.setAdapter(upcomingAdapter);

        Call<DataResponse> clientNowPlaying = apiInterface.getMovieNowPlaying(1, ApiInstance.API_KEY);
        getMovies(binding.rvNowPlaying, binding.shimmerNowPlaying, clientNowPlaying, upcomingAdapter);

        getPopular();

        binding.swipeRefreshLayout.setOnRefreshListener(() -> refreshContent(clientTopRated, clientUpcoming, clientNowPlaying, nowPlayingAdapter, topRatedAdapter, upcomingAdapter));

        binding.svMovie.getViewTreeObserver().addOnScrollChangedListener(this::loadMore);
    }

    private void getPopular() {
        binding.rvPopular.setLayoutManager(new GridLayoutManager(instance.getContext(), 3));
        ContentPopularAdapter adapter = new ContentPopularAdapter(popularMovies);
        adapter.setOnItemClickListener(contentItem -> detailIntent(contentItem));
        binding.rvPopular.setAdapter(adapter);
        getPopularMovies(currentPage);
    }

    private void loadMore() {
        if (binding.svMovie.getChildAt(0).getBottom() <= (binding.svMovie.getHeight() + binding.svMovie.getScrollY())) {
            if (currentPage < totalPage) {
                System.out.println("Load More");
                currentPage++;
                getPopularMovies(currentPage);
            }
        }
    }

    private void refreshContent(Call<DataResponse> clientTopRated, Call<DataResponse> clientUpcoming, Call<DataResponse> clientNowPlaying, ContentAdapter nowPlayingAdapter, ContentAdapter topRatedAdapter, ContentAdapter upcomingAdapter) {
        getMovies(binding.rvNowPlaying, binding.shimmerNowPlaying, clientNowPlaying, nowPlayingAdapter);
        getMovies(binding.rvTopRated, binding.shimmerTopRated, clientTopRated, topRatedAdapter);
        getMovies(binding.rvUpcoming, binding.shimmerUpcoming, clientUpcoming, upcomingAdapter);

        currentPage = 1;
        popularMovies.clear();
        getPopularMovies(currentPage);
    }

    private void getPopularMovies(int currentPage) {
        Call<DataResponse> clientPopular = apiInterface.getMoviePopular(currentPage, ApiInstance.API_KEY);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> clientPopular.clone().enqueue(new retrofit2.Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
                if (response.isSuccessful()) {
                    Log.i("Api Response", "onResponse: Success");
                    if (response.body() != null) {
                        DataResponse dataResponse = response.body();
                        ArrayList<ContentItem> data = dataResponse.getResults();
                        MovieFragment.popularMovies.addAll(data);

                        MovieFragment.currentPage = dataResponse.getPage();
                        MovieFragment.totalPage = dataResponse.getTotalPages();

                        handler.post(() -> {
                            binding.rvPopular.getAdapter().notifyDataSetChanged();
                            binding.shimmerPopular.stopShimmer();
                            binding.shimmerPopular.setVisibility(View.GONE);

                            binding.swipeRefreshLayout.setRefreshing(false);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        }));
    }

    private static void getMovies(RecyclerView rvMovie, ShimmerFrameLayout shimmer, Call client, ContentAdapter adapter) {
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        rvMovie.setVisibility(View.GONE);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> client.clone().enqueue(new retrofit2.Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
                if (response.isSuccessful()) {
                    Log.i("Api Response", "onResponse: Success");
                    if (response.body() != null) {
                        DataResponse dataResponse = response.body();
                        adapter.setContentItems(dataResponse.getResults());

                        handler.post(() -> {
                            adapter.notifyDataSetChanged();
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            rvMovie.setVisibility(View.VISIBLE);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        }));
    }

    private void detailIntent(ContentItem contentItem) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, contentItem.getId());
        intent.putExtra(DetailActivity.EXTRA_TYPE, DetailActivity.MOVIE_TYPE);
        startActivity(intent);
    }
}