package com.example.q.pocketmusic.module.home.net;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.q.pocketmusic.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;



//最好能设定成后台自定义图片
public class RollPagerAdapter extends LoopPagerAdapter {
    private static int[] picList = new int[]{R.drawable.banner_1,
            R.drawable.banner_2, R.drawable.banner_3};


    public RollPagerAdapter(RollPagerView viewPager) {
        super(viewPager);

    }


    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(picList[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }

    @Override
    public int getRealCount() {
        return picList.length;
    }


}
