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
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import io.coldfish.lettv.adapter.TVShowsAdapter;
import io.coldfish.lettv.model.TVShow;
import io.coldfish.lettv.rest.RestClient;
import io.coldfish.lettv.rest.TVShowsRepository;
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
        final LinearLayout noData = findViewById(R.id.noData);

        VMTVShows.Factory factory = new VMTVShows.Factory(new TVShowsRepository(RestClient.get()));
        final VMTVShows vmTVShows = ViewModelProviders.of(this, factory).get(VMTVShows.class);
        swipeRefreshLayout.setRefreshing(true);
        vmTVShows.callServiceForTVShows();

        final TVShowsAdapter adapter = new TVShowsAdapter(new ArrayList<TVShow>(0));
        adapter.setClickListener(this);
        tvShowListView.setAdapter(adapter);
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
                if (tvShowList != null && tvShowList.size() > 0) {
                    noData.setVisibility(View.GONE);
                    if (!isDestroyed()) {
                        adapter.setItems(tvShows);
                    }
                } else {
                    noData.setVisibility(View.VISIBLE);
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
