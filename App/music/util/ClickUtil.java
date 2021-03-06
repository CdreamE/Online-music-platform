package com.example.q.pocketmusic.util;


//防止快速点击
public class ClickUtil {
    private static long lastClickTime;
    public synchronized static boolean isFastDoubleClick(int min) {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < min) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
