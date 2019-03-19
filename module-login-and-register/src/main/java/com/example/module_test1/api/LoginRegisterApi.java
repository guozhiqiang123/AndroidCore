package com.example.module_test1.api;

import com.gzq.lib_core.http.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginRegisterApi {
    /**
     * 监护人登录
     *
     * @param userName
     * @param password
     * @return
     */
    @POST("ZZB/api/guardian/login/")
    Observable<HttpResult<Object>> login(
            @Query("userName") String userName,
            @Query("password") String password
    );
}
