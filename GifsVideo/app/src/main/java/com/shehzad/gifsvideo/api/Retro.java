package com.shehzad.gifsvideo.api;

import android.util.Base64;

import com.shehzad.gifsvideo.BuildConfig;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
    //authentication
    public static final String AUTH = "Basic " + Base64.encodeToString((BuildConfig.API_KEY).getBytes(), Base64.NO_WRAP);

    //retro instance created only once
    private static Retro retroInstance = null;
    private String base_url = BuildConfig.BASE_URL;
    private Api api;

    private Retro() {

        //Authorization header for accessing the api
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request orginal = chain.request();

                                Request.Builder requestBuilder = orginal.newBuilder()
                                        .addHeader("Authorization",AUTH)
                                        .method(orginal.method(), orginal.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }
                ).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        api = retrofit.create(Api.class);
    }

    public static synchronized Retro getRetroInstance(){
        if (retroInstance == null){
            retroInstance = new Retro();
        }
        return retroInstance;
    }
    public Api getApi(){
        return api;
    }
}
