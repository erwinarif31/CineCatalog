package com.h071211059.h071211059_finalmobile.fragment;

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
import com.h071211059.h071211059_finalmobile.adapter.ContentAdapter;
import com.h071211059.h071211059_finalmobile.adapter.ContentPopularAdapter;
import com.h071211059.h071211059_finalmobile.databinding.FragmentTvBinding;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.model.DataResponse;
import com.h071211059.h071211059_finalmobile.network.ApiInstance;
import com.h071211059.h071211059_finalmobile.network.ApiInterface;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Retrofit;

public class TvFragment extends Fragment {
    private FragmentTvBinding binding;

    private static TvFragment instance;

    private static ApiInterface apiInterface;

    public static int currentPage = 1;

    private static int totalPage;

    private static ArrayList<ContentItem> popularTv;

    private Retrofit retrofit;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        retrofit = ApiInstance.getInstance();
        apiInterface = retrofit.create(ApiInterface.class);

        Call<DataResponse> clientAiringToday = apiInterface.getTvAiringToday(1, ApiInstance.API_KEY);
        Call<DataResponse> clientTopRated = apiInterface.getTvTopRated(1, ApiInstance.API_KEY);
        Call<DataResponse> clientOnTheAir = apiInterface.getTvOnTheAir(1, ApiInstance.API_KEY);

        getTvShow(binding.rvNowPlaying, binding.shimmerNowPlaying, clientOnTheAir);
        getTvShow(binding.rvTopRated, binding.shimmerTopRated, clientTopRated);
        getTvShow(binding.rvUpcoming, binding.shimmerUpcoming, clientAiringToday);

        binding.rvPopular.setLayoutManager(new GridLayoutManager(instance.getContext(), 3));

        popularTv = new ArrayList<>();
        ContentPopularAdapter adapter = new ContentPopularAdapter(popularTv);
        binding.rvPopular.setAdapter(adapter);

        getPopularTvShow(currentPage);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            getTvShow(binding.rvNowPlaying, binding.shimmerNowPlaying, clientOnTheAir);
            getTvShow(binding.rvTopRated, binding.shimmerTopRated, clientTopRated);
            getTvShow(binding.rvUpcoming, binding.shimmerUpcoming, clientAiringToday);

            currentPage = 1;
            popularTv.clear();
            getPopularTvShow(currentPage);
        });

        binding.svMovie.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (binding.svMovie.getChildAt(0).getBottom() <= (binding.svMovie.getHeight() + binding.svMovie.getScrollY())) {
                if (currentPage < totalPage) {
                    currentPage++;
                    getPopularTvShow(currentPage);
                }
            }
        });
    }

    private void getPopularTvShow(int currentPage) {
        Call<DataResponse> clientPopular = apiInterface.getTvPopular(currentPage, ApiInstance.API_KEY);
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
                        TvFragment.popularTv.addAll(data);

                        TvFragment.currentPage = dataResponse.getPage();
                        TvFragment.totalPage = dataResponse.getTotalPages();

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

    private void getTvShow(RecyclerView rvTvShow, ShimmerFrameLayout shimmer, Call<DataResponse> client) {
        shimmer.startShimmer();
        shimmer.setVisibility(View.VISIBLE);
        rvTvShow.setVisibility(View.GONE);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> client.clone().enqueue(new retrofit2.Callback<DataResponse>() {
            @Override
            public void onResponse(Call<DataResponse> call, retrofit2.Response<DataResponse> response) {
                if (response.isSuccessful()) {
                    Log.i("Api Response", "onResponse: Success");
                    if (response.body() != null) {
                        DataResponse dataResponse = response.body();
                        ArrayList<ContentItem> results = dataResponse.getResults();

                        handler.post(() -> {
                            rvTvShow.setLayoutManager(new LinearLayoutManager(instance.getContext(), LinearLayoutManager.HORIZONTAL, false));
                            ContentAdapter adapter = new ContentAdapter(results);
                            rvTvShow.setAdapter(adapter);

                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            rvTvShow.setVisibility(View.VISIBLE);

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
}