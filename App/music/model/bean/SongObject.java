package com.example.q.pocketmusic.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


//Intent传递
public class SongObject implements Parcelable{
    private Song song;
    private int from;
    private int showMenu;
    private int loadingWay;

    public SongObject(Song song, int from, int showMenu, int loadingWay) {
        this.song = song;
        this.from = from;
        this.showMenu = showMenu;
        this.loadingWay = loadingWay;
    }


    protected SongObject(Parcel in) {
        from = in.readInt();
        showMenu = in.readInt();
        loadingWay = in.readInt();
        song=in.readParcelable(Thread.currentThread().getContextClassLoader());
    }


    public static final Creator<SongObject> CREATOR = new Creator<SongObject>() {
        @Override
        public SongObject createFromParcel(Parcel in) {
            return new SongObject(in);
        }

        @Override
        public SongObject[] newArray(int size) {
            return new SongObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(from);
        dest.writeInt(showMenu);
        dest.writeInt(loadingWay);
        dest.writeParcelable(song,0);
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(int showMenu) {
        this.showMenu = showMenu;
    }

    public int getLoadingWay() {
        return loadingWay;
    }

    public void setLoadingWay(int loadingWay) {
        this.loadingWay = loadingWay;
    }


}
