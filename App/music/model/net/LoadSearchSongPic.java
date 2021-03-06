package com.example.q.pocketmusic.model.net;

import android.os.AsyncTask;


import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.Song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class LoadSearchSongPic extends AsyncTask<Song, Void, Integer> {
    private Song song;

    /**
     * 得到搜索列表中某一首歌的图片，存入歌曲的ivUrls中
     *
     * @param songs 选中歌曲
     * @return 正确or失败
     */
    @Override
    protected Integer doInBackground(Song... songs) {
        song = songs[0];
        ArrayList<String> list = new ArrayList<>();
        String songUrl=song.getUrl();
        try {
            Document doc = Jsoup.connect(songUrl).userAgent(Constant.USER_AGENT).timeout(6000).get();
            Element box = doc.getElementById("box");
            Element content = box.select("div.content").get(0);
            Elements imgs = content.getElementsByTag("img");
            for (Element img : imgs) {
                String src = img.attr("src");
                list.add(Constant.SO_PU_BASE+src);
            }
            song.setIvUrl(list);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.FAIL;
        }
        return Constant.SUCCESS;
    }
}
