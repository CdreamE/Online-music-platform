package com.example.q.pocketmusic.callback;

import android.content.Context;
import android.widget.ImageView;



//策略模式
public interface IDisplayStrategy {
    void display(Context context, String url, ImageView imageView);

    void displayCircle(Context context, String url, ImageView imageView);

    void displayCorner(Context context, String url, ImageView imageView);
}
