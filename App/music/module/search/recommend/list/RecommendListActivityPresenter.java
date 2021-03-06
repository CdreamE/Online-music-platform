package com.example.q.pocketmusic.module.search.recommend.list;


import android.content.Context;
import android.content.Intent;

import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.Song;
import com.example.q.pocketmusic.model.bean.SongObject;
import com.example.q.pocketmusic.model.net.LoadRecommendList;
import com.example.q.pocketmusic.module.common.BasePresenter;
import com.example.q.pocketmusic.module.song.SongActivity;
import com.example.q.pocketmusic.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class RecommendListActivityPresenter  extends BasePresenter {
    private IView activity;
    private Context context;
    private int mPage;

    public RecommendListActivityPresenter(IView activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    //可以得到推荐列表
    public void getList() {
        String url = Constant.RECOMMEND_LIST_URL + mPage + ".html";
        new LoadRecommendList() {
            @Override
            protected void onPostExecute(List<Song> songs) {
                super.onPostExecute(songs);
                LogUtils.e(TAG,"songs");
                activity.setList(songs);
            }
        }.execute(url);
    }

    public int getmPage() {
        return mPage;
    }


    public void setPage(int page) {
        this.mPage=page;
    }

    public void enterSongActivity(Song song) {
        Intent intent = new Intent(context, SongActivity.class);
        SongObject object=new SongObject(song, Constant.FROM_RECOMMEND,Constant.SHOW_COLLECTION_MENU,Constant.NET);
        intent.putExtra(SongActivity.PARAM_SONG_OBJECT_PARCEL,object);
        context.startActivity(intent);
    }

    public interface IView {
        void setList(List<Song> list);
    }
}
