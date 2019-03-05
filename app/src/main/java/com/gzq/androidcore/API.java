package com.gzq.androidcore;

import com.gzq.lib_core.http.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface API {
    @GET("/xxxx/xxx/xxxx")
    Observable<HttpResult<Object>> test();
}
