package com.example.q.pocketmusic.module.song.state;

import android.content.Context;

import com.example.q.pocketmusic.callback.ToastQueryListener;
import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.Song;
import com.example.q.pocketmusic.model.bean.share.SharePic;
import com.example.q.pocketmusic.model.bean.share.ShareSong;
import com.example.q.pocketmusic.module.song.SongActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;


//分享状态
public class ShareState extends BaseState implements IState {
    private ShareSong shareSong;
    private Context context;
    private SongActivityPresenter.IView activity;

    public ShareState(Song song, ShareSong shareSong,Context context,SongActivityPresenter.IView activity) {
        super(song,Constant.NET);
        this.shareSong=shareSong;
        this.context=context;
        this.activity=activity;
    }

    @Override
    public void loadPic() {
        BmobQuery<SharePic> query = new BmobQuery<>();
        query.addWhereEqualTo("shareSong", new BmobPointer(shareSong));
        query.findObjects(new ToastQueryListener<SharePic>(context, activity) {
            @Override
            public void onSuccess(List<SharePic> list) {
                List<String> pics = new ArrayList<String>();
                for (SharePic sharePic : list) {
                    pics.add(sharePic.getUrl());
                }
                getSong().setIvUrl(pics);
                activity.setPicResult(getSong().getIvUrl(), getLoadingWay());
            }
        });
    }

}