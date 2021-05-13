package com.example.q.pocketmusic.model.bean.share;

import cn.bmob.v3.BmobObject;



public class SharePic extends BmobObject {
    private ShareSong shareSong;
    private String url;

    public SharePic(ShareSong shareSong, String url) {
        this.shareSong = shareSong;
        this.url = url;
    }

    public ShareSong getShareSong() {
        return shareSong;
    }

    public void setShareSong(ShareSong shareSong) {
        this.shareSong = shareSong;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
