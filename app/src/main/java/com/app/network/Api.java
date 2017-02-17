package com.app.network;


import com.app.model.NetImage;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface Api {

    @GET("/api/data/福利/10/{page}")
    Observable<NetImage> getNetImage(@Path("page") int page);
}
