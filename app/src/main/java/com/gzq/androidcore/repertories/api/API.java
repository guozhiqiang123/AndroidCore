package com.gzq.androidcore.repertories.api;

import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface API {
    @GET("test")
    Observable<HttpResult<List<Object>>> getDatas();
}
