package io.coldfish.lettv.rest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

public class LetTvAPITest {

    private LetTvAPI service;
    private String bodyJson = "{\"page\":1, \"total_pages\":1000, \"total_results\": 20073, \"results\":[{\"original_name\":\"The Big Bang Theory\",\"genre_ids\":[35],\"name\":\"The Big Bang Theory\",\"popularity\":266.263,\"origin_country\":[\"US\"],\"vote_count\":3039,\"first_air_date\":\"2007-09-24\",\"backdrop_path\":\"\\/nGsNruW3W27V6r4gkyc3iiEGsKR.jpg\",\"original_language\":\"en\",\"id\":1418,\"vote_average\":6.8,\"overview\":\"The Big Bang Theory is centered on five characters living in Pasadena, California: roommates Leonard Hofstadter and Sheldon Cooper; Penny, a waitress and aspiring actress who lives across the hall; and Leonard and Sheldon's equally geeky and socially awkward friends and co-workers, mechanical engineer Howard Wolowitz and astrophysicist Raj Koothrappali. The geekiness and intellect of the four guys is contrasted for comic effect with Penny's social skills and common sense.\",\"poster_path\":\"\\/ooBGRQBdbGzBxAVfExiO8r7kloA.jpg\"},{\"original_name\":\"The 100\",\"genre_ids\":[18,10765],\"name\":\"The 100\",\"popularity\":129.367,\"origin_country\":[\"US\"],\"vote_count\":1192,\"first_air_date\":\"2014-03-19\",\"backdrop_path\":\"\\/qYTIuJJ7fIehicAt3bl0vW70Sq6.jpg\",\"original_language\":\"en\",\"id\":48866,\"vote_average\":6.4,\"overview\":\"Based on the books by Kass Morgan, this show takes place 100 years in the future, when the Earth has been abandoned due to radioactivity. The last surviving humans live on an ark orbiting the planet — but the ark won't last forever. So the repressive regime picks 100 expendable juvenile delinquents to send down to Earth to see if the planet is still habitable.\",\"poster_path\":\"\\/fjJAcCmeSRQIpFttRX6NGdjnTIh.jpg\"}]}";
    private MockWebServer mockWebServer;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("api_key", "9c65daa547a3969f60e574e334efcb07").build();

                Request.Builder requestBuilder = original.newBuilder().url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mockWebServer.url(""))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        service = retrofit.create(LetTvAPI.class);
        mockWebServer.enqueue(new MockResponse().setBody(bodyJson).setBodyDelay(2, TimeUnit.SECONDS));
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getPopularTVShows() throws IOException {
        Call<io.coldfish.lettv.model.Response> call = service.getPopularTVShows(1, "en_US");
        retrofit2.Response<io.coldfish.lettv.model.Response> execute = call.execute();
        System.out.println(execute.body());
        assertNotNull(execute);
        assertNotNull(execute.body());
        assertFalse(execute.body().getResults().isEmpty());
        assertEquals(execute.body().getPage(), 1);
        assertEquals(execute.body().getResults().size(), 2);
        assertNotNull(execute.body().getResults().get(0).getName());
    }

    @Test
    public void getSimilarTVShows() throws IOException {
        Call<io.coldfish.lettv.model.Response> call = service.getSimilarTVShows(1418, 1, "en_US");
        retrofit2.Response<io.coldfish.lettv.model.Response> execute = call.execute();
        assertNotNull(execute);
        assertNotNull(execute.body());
        assertFalse(execute.body().getResults().isEmpty());
        assertEquals(execute.body().getPage(), 1);
        assertEquals(execute.body().getResults().size(), 2);
        assertNotNull(execute.body().getResults().get(0).getName());
    }

}