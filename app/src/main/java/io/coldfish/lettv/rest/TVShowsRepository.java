package io.coldfish.lettv.rest;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import io.coldfish.lettv.model.Response;
import io.coldfish.lettv.model.TVShow;
import retrofit2.Call;
import retrofit2.Callback;

public class TVShowsRepository {

    @NonNull
    private final LetTvAPI service;

    private MutableLiveData<Integer> popularShowsPageNumber;
    private MutableLiveData<List<TVShow>> popularShowsData;
    private MutableLiveData<Integer> similarShowsPageNumber;
    private MutableLiveData<List<TVShow>> similarShowsData;

    public TVShowsRepository(@NonNull LetTvAPI service) {
        this.service = service;
        popularShowsPageNumber = new MutableLiveData<>();
        popularShowsData = new MutableLiveData<>();
        similarShowsPageNumber = new MutableLiveData<>();
        similarShowsData = new MutableLiveData<>();
    }

    @NonNull
    public MutableLiveData<List<TVShow>> popularTVShows() {
        service.getPopularTVShows(popularShowsPageNumber.getValue() != null ? popularShowsPageNumber.getValue() + 1 : 1, Locale.getDefault().toString()).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                popularShowsPageNumber.setValue(response.body().getPage());
                if (popularShowsData.getValue() == null)
                    popularShowsData.setValue(response.body().getResults());
                else {
                    List<TVShow> value = popularShowsData.getValue();
                    value.addAll(response.body().getResults());
                    popularShowsData.setValue(value);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                popularShowsData.setValue(null);
            }
        });
        return popularShowsData;
    }

    public MutableLiveData<List<TVShow>> similarTVShows(final TVShow selectedTVShow) {
        service.getSimilarTVShows(selectedTVShow.getId(), similarShowsPageNumber.getValue() != null ? similarShowsPageNumber.getValue() + 1 : 1, Locale.getDefault().toString()).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                similarShowsPageNumber.setValue(response.body().getPage());
                if (similarShowsData.getValue() == null) {
                    List<TVShow> results = response.body().getResults();
                    results.add(0, selectedTVShow);
                    similarShowsData.setValue(results);
                } else {
                    List<TVShow> value = similarShowsData.getValue();
                    value.addAll(response.body().getResults());
                    similarShowsData.setValue(value);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                similarShowsData.setValue(null);
            }
        });
        return similarShowsData;
    }

    public void clearData() {
        popularShowsPageNumber.setValue(null);
        popularShowsData.setValue(null);
    }

    public MutableLiveData<List<TVShow>> getSimilarShows() {
        return similarShowsData;
    }

    public MutableLiveData<List<TVShow>> getPopularShows() {
        return popularShowsData;
    }
}
