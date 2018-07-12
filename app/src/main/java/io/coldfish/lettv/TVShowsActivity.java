package io.coldfish.lettv;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.coldfish.lettv.adapter.TVShowsAdapter;
import io.coldfish.lettv.model.TVShow;
import io.coldfish.lettv.utils.LoadMoreListener;
import io.coldfish.lettv.utils.VerticalSpaceItemDecoration;
import io.coldfish.lettv.viewmodel.VMTVShows;

public class TVShowsActivity extends AppCompatActivity implements TVShowsAdapter.ItemClickListener {

    private List<TVShow> tvShowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshows);

        final RecyclerView tvShowListView = findViewById(R.id.tv_shows_list);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.pull_down_to_refresh);
        
        final VMTVShows vmTVShows = ViewModelProviders.of(this).get(VMTVShows.class);
        swipeRefreshLayout.setRefreshing(true);
        vmTVShows.callServiceForTVShows();
        
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        tvShowListView.setLayoutManager(layoutManager);
        tvShowListView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        tvShowListView.addOnScrollListener(new LoadMoreListener(layoutManager) {
            @Override
            public void onLoadMore() {
                vmTVShows.callServiceForTVShows();
            }
        });

        vmTVShows.getPopularShowsData().observe(this, new Observer<List<TVShow>>() {
            @Override
            public void onChanged(@Nullable List<TVShow> tvShows) {
                tvShowList = tvShows;
                if (!isDestroyed()) {
                    TVShowsAdapter adapter = new TVShowsAdapter(tvShows);
                    tvShowListView.setAdapter(adapter);
                    adapter.setClickListener(TVShowsActivity.this);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vmTVShows.clearAndCallServiceForTVShows();
                swipeRefreshLayout.setRefreshing(true);
            }
        });

    }

    @Override
    public void onClick(View view, int position) {
        final TVShow tvshow = tvShowList.get(position);
        Intent intent = new Intent(this, TVShowDetailsActivity.class);
        intent.putExtra("tv_show", tvshow);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_right);
    }
}
