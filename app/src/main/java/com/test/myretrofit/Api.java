package com.test.myretrofit;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("/repos/{owner}/{repo}/contributors")
    Call<ResponseBody> contributors(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<ResultBean>> contributorsRx(@Path("owner") String owner, @Path("repo") String repo);

}
