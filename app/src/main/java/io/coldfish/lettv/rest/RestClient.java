package io.coldfish.lettv.rest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.coldfish.lettv.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    public static final String ROOT = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private static LetTvAPI REST_CLIENT;

    public static LetTvAPI get() {
        if (REST_CLIENT == null)
            setupRestClient();
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        REST_CLIENT = retrofit.create(LetTvAPI.class);
    }

}
