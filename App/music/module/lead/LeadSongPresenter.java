package com.example.q.pocketmusic.module.lead;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.local.Img;
import com.example.q.pocketmusic.model.bean.local.LocalSong;
import com.example.q.pocketmusic.model.db.ImgDao;
import com.example.q.pocketmusic.model.db.LocalSongDao;
import com.example.q.pocketmusic.module.common.BasePresenter;
import com.example.q.pocketmusic.util.FileUtils;
import com.example.q.pocketmusic.util.LogUtils;
import com.example.q.pocketmusic.util.MyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;



public class LeadSongPresenter extends BasePresenter {
    private Context context;
    private IView activity;
    private LocalSongDao localSongDao;
    private ImgDao imgDao;
    public final static String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constant.FILE_NAME + "/";
    private List<String> imgUrls;

    public LeadSongPresenter(Context context, IView activity) {
        this.context = context;
        this.activity = activity;
        localSongDao = new LocalSongDao(context);
        imgDao = new ImgDao(context);
        imgUrls = new ArrayList<>();
    }

    //打开图片管理器
    public void openPicture() {
        FunctionConfig config = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(8)
                .build();
        GalleryFinal.openGalleryMuti(1, config, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode == 1) {
                    imgUrls.clear();
                    for (PhotoInfo info : resultList) {
                        String url = info.getPhotoPath();
                        imgUrls.add(url);
                    }
                    activity.showSmallPic(imgUrls);//返回图片地址
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    //移动，并加入数据库
    public void moveFileAndAddDatabase(String name, List<String> list) {
        //建立目标文件
        File dir = new File(FILE_DIR + name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        LocalSong localSong = new LocalSong();
        localSong.setName(name);
        localSong.setDate(dateFormat.format(new Date()));
        localSong.setSort(Constant.getSort());
        localSongDao.add(localSong);

        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));//只需要得到名字
            //这里先转移，再加入数据库
            FileUtils.copyFile(list.get(i), dir + "/" + file.getName());// /storage/emulated/0/sina/weibo/.weibo_pic_newthumb.4024457410441510.jpg ---> YuPuDownload/.weibo_pic_newthumb.4024457410441510.jpg;
            Img img = new Img();
            img.setUrl(dir + "/" + file.getName());
            img.setLocalSong(localSong);
            imgDao.add(img);
        }
    }

    public void leadSong(String name) {
        if (TextUtils.isEmpty(name)) {
            MyToast.showToast(context, "名字不能为空");
            return;
        } else if (imgUrls.size() <= 0) {
            MyToast.showToast(context, "请添加图片");
            return;
        }
        moveFileAndAddDatabase(name, imgUrls);
        activity.returnActivity();
    }

    public void checkPic(String path) {
        LogUtils.e(TAG, path);
        GalleryFinal.openEdit(2, path, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int requestCode, List<PhotoInfo> resultList) {

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    interface IView {

        void showSmallPic(List<String> imgUrls);

        void finish();

        void returnActivity();
    }
}
