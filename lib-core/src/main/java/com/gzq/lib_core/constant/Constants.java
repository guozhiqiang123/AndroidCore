package com.gzq.lib_core.constant;

import com.gzq.lib_core.base.Box;

/**
 * created on 2018/10/16 11:28
 * created by: gzq
 * description:
 */
public interface Constants {
    /**
     * 存放session user信息的key
     * {@link com.gzq.lib_core.session.PreferencesSessionManager}
     * {@link com.gzq.lib_core.session.MmkvSessionManager}
     */
    String KEY_SESSION_USER = "session_user";
    /**
     * 存放session token信息的key
     * {@link com.gzq.lib_core.session.PreferencesSessionManager}
     * {@link com.gzq.lib_core.session.MmkvSessionManager}
     */
    String KEY_SESSION_TOKEN = "session_token";
    /**
     * room数据库默认的名字
     * {@link Box#getRoomDataBase(Class)}
     */
    String NAME_ROOM_DATABASE = "EasyDB";

    /**
     * 默认缓存数据库名字
     */
    String ROOM_CACHE_NAME = "Room-Cache-Database";
    /**
     * 网络请求默认超时时间
     */
    int DEFAULT_TIMEOUT = 10;
}
