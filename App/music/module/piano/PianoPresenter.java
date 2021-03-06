package com.example.q.pocketmusic.module.piano;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.LinearLayout;

import com.example.q.pocketmusic.util.MusicUtils;
import com.example.q.pocketmusic.util.MyToast;



public class PianoPresenter {
    private Context context;
    private IView activity;
    private MusicUtils utils;
    private StringBuilder builder;
    private Handler handler;
    private static final int WHAT_BACK = 1002;
    private static final int DELAY_TIME = 250;
    private Boolean isCloseQuickBack;

    public PianoPresenter(Context context, final IView activity) {
        this.context = context;
        this.activity = activity;
        utils = new MusicUtils(context);
        builder = new StringBuilder();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (isCloseQuickBack) {
                    return;
                }
                activity.back();
                handler.sendEmptyMessageDelayed(WHAT_BACK, DELAY_TIME);
            }
        };
    }

    public String sound(int id) {
        utils.soundPlay(id);
        builder.append(utils.getNote(id));
        return builder.toString();
    }

    public String setBack() {
        if (builder.length() <= 0) {
            return "";
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public String setTab() {
        return builder.append("  ").toString();
    }

    public String setEnter() {
        return builder.append("\n").toString();
    }

    public String setBoLang() {
        return builder.append("~").toString();
    }

    public void openQuickBack() {
        isCloseQuickBack = false;
        handler.sendEmptyMessageDelayed(WHAT_BACK, DELAY_TIME);
    }

    public void closeQuickBack() {
        isCloseQuickBack = true;
    }

    public void keepPic(String title) {
        MyToast.showToast(context,"??????(?????????)");
    }

    public interface IView {

        void back();
    }
}
