package com.gzq.lib_core.http.cache;

import android.text.TextUtils;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.GlobalConfig;
import com.gzq.lib_core.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * 在此处实现数据库的缓存
 */
public class RoomCacheInterceptor implements Interceptor {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json;charset=UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        GlobalConfig globalConfig = App.getGlobalConfig();
        String roomHeader = request.header("Room-Cache");
        long roomCacheTime = TextUtils.isEmpty(roomHeader) ? globalConfig.getGlobalCacheSecond() * 1000 : Integer.parseInt(roomHeader) * 1000;
        String okhttpHeader = request.header("OkHttp-Cache");
        int okhttpCacheTime = TextUtils.isEmpty(okhttpHeader) ? globalConfig.getGlobalCacheSecond() : Integer.parseInt(okhttpHeader);
        String cacheMode = request.header("Cache-Mode");
        String globalCacheMode = TextUtils.isEmpty(cacheMode) ? globalConfig.getCacheMode() : cacheMode;
        boolean isRoomCache = globalConfig.isRoomCache();
        boolean isOkhttpCache = globalCacheMode.equals(CacheMode.DEFAULT) || !isRoomCache;
        if (NetworkUtils.isNetworkAvailable()) {
            //有网模式
            if (isOkhttpCache) {
                //DEFAULT模式
                return readOkhttpCache(chain.proceed(request), okhttpCacheTime);
            }
            if (globalCacheMode.equals(CacheMode.REQUEST_FAILED_READ_CACHE)) {
                //REQUEST_FAILED_READ_CACHE模式
                Response response = chain.proceed(request);
                if (response.code() != 200) {
                    //请求不成功
                    if (isOkhttpCache) {
                        return readOkhttpCacheOnly(chain, request);
                    }
                    if (isRoomCache) {
                        Response roomResponse = readRoomCache(true, false, chain, request, CacheMode.REQUEST_FAILED_READ_CACHE, roomCacheTime);
                        return roomResponse == null ? get400Response(request, globalCacheMode) : roomResponse;
                    }
                } else {
                    //请求成功写入数据库
                    return writeRoomCache(response);
                }
            }


            if (globalCacheMode.equals(CacheMode.IF_NONE_CACHE_REQUEST)) {
                //IF_NONE_CACHE_REQUEST模式
                if (isOkhttpCache) {
                    Timber.e("OkhttpCache暂时不支持这个模式");
                    return chain.proceed(request);
                }
                if (isRoomCache) {
                    return readRoomCache(true, true, chain, request, CacheMode.IF_NONE_CACHE_REQUEST, roomCacheTime);
                }
            }


            if (globalCacheMode.equals(CacheMode.FIRST_CACHE_THEN_REQUEST)) {
                //FIRST_CACHE_THEN_REQUEST模式
                if (isOkhttpCache) {
                    return readOkhttpCache(chain.proceed(request), okhttpCacheTime);
                }
                if (isRoomCache) {
                    Response response = chain.proceed(request);
                    Response roomResponse = readRoomCache(true, true, chain, request, CacheMode.FIRST_CACHE_THEN_REQUEST, roomCacheTime);
                    writeRoomCache(roomResponse.newBuilder().body(roomResponse.body()).build());
                    return roomResponse;
                }
            }
        } else {
            //无网模式
            if (isOkhttpCache) {
                return readOkhttpCacheOnly(chain, request);
            }
            if (isRoomCache) {
                Response roomResponse = readRoomCache(false, false, chain, request, globalCacheMode, roomCacheTime);
                return roomResponse == null ? get400Response(request, globalCacheMode) : roomResponse;
            }
        }
        return chain.proceed(request);
    }

    private Response readOkhttpCache(Response response, int maxAge) {
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build();
    }

    private Response readOkhttpCacheOnly(Chain chain, Request request) throws IOException {
        //读取缓存信息
        request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
        Response response = chain.proceed(request);
        Timber.i("on cache data:" + response.cacheResponse());
        //set cache times is 3 days
        int maxStale = 60 * 60 * 24 * 3;
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                .build();
    }

    private Response readRoomCache(boolean isNetOk, boolean isReturnOriginalResponse, Chain chain, Request request, String cacheMode, long time) throws IOException {
        //如果是缓存存在并且没有过期，则手动构建一个Response返回；否则，构建一个原生的
        String key = request.url().url().toString() + ">" + request.method();
        Timber.i("RoomCache-Key(get):" + key);
        RoomCacheDB roomCacheDB = Box.getCacheRoomDataBase(RoomCacheDB.class);
        RoomCacheEntity roomCacheEntity = roomCacheDB.roomCacheDao().queryByKey(key);
        if (roomCacheEntity == null)
            return isReturnOriginalResponse ? chain.proceed(request) : null;
        boolean isExpire = roomCacheEntity.checkExpire(cacheMode, time, System.currentTimeMillis());
        Timber.i(key + ">>>>>isExpire(" + isExpire + ")");
        Headers responseHeaders = roomCacheEntity.getResponseHeaders();
        if (isExpire) {
            if (isNetOk)
                return isReturnOriginalResponse ?chain.proceed(request) : null;
            return new Response.Builder()
                    .code(400)
                    .request(request)
                    .headers(responseHeaders)
                    .addHeader("Room-Cache-Control", cacheMode)
                    .sentRequestAtMillis(System.currentTimeMillis())
                    .receivedResponseAtMillis(System.currentTimeMillis() + 20)
                    .protocol(Protocol.get(roomCacheEntity.getProtocol()))
                    .message("")
                    .body(ResponseBody.create(MEDIA_TYPE, ""))
                    .build();
        } else {
            return new Response.Builder()
                    .code(200)
                    .request(request)
                    .headers(responseHeaders)
                    .addHeader("Room-Cache-Control", cacheMode)
                    .sentRequestAtMillis(System.currentTimeMillis())
                    .receivedResponseAtMillis(System.currentTimeMillis() + 20)
                    .protocol(Protocol.get(roomCacheEntity.getProtocol()))
                    .message("")
                    .body(ResponseBody.create(MEDIA_TYPE, roomCacheEntity.getData()))
                    .build();
        }
    }

    private Response writeRoomCache(final Response response) throws IOException {
        if (response.code() != 200) {
            return null;
        }
        //构造缓存实体并写入数据库
        String key = response.request().url().url().toString() + ">" + response.request().method();
        Timber.i("RoomCache-Key(put):" + key);
        String protocol = response.protocol().toString();
        RoomCacheDB roomCacheDB = Box.getCacheRoomDataBase(RoomCacheDB.class);
        RoomCacheEntity roomCacheEntity = new RoomCacheEntity();
        roomCacheEntity.setLocalExpire(System.currentTimeMillis());
        roomCacheEntity.setExpire(false);
        roomCacheEntity.setKey(key);
        String content = response.body().string();
        roomCacheEntity.setData(content);
        roomCacheEntity.setResponseHeaders(response.headers());
        roomCacheEntity.setProtocol(protocol);
        roomCacheDB.roomCacheDao().insertCache(roomCacheEntity);

        return response.newBuilder()
                .body(ResponseBody.create(MEDIA_TYPE, content))
                .build();
    }

    private Response get400Response(Request request, String cacheMode) throws IOException {
        return new Response.Builder()
                .code(400)
                .request(request)
                .addHeader("Room-Cache-Control", cacheMode)
                .sentRequestAtMillis(System.currentTimeMillis())
                .receivedResponseAtMillis(System.currentTimeMillis() + 20)
                .protocol(Protocol.get("http/1.1"))
                .message("")
                .body(ResponseBody.create(MEDIA_TYPE, ""))
                .build();
    }

}
