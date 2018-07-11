package io.coldfish.lettv.rest;

import io.coldfish.lettv.model.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LetTvAPI {

    @GET("tv/popular")
    Call<Response> getPopularTVShows(@Query("page") int page, @Query("language") String language);

    @GET("tv/{tv_id}/similar")
    Call<Response> getSimilarTVShows(@Path("tv_id") int tvId, @Query("page") int page, @Query("language") String language);
}
