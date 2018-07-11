package io.coldfish.lettv.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;
import java.util.Locale;

import io.coldfish.lettv.model.Response;
import io.coldfish.lettv.model.TVShow;
import io.coldfish.lettv.rest.RestClient;
import retrofit2.Call;
import retrofit2.Callback;

public class VMTVShows extends ViewModel {

    private MutableLiveData<Integer> popularShowsPageNumber = new MutableLiveData<>();
    private MutableLiveData<List<TVShow>> popularShowsData = new MutableLiveData<>();
    private MutableLiveData<Integer> similarShowsPageNumber = new MutableLiveData<>();
    private MutableLiveData<List<TVShow>> similarShowsData = new MutableLiveData<>();

    public void callServiceForTVShows() {
        RestClient.get().getPopularTVShows(popularShowsPageNumber.getValue() != null ? popularShowsPageNumber.getValue() + 1 : 1, Locale.getDefault().toString()).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                popularShowsPageNumber.setValue(response.body().getPage());
                if (popularShowsData.getValue() == null)
                    popularShowsData.setValue(response.body().getResults());
                else
                    (popularShowsData.getValue()).addAll(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    public void callServiceForSimilarTVShows(final TVShow selectedTVShow) {
        RestClient.get().getSimilarTVShows(selectedTVShow.getId(), similarShowsPageNumber.getValue() != null ? similarShowsPageNumber.getValue() + 1 : 1, Locale.getDefault().toString()).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                similarShowsPageNumber.setValue(response.body().getPage());
                if (similarShowsData.getValue() == null) {
                    List<TVShow> results = response.body().getResults();
                    results.add(0, selectedTVShow);
                    similarShowsData.setValue(results);
                } else
                    (similarShowsData.getValue()).addAll(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    public void clearAndCallServiceForTVShows() {
        popularShowsPageNumber.setValue(null);
        popularShowsData.setValue(null);
        callServiceForTVShows();
    }

    public MutableLiveData<List<TVShow>> getPopularShowsData() {
        return popularShowsData;
    }

    public MutableLiveData<List<TVShow>> getSimilarShowsData() {
        return similarShowsData;
    }
}
