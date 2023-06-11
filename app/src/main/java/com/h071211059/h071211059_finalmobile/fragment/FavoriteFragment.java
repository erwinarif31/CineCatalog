package com.h071211059.h071211059_finalmobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h071211059.h071211059_finalmobile.FavoriteActivity;
import com.h071211059.h071211059_finalmobile.adapter.FavoriteAdapter;
import com.h071211059.h071211059_finalmobile.databinding.FragmentFavoriteBinding;
import com.h071211059.h071211059_finalmobile.db.DBHelper;
import com.h071211059.h071211059_finalmobile.db.DataHelper;
import com.h071211059.h071211059_finalmobile.model.ContentItem;
import com.h071211059.h071211059_finalmobile.util.MappingHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteFragment extends Fragment {
    private FragmentFavoriteBinding binding;


    private static FavoriteFragment instance;

    private FavoriteFragment() {}

    public static FavoriteFragment getInstance() {
        if (instance == null) {
            instance = new FavoriteFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getFavorites();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavorites();
    }

    private void getFavorites() {
        new LoadContentAsync(getContext(), contentItems -> {
            if (contentItems.size() > 0) {
                setRecyclerView(contentItems);
            }
        }).execute();
    }

    private void setRecyclerView(ArrayList<ContentItem> contentItems) {
        FavoriteAdapter adapter = new FavoriteAdapter(contentItems);
        adapter.setOnItemClickListener(contentItem -> {
            favoriteIntent(contentItem);
        });
        binding.rvFavorites.setAdapter(adapter);
    }

    private void favoriteIntent(ContentItem contentItem) {
        Intent intent = new Intent(instance.getContext(), FavoriteActivity.class);
        intent.putExtra(FavoriteActivity.EXTRA_CONTENT, contentItem);
        instance.startActivity(intent);
    }

    private static class LoadContentAsync {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadContentCallback> weakCallback;

        private LoadContentAsync(Context context, LoadContentCallback callback) {
            this.weakContext = new WeakReference<>(context);
            this.weakCallback = new WeakReference<>(callback);
        }

        void execute() {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                DataHelper dataHelper = DataHelper.getInstance(weakContext.get());
                dataHelper.open();

                Cursor cursor = dataHelper.queryAllFavoritesContent();
                ArrayList<ContentItem> contentItems = MappingHelper.mapCursorToArrayList(cursor);

                handler.post(() -> weakCallback.get().postExecute(contentItems));
            });
        }
    }

    interface LoadContentCallback {
        void postExecute(ArrayList<ContentItem> contentItems);
    }
}