package io.coldfish.lettv.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import io.coldfish.lettv.model.TVShow;
import io.coldfish.lettv.rest.TVShowsRepository;

public class VMTVShows extends ViewModel {

    @NonNull
    private final TVShowsRepository tvShowsRepository;

    public VMTVShows(@NonNull TVShowsRepository tvShowsRepository) {
        this.tvShowsRepository = tvShowsRepository;
    }

    public LiveData<List<TVShow>> callServiceForTVShows() {
        return tvShowsRepository.popularTVShows();
    }

    public LiveData<List<TVShow>> callServiceForSimilarTVShows(final TVShow selectedTVShow) {
        return tvShowsRepository.similarTVShows(selectedTVShow);
    }

    public void clearAndCallServiceForTVShows() {
        tvShowsRepository.clearData();
        callServiceForTVShows();
    }

    public MutableLiveData<List<TVShow>> getPopularShowsData() {
        return tvShowsRepository.getPopularShows();
    }

    public MutableLiveData<List<TVShow>> getSimilarShowsData() {
        return tvShowsRepository.getSimilarShows();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final TVShowsRepository repository;

        public Factory(TVShowsRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new VMTVShows(repository);
        }
    }
}
