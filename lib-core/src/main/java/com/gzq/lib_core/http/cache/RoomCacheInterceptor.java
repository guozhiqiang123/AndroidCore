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
            //OkHttp缓存
            if (isOkhttpCache) {
                //DEFAULT模式
                return readOkhttpCache(chain.proceed(request), okhttpCacheTime);
            }
            //既非okhttp缓存，也不是room缓存
            if (!isRoomCache) {
                return chain.proceed(request);
            }
            //room缓存
            if (globalCacheMode.equals(CacheMode.REQUEST_FAILED_READ_CACHE)) {
                //REQUEST_FAILED_READ_CACHE模式
                Response response = chain.proceed(request);
                if (response.code() != 200) {
                    return readRoomCacheWithRequestFailedReadCache(true, response, roomCacheTime);
                } else {
                    //请求成功写入数据库
                    return writeRoomCache(response);
                }
            }

            if (globalCacheMode.equals(CacheMode.IF_NONE_CACHE_REQUEST)) {
                //IF_NONE_CACHE_REQUEST模式
                return readRoomCacheWithIfNoneCacheRequest(true, chain, request, roomCacheTime);
            }

            if (globalCacheMode.equals(CacheMode.FIRST_CACHE_THEN_REQUEST)) {
                //FIRST_CACHE_THEN_REQUEST模式
                Response response = chain.proceed(request);
                try {
                    Response roomResponse = readRoomCacheWithFirstCacheThenRequest(true, response, roomCacheTime);
                    return roomResponse == null ? get400Response(request, CacheMode.FIRST_CACHE_THEN_REQUEST) : roomResponse;
                } finally {
                    writeRoomCache(response.newBuilder().body(response.body()).build());
                }
            }
        } else {
            //无网模式
            if (isOkhttpCache) {
                return readOkhttpCacheOnly(chain, request);
            }
            if (!isRoomCache) {
                return chain.proceed(request);
            }
            //room缓存
            if (globalCacheMode.equals(CacheMode.REQUEST_FAILED_READ_CACHE)) {
                //REQUEST_FAILED_READ_CACHE模式
                Response response = readRoomCacheWithRequestFailedReadCacheNotNet(false, request, roomCacheTime);
                return response == null ? get400Response(request, CacheMode.REQUEST_FAILED_READ_CACHE) : response;
            }
            //TODO:这之后的代码还需要梳理
            if (globalCacheMode.equals(CacheMode.IF_NONE_CACHE_REQUEST)) {
                //IF_NONE_CACHE_REQUEST模式
                return readRoomCacheWithIfNoneCacheRequest(false, chain, request, roomCacheTime);
            }

            if (globalCacheMode.equals(CacheMode.FIRST_CACHE_THEN_REQUEST)) {
                //FIRST_CACHE_THEN_REQUEST模式
                Response response = chain.proceed(request);
                try {
                    Response roomResponse = readRoomCacheWithFirstCacheThenRequest(true, response, roomCacheTime);
                    return roomResponse == null ? get400Response(request, CacheMode.FIRST_CACHE_THEN_REQUEST) : roomResponse;
                } finally {
                    writeRoomCache(response.newBuilder().body(response.body()).build());
                }
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

    private Response readRoomCacheWithRequestFailedReadCache(boolean isNetOk, Response response, long time) throws IOException {
        Request request = response.request();
        String key = request.url().url().toString() + ">" + request.method();
        Timber.i("RoomCache-Key(get):" + key);
        RoomCacheDB roomCacheDB = Box.getCacheRoomDataBase(RoomCacheDB.class);
        RoomCacheEntity roomCacheEntity = roomCacheDB.roomCacheDao().queryByKey(key);
        if (roomCacheEntity == null)
            return response;
        boolean isExpire = roomCacheEntity.checkExpire(CacheMode.REQUEST_FAILED_READ_CACHE, time, System.currentTimeMillis());
        Timber.i(key + ">>>>>isExpire(" + isExpire + ")");
        Headers responseHeaders = roomCacheEntity.getResponseHeaders();
        if (isExpire) {
            if (isNetOk)
                return response;
            return new Response.Builder()
                    .code(400)
                    .request(request)
                    .headers(responseHeaders)
                    .addHeader("Room-Cache-Control", CacheMode.REQUEST_FAILED_READ_CACHE)
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
                    .addHeader("Room-Cache-Control", CacheMode.REQUEST_FAILED_READ_CACHE)
                    .sentRequestAtMillis(System.currentTimeMillis())
                    .receivedResponseAtMillis(System.currentTimeMillis() + 20)
                    .protocol(Protocol.get(roomCacheEntity.getProtocol()))
                    .message("")
                    .body(ResponseBody.create(MEDIA_TYPE, roomCacheEntity.getData()))
                    .build();
        }
    }

    private Response readRoomCacheWithRequestFailedReadCacheNotNet(boolean isNetOk, Request request, long time) throws IOException {
        String key = request.url().url().toString() + ">" + request.method();
        Timber.i("RoomCache-Key(get):" + key);
        RoomCacheDB roomCacheDB = Box.getCacheRoomDataBase(RoomCacheDB.class);
        RoomCacheEntity roomCacheEntity = roomCacheDB.roomCacheDao().queryByKey(key);
        if (roomCacheEntity == null)
            return null;
        boolean isExpire = roomCacheEntity.checkExpire(CacheMode.REQUEST_FAILED_READ_CACHE, time, System.currentTimeMillis());
        Timber.i(key + ">>>>>isExpire(" + isExpire + ")");
        Headers responseHeaders = roomCacheEntity.getResponseHeaders();
        if (isExpire) {
            return null;
        } else {
            return new Response.Builder()
                    .code(200)
                    .request(request)
                    .headers(responseHeaders)
                    .addHeader("Room-Cache-Control", CacheMode.REQUEST_FAILED_READ_CACHE)
                    .sentRequestAtMillis(System.currentTimeMillis())
                    .receivedResponseAtMillis(System.currentTimeMillis() + 20)
                    .protocol(Protocol.get(roomCacheEntity.getProtocol()))
                    .message("")
                    .body(ResponseBody.create(MEDIA_TYPE, roomCacheEntity.getData()))
                    .build();
        }
    }

    private Response readRoomCacheWithIfNoneCacheRequest(boolean isNetOk, Chain chain, Request request, long time) throws IOException {
        String key = request.url().url().toString() + ">" + request.method();
        Timber.i("RoomCache-Key(get):" + key);
        RoomCacheDB roomCacheDB = Box.getCacheRoomDataBase(RoomCacheDB.class);
        RoomCacheEntity roomCacheEntity = roomCacheDB.roomCacheDao().queryByKey(key);
        if (roomCacheEntity == null)
            return chain.proceed(request);
        boolean isExpire = roomCacheEntity.checkExpire(CacheMode.IF_NONE_CACHE_REQUEST, time, System.currentTimeMillis());
        Timber.i(key + ">>>>>isExpire(" + isExpire + ")");
        Headers responseHeaders = roomCacheEntity.getResponseHeaders();
        if (isExpire) {
            if (isNetOk)
                return chain.proceed(request);
            return new Response.Builder()
                    .code(400)
                    .request(request)
                    .headers(responseHeaders)
                    .addHeader("Room-Cache-Control", CacheMode.IF_NONE_CACHE_REQUEST)
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
                    .addHeader("Room-Cache-Control", CacheMode.IF_NONE_CACHE_REQUEST)
                    .sentRequestAtMillis(System.currentTimeMillis())
                    .receivedResponseAtMillis(System.currentTimeMillis() + 20)
                    .protocol(Protocol.get(roomCacheEntity.getProtocol()))
                    .message("")
                    .body(ResponseBody.create(MEDIA_TYPE, roomCacheEntity.getData()))
                    .build();
        }
    }

    private Response readRoomCacheWithFirstCacheThenRequest(boolean isNetOk, Response response, long time) throws IOException {
        Request request = response.request();
        String key = request.url().url().toString() + ">" + request.method();
        Timber.i("RoomCache-Key(get):" + key);
        RoomCacheDB roomCacheDB = Box.getCacheRoomDataBase(RoomCacheDB.class);
        RoomCacheEntity roomCacheEntity = roomCacheDB.roomCacheDao().queryByKey(key);
        if (roomCacheEntity == null)
            return null;
        boolean isExpire = roomCacheEntity.checkExpire(CacheMode.FIRST_CACHE_THEN_REQUEST, time, System.currentTimeMillis());
        Timber.i(key + ">>>>>isExpire(" + isExpire + ")");
        Headers responseHeaders = roomCacheEntity.getResponseHeaders();
        if (isExpire) {
            return null;
        } else {
            return new Response.Builder()
                    .code(200)
                    .request(request)
                    .headers(responseHeaders)
                    .addHeader("Room-Cache-Control", CacheMode.FIRST_CACHE_THEN_REQUEST)
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
