package com.shehzad.gifsvideo.api;

import com.shehzad.gifsvideo.model.GifsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    //funny
    @GET("funny")
    Call<List<GifsModel>> getHome(@Query("_random") int random);

    @GET("moreVideos")
    Call<List<GifsModel>> getMoreVideos(@Query("_random") int random);

}
