package com.gzq.androidcore.repertories;

import com.gzq.androidcore.repertories.api.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;

public class AppRepertory {

    public static Observable<List<Object>> getDataFromNet() {
        return Box.getRetrofit(API.class).getDatas().compose(RxUtils.httpResponseTransformer());
    }
}
