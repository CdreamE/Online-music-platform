package com.example.q.pocketmusic.module.search.net;

import android.content.Context;
import android.content.Intent;

import com.example.q.pocketmusic.callback.IBaseList;
import com.example.q.pocketmusic.callback.IBaseView;
import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.Song;
import com.example.q.pocketmusic.model.bean.SongObject;
import com.example.q.pocketmusic.model.net.LoadSearchSongList;
import com.example.q.pocketmusic.module.song.SongActivity;

import java.util.ArrayList;
import java.util.List;



public class SearchNetFragmentPresenter {
    private Context context;
    private IView fragment;
    private int mPage;

    public SearchNetFragmentPresenter(Context context, IView fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    public int getmPage() {
        return mPage;
    }

    //这里有问题，最好是能够先搜Bmob再搜全网
    public void getList(final String query) {
        //来自网络
        final List<Song> songs = new ArrayList<>();
        getListFromNet(query, songs);
    }

    private void getListFromNet(String query, final List<Song> songs) {
        new LoadSearchSongList(mPage) {
            @Override
            protected void onPostExecute(final List<Song> list) {
                if (list != null) {
                    songs.addAll(list);//Bmob和网络搜索组合
                }
                fragment.setList(songs);
            }
        }.execute(query);
    }

    public void setPage(int page) {
        this.mPage = page;
    }

    public void enterSongActivity(Song song, int searchFrom) {
        Intent intent = new Intent(context, SongActivity.class);
        SongObject object = new SongObject(song, searchFrom, Constant.SHOW_COLLECTION_MENU, Constant.NET);
        intent.putExtra(SongActivity.PARAM_SONG_OBJECT_PARCEL, object);
        context.startActivity(intent);
    }

    public interface IView extends IBaseList {
        void setList(List<Song> list);
    }
}
