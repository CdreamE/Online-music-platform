package com.example.q.pocketmusic.callback;



public interface IBaseView {
    void showLoading(boolean isShow);

    void finish();

    int setContentResource();

    void setListener();

    void init();
}
