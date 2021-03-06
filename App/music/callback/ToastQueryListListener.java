package com.example.q.pocketmusic.callback;

import android.content.Context;

import com.example.q.pocketmusic.config.CommonString;
import com.example.q.pocketmusic.util.MyToast;

import java.util.List;


import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;



public abstract class ToastQueryListListener<BatchResult> extends QueryListListener<BatchResult> {
    private Context context;
    private IBaseList baseList;

    public ToastQueryListListener(Context context, IBaseList baseView) {
        this.context = context;
        this.baseList = baseView;
    }

    public abstract void onSuccess(List<BatchResult> list);

    @Override
    final public void done(List<BatchResult> list, BmobException e) {
        if (e == null) {
            onSuccess(list);
        } else {

            onFail(e);
        }
    }

    public void onFail(BmobException e) {
        baseList.showLoading(false);
        baseList.showRefreshing(false);
        MyToast.showToast(context, CommonString.STR_ERROR_INFO+e.getMessage());
        e.printStackTrace();
        //        CrashHandler handler=CrashHandler.getInstance();
//        handler.uncaughtException(Thread.currentThread(),e);
    }
}
