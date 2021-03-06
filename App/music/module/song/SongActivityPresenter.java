package com.example.q.pocketmusic.module.song;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.Menu;

import com.example.q.pocketmusic.R;
import com.example.q.pocketmusic.callback.IBaseList;
import com.example.q.pocketmusic.callback.IBasePresenter;
import com.example.q.pocketmusic.callback.ToastQueryListListener;
import com.example.q.pocketmusic.callback.ToastQueryListener;
import com.example.q.pocketmusic.callback.ToastSaveListener;
import com.example.q.pocketmusic.callback.ToastUpdateListener;
import com.example.q.pocketmusic.config.CommonString;
import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.DownloadInfo;
import com.example.q.pocketmusic.model.bean.MyUser;
import com.example.q.pocketmusic.model.bean.Song;
import com.example.q.pocketmusic.model.bean.SongObject;
import com.example.q.pocketmusic.model.bean.ask.AskSongComment;
import com.example.q.pocketmusic.model.bean.collection.CollectionPic;
import com.example.q.pocketmusic.model.bean.collection.CollectionSong;
import com.example.q.pocketmusic.model.bean.local.Img;
import com.example.q.pocketmusic.model.bean.local.LocalSong;
import com.example.q.pocketmusic.model.bean.local.RecordAudio;
import com.example.q.pocketmusic.model.bean.share.ShareSong;
import com.example.q.pocketmusic.model.db.LocalSongDao;
import com.example.q.pocketmusic.model.db.RecordAudioDao;
import com.example.q.pocketmusic.module.common.BaseActivity;
import com.example.q.pocketmusic.module.common.BasePresenter;
import com.example.q.pocketmusic.module.song.state.SongController;
import com.example.q.pocketmusic.util.CheckUserUtil;
import com.example.q.pocketmusic.util.DownloadUtil;
import com.example.q.pocketmusic.util.FileUtils;
import com.example.q.pocketmusic.util.MyToast;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import pub.devrel.easypermissions.EasyPermissions;


public class SongActivityPresenter extends BasePresenter implements IBasePresenter {
    private Context context;
    private IView activity;
    private Intent intent;
    private SongController controller;//???????????????,??????????????????
    private int isFrom;//????????????
    private Song song;
    private int showMenuFlag;
    private int loadingWay;//????????????
    private boolean isEnableAgree = true;//??????????????????

    //??????????????????
    private RECORD_STATUS status = RECORD_STATUS.STOP;

    public enum RECORD_STATUS {
        PLAY, STOP
    }

    private RecordAudioDao recordAudioDao;
    //???????????????
    private final static String RECORD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constant.RECORD_FILE + "/";
    //???????????????
    private final static String TEMP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    //???????????????
    private final static String TEMP_NAME = "temp";
    //?????????????????????
    private MediaRecorder mRecorder = new MediaRecorder();
    private MediaPlayer mPlayer = new MediaPlayer();
    //??????????????????
    private static final int ADD_TIME = 0;
    //????????????
    private int mRecordTime;
    //????????????
    private Timer mRecordTimer;
    private final int REQUEST_RECORD_AUDIO = 1001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_TIME:
                    mRecordTime++;
                    activity.changedTimeTv(String.valueOf(mRecordTime));
                    break;
            }
        }
    };


    public int getLoadingWay() {
        return loadingWay;
    }

    public Song getSong() {
        return song;
    }

    public boolean isEnableAgree() {
        return isEnableAgree;
    }

    public SongActivityPresenter(Context context, IView activity, Intent intent) {
        this.context = context;
        this.activity = activity;
        this.intent = intent;
        SongObject songObject = intent.getParcelableExtra(SongActivity.PARAM_SONG_OBJECT_PARCEL);
        song = songObject.getSong();
        this.isFrom = songObject.getFrom();
        this.showMenuFlag = songObject.getShowMenu();
        this.loadingWay = songObject.getLoadingWay();

        controller = SongController.getInstance(intent, context, activity);

        if (controller == null) {
            MyToast.showToast(context, "??????????????????");
            activity.finish();
        }

        //?????????????????????????????????,
        if (isFrom == Constant.FROM_ASK) {
            checkHasAgree();
        }

        //??????????????????
        if (loadingWay == Constant.LOCAL) {
            recordAudioDao = new RecordAudioDao(context);
            File file = new File(RECORD_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    //????????????
    public void loadPic() {
        controller.loadPic();
    }


    //???????????????from???????????????menu;
    public void CreateMenuByFrom(Menu menu) {
        switch (showMenuFlag) {
            case Constant.SHOW_ALL_MENU:
                ((BaseActivity) context).getMenuInflater().inflate(R.menu.menu_song_all, menu);//????????????????????????
                break;
            case Constant.SHOW_COLLECTION_MENU:
                ((BaseActivity) context).getMenuInflater().inflate(R.menu.menu_song_collection, menu);//???????????????
                break;
            case Constant.SHOW_ONLY_DOWNLOAD:
                ((BaseActivity) context).getMenuInflater().inflate(R.menu.menu_song_download, menu);//??????
                break;
            case Constant.SHOW_NO_MENU://?????????Menu
                break;
        }
    }

    //??????
    public void download(final String name) {
        activity.showLoading(true);
        DownloadUtil downloadUtil = new DownloadUtil(context);
        downloadUtil.setOnDownloadListener(new DownloadUtil.OnDownloadListener() {
                                               @Override
                                               public DownloadInfo onStart() {
                                                   activity.dismissEditDialog();
                                                   return downloadStartCheck();
                                               }

                                               @Override
                                               public void onSuccess() {
                                                   activity.showLoading(false);
                                                   activity.downloadResult(Constant.SUCCESS, "????????????");
                                               }

                                               @Override
                                               public void onFailed(String info) {
                                                   activity.showLoading(false);
                                                   activity.downloadResult(Constant.FAIL, info);
                                               }
                                           }
        ).downloadBatchPic(name, song.getIvUrl(), song.getTypeId());
    }

    //????????????
    private DownloadInfo downloadStartCheck() {
        //????????????
        if (song.getIvUrl() == null || song.getIvUrl().size() <= 0) {
            activity.showLoading(false);
            return new DownloadInfo("????????????", false);
        }
        //????????????????????????
        if (new LocalSongDao(context).isExist(song.getName())) {
            activity.showLoading(false);
            return new DownloadInfo("???????????????", false);
        }

        //????????????
        if (song.isNeedGrade()) {
            MyUser user = CheckUserUtil.checkLocalUser((BaseActivity) context);
            //???????????????
            if (user == null) {
                activity.showLoading(false);
                return new DownloadInfo("???????????????", false);
            }
            //????????????
            if (!CheckUserUtil.checkUserContribution(((BaseActivity) context), Constant.REDUCE_COIN_UPLOAD)) {
                activity.showLoading(false);
                return new DownloadInfo(CommonString.STR_NOT_ENOUGH_COIN, false);
            }
            //????????????
            user.increment("contribution", -Constant.REDUCE_COIN_UPLOAD);
            user.update(new ToastUpdateListener(context, activity) {
                @Override
                public void onSuccess() {
                    MyToast.showToast(context, CommonString.REDUCE_COIN_BASE + (Constant.REDUCE_COIN_UPLOAD));
                }
            });
        }
        return new DownloadInfo("", true);
    }


    //??????
    public void agree() {
        if (isEnableAgree()) {
            BmobRelation relation = new BmobRelation();
            final MyUser user = MyUser.getCurrentUser(MyUser.class);
            relation.add(user);
            AskSongComment askSongComment = (AskSongComment) intent.getSerializableExtra(SongActivity.ASK_COMMENT);
            askSongComment.setAgrees(relation);
            askSongComment.increment("agreeNum");//??????????????????????????????
            askSongComment.update(new ToastUpdateListener(context, activity) {
                @Override
                public void onSuccess() {
                    MyToast.showToast(context, "?????????");
                    user.increment("contribution", Constant.ADD_CONTRIBUTION_AGREE);
                    user.update(new ToastUpdateListener(context, activity) {
                        @Override
                        public void onSuccess() {
                            MyToast.showToast(context, CommonString.ADD_COIN_BASE + Constant.ADD_CONTRIBUTION_AGREE);
                        }
                    });
                }
            });
        } else {
            MyToast.showToast(context, "??????????????????~");
        }


    }


    //????????????????????????????????????????????????
    public void checkHasAgree() {
        BmobQuery<MyUser> query = new BmobQuery<>();
        final MyUser user = MyUser.getCurrentUser(MyUser.class);
        AskSongComment askSongComment = (AskSongComment) intent.getSerializableExtra(SongActivity.ASK_COMMENT);
        query.addWhereRelatedTo("agrees", new BmobPointer(askSongComment));
        query.findObjects(new ToastQueryListener<MyUser>(context, activity) {
            @Override
            public void onSuccess(List<MyUser> list) {
                for (MyUser other : list) {
                    if (other.getObjectId().equals(user.getObjectId())) {
                        //????????????
                        isEnableAgree = false;
                        break;
                    }
                    isEnableAgree = true;
                }
            }
        });

    }

    //????????????
    public void addCollection() {
        activity.showLoading(true);
        final MyUser user = CheckUserUtil.checkLocalUser((BaseActivity) context);
        if (user == null) {
            activity.showLoading(false);
            MyToast.showToast(context, "????????????~");
            return;
        }
        if (song.getIvUrl() == null || song.getIvUrl().size() <= 0) {
            activity.showLoading(false);
            MyToast.showToast(context, "????????????");
            return;
        }
        //????????????????????????
        BmobQuery<CollectionSong> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.addWhereRelatedTo("collections", new BmobPointer(user));//???user??????Collections???user
        query.findObjects(new ToastQueryListener<CollectionSong>(context, activity) {
            @Override
            public void onSuccess(List<CollectionSong> list) {
                //???????????????
                for (CollectionSong collectionSong : list) {
                    if (collectionSong.getName().equals(song.getName())) {
                        activity.showLoading(false);
                        MyToast.showToast(context, "?????????");
                        return;
                    }
                }
                //?????????????????????
                if (!CheckUserUtil.checkUserContribution(((BaseActivity) context), Constant.REDUCE_CONTRIBUTION_COLLECTION)) {
                    activity.showLoading(false);
                    MyToast.showToast(context, "???????????????~");
                    return;
                }


                //??????????????????
                final CollectionSong collectionSong = new CollectionSong();
                collectionSong.setName(song.getName());
                collectionSong.setNeedGrade(song.isNeedGrade());//??????????????????
                collectionSong.setIsFrom(isFrom);
                collectionSong.setContent(song.getContent());
                collectionSong.save(new ToastSaveListener<String>(context, activity) {

                    @Override
                    public void onSuccess(String s) {
                        final int numPic = song.getIvUrl().size();
                        List<BmobObject> collectionPics = new ArrayList<BmobObject>();
                        for (int i = 0; i < numPic; i++) {
                            CollectionPic collectionPic = new CollectionPic();
                            collectionPic.setCollectionSong(collectionSong);
                            collectionPic.setUrl(song.getIvUrl().get(i));
                            collectionPics.add(collectionPic);
                        }
                        //????????????
                        new BmobBatch().insertBatch(collectionPics).doBatch(new ToastQueryListListener<BatchResult>(context, activity) {
                            @Override
                            public void onSuccess(List<BatchResult> list) {
                                BmobRelation relation = new BmobRelation();
                                relation.add(collectionSong);
                                user.setCollections(relation);//??????????????????
                                user.update(new ToastUpdateListener(context, activity) {
                                    @Override
                                    public void onSuccess() {
                                        MyToast.showToast(context, "?????????");
                                        user.increment("contribution", -Constant.REDUCE_CONTRIBUTION_COLLECTION);//?????????-1
                                        user.update(new ToastUpdateListener(context, activity) {
                                            @Override
                                            public void onSuccess() {
                                                activity.showLoading(false);
                                                MyToast.showToast(context, CommonString.REDUCE_COIN_BASE + Constant.REDUCE_CONTRIBUTION_COLLECTION);
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    //????????????,???????????????
    public void share() {
        List<String> list = null;
        switch (loadingWay) {
            case Constant.NET:
                list = song.getIvUrl();
                break;
            case Constant.LOCAL:
                list = getLocalImgs();
                break;
        }
        if (list == null || list.size() <= 0) {
            MyToast.showToast(context, "????????????");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String url : list) {
            sb.append(url).append(",");
        }

        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "??????????????????" + "<<" + song.getName() + ">>:" + sb.toString());
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            MyToast.showToast(context, "???????????????????????????~");
        }
    }

    //??????????????????
    @NonNull
    private ArrayList<String> getLocalImgs() {
        LocalSong localsong = (LocalSong) intent.getSerializableExtra(SongActivity.LOCAL_SONG);
        LocalSongDao localSongDao = new LocalSongDao(context);
        ArrayList<String> imgUrls = new ArrayList<>();
        LocalSong localSong = localSongDao.findBySongId(localsong.getId());
        if (localSong == null) {
            MyToast.showToast(context, "??????????????????????????????");
            activity.finish();
            return new ArrayList<>();
        }
        ForeignCollection<Img> imgs = localSong.getImgs();
        CloseableIterator<Img> iterator = imgs.closeableIterator();
        try {
            while (iterator.hasNext()) {
                Img img = iterator.next();
                imgUrls.add(img.getUrl());
            }
        } finally {
            try {
                iterator.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        return imgUrls;
    }

    //??????
    public void record() {
        //????????????
        String[] perms = {Manifest.permission.RECORD_AUDIO};
        if (!EasyPermissions.hasPermissions(context, perms)) {
            EasyPermissions.requestPermissions((BaseActivity) context, "????????????", REQUEST_RECORD_AUDIO, perms);
            return;
        }

        activity.setBtnStatus(status);
        //????????????
        if (status == SongActivityPresenter.RECORD_STATUS.STOP) {
            //??????????????????
            status = SongActivityPresenter.RECORD_STATUS.PLAY;
            //?????????
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //???????????????????????????,???????????????????????????
            File file = new File(TEMP_DIR + TEMP_NAME + ".3gp");
            if (file.exists()) {
                file.delete();
            }
            mRecorder.setOutputFile(TEMP_DIR + TEMP_NAME + ".3gp");
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecordTime = 0;
            try {
                mRecorder.prepare();
                mRecorder.start();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(ADD_TIME);
                    }
                };
                mRecordTimer = new Timer();
                mRecordTimer.schedule(task, 1000, 1000);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //????????????
            status = SongActivityPresenter.RECORD_STATUS.STOP;
            mRecorder.stop();
            mHandler.removeMessages(ADD_TIME);
            mRecordTimer.cancel();
            mRecordTime = 0;
            activity.changedTimeTv(String.valueOf(mRecordTime));
            //????????????dialog
            activity.showAddDialog(song.getName());
        }
    }

    //????????????????????????
    public void enterSystemSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    //s:?????????=editText?????????
    public void saveRecordAudio(final String s) {
        try {
            //????????????
            mPlayer.reset();
            mPlayer.setDataSource(TEMP_DIR + TEMP_NAME + ".3gp");//????????????????????????
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int duration = mp.getDuration();
                    //??????????????????
                    RecordAudio recordAudio = new RecordAudio();
                    recordAudio.setName(s);
                    recordAudio.setDuration(duration);//??????????????????
                    recordAudio.setDate(dateFormat.format(new Date()));//???????????????????????????????????????
                    recordAudio.setPath(RECORD_DIR + s + ".3gp");
                    boolean isSucceed = recordAudioDao.add(recordAudio);
                    activity.setAddResult(isSucceed);//????????????
                    //?????????????????????????????????????????????
                    //???tempRecord????????????????????????
                    if (isSucceed) {
                        FileUtils.copyFile(TEMP_DIR + TEMP_NAME + ".3gp", RECORD_DIR + s + ".3gp");
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onStop() {
        //????????????,??????Timer
        mHandler.removeMessages(ADD_TIME);
        if (mRecordTimer != null) {
            mRecordTimer.cancel();
        }
        //??????mRecorder
        if (mRecorder != null) {
            mRecorder.reset();
        }
        //??????mPlayer
        if (mPlayer != null) {
            mPlayer.reset();
        }

        //?????????
        status = RECORD_STATUS.STOP;
        activity.setBtnStatus(RECORD_STATUS.PLAY);
        activity.changedTimeTv(String.valueOf(0));
    }

    //????????????
    @Override
    public void release() {
        onStop();
        mPlayer.release();
        mRecorder.release();
    }


    public interface IView extends IBaseList {
        void loadFail();

        void downloadResult(Integer result, String info);

        void dismissEditDialog();

        void setPicResult(List<String> ivUrl, int from);

        void setBtnStatus(SongActivityPresenter.RECORD_STATUS status);

        void changedTimeTv(String s);

        void showAddDialog(String s);

        void setAddResult(boolean isSucceed);

        void finish();
    }
}
