package com.example.q.pocketmusic.module.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


//用于测试Activity生命周期
public  abstract class TestActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(getClass().getSimpleName(),"onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(getClass().getSimpleName(),"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(getClass().getSimpleName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(getClass().getSimpleName(),"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(getClass().getSimpleName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(getClass().getSimpleName(),"onDestroy");
    }
}
