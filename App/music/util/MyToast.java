package com.example.q.pocketmusic.util;

import android.content.Context;
import android.widget.Toast;


public class MyToast {
    private static Toast toast;

    public static void showToast(Context context,String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }
        toast.setText(content);
        toast.show();
    }
}
