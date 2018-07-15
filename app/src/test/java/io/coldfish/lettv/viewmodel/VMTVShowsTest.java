package io.coldfish.lettv.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.coldfish.lettv.model.TVShow;
import io.coldfish.lettv.rest.TVShowsRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VMTVShowsTest {

    // Needed for lifecycle components, so that postValue is performed immediately
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    TVShowsRepository repo;
    @Mock
    Observer popularObserver;
    @Mock
    Observer similarObserver;

    VMTVShows tvShowsViewModel;
    MutableLiveData<List<TVShow>> success;
    MutableLiveData<List<TVShow>> error;
    TVShow show;

    @Before
    public void setupStatisticsViewModel() {
        MockitoAnnotations.initMocks(this);
        tvShowsViewModel = new VMTVShows(repo);

        // Success Object
        success = new MutableLiveData<>();
        List<TVShow> tvShows = new ArrayList<>();
        show = new TVShow(1401, "Title 1", "Title 1", "Overview 1", "1977-01-01", "/image1.png", "/image1.png", 100.0, 7.8, 100, "US", null, null);
        TVShow show2 = new TVShow(1402, "Title 1", "Title 1", "Overview 1", "1977-01-01", "/image1.png", "/image1.png", 100.0, 7.8, 100, "US", null, null);
        tvShows.add(show);
        tvShows.add(show2);
        success.postValue(tvShows);

        // Error Object
        error = new MutableLiveData<>();
        error.postValue(null);
    }

    @Test
    public void callServiceForTVShows_getResults() {
        when(repo.getPopularShows()).thenReturn(success);
        tvShowsViewModel.callServiceForTVShows();
        tvShowsViewModel.getPopularShowsData().observeForever(popularObserver);
        verify(popularObserver).onChanged(success.getValue());
    }

    @Test
    public void callServiceForTVShows_getError() {
        when(repo.getPopularShows()).thenReturn(error);
        tvShowsViewModel.callServiceForTVShows();
        tvShowsViewModel.getPopularShowsData().observeForever(popularObserver);
        verify(popularObserver).onChanged(null);
    }

    @Test
    public void callServiceForSimilarTVShows_getResults() {
        when(repo.getSimilarShows()).thenReturn(success);
        tvShowsViewModel.callServiceForSimilarTVShows(show);
        tvShowsViewModel.getSimilarShowsData().observeForever(similarObserver);
        verify(similarObserver).onChanged(success.getValue());
    }

    @Test
    public void callServiceForSimilarTVShows_getError() {
        when(repo.getSimilarShows()).thenReturn(error);
        tvShowsViewModel.callServiceForSimilarTVShows(show);
        tvShowsViewModel.getSimilarShowsData().observeForever(similarObserver);
        verify(similarObserver).onChanged(null);
    }

    @Test
    public void clearAndCallServiceForTVShows_getResults() {
        when(repo.getPopularShows()).thenReturn(success);
        tvShowsViewModel.clearAndCallServiceForTVShows();
        tvShowsViewModel.getPopularShowsData().observeForever(popularObserver);
        verify(popularObserver).onChanged(success.getValue());
    }

    @Test
    public void clearAndCallServiceForTVShows_getError() {
        when(repo.getPopularShows()).thenReturn(error);
        tvShowsViewModel.clearAndCallServiceForTVShows();
        tvShowsViewModel.getPopularShowsData().observeForever(popularObserver);
        verify(popularObserver).onChanged(null);
    }
}