package com.example.q.pocketmusic.callback;

import cn.bmob.v3.BmobQuery;



public class CacheQuery<T> extends BmobQuery<T> {

    public CacheQuery(boolean isRefreshing) {
        if (isRefreshing) {
            setCachePolicy(CachePolicy.NETWORK_ELSE_CACHE);
        } else {
            setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
        }

    }
}
