package com.shehzad.gifsvideo.api;

import com.shehzad.gifsvideo.model.GifsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    //funny videos
    @GET("funny")
    Call<List<GifsModel>> getFunny(@Query("_random") int random);

}
