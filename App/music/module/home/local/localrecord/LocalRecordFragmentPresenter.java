package com.example.q.pocketmusic.module.home.local.localrecord;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.example.q.pocketmusic.IAudioPlayerService;
import com.example.q.pocketmusic.config.Constant;
import com.example.q.pocketmusic.model.bean.local.RecordAudio;
import com.example.q.pocketmusic.model.db.RecordAudioDao;
import com.example.q.pocketmusic.callback.IBasePresenter;
import com.example.q.pocketmusic.model.net.LoadLocalRecordList;
import com.example.q.pocketmusic.model.net.LoadLocalSongList;
import com.example.q.pocketmusic.model.net.SynchronizeRecordAudio;
import com.example.q.pocketmusic.module.common.BaseActivity;
import com.example.q.pocketmusic.module.common.BasePresenter;
import com.example.q.pocketmusic.service.MediaPlayerService;
import com.example.q.pocketmusic.util.LogUtils;
import com.example.q.pocketmusic.util.MyToast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;



public class LocalRecordFragmentPresenter extends BasePresenter {
    private static final int PROGRESS = 0;
    private Context context;
    private IView activity;
    private RecordAudioDao recordAudioDao;

    public LocalRecordFragmentPresenter(Context context, IView activity) {
        this.context = context;
        this.activity = activity;
        recordAudioDao = new RecordAudioDao(context);
    }

    public void loadRecordList() {
        new LoadLocalRecordList(context, recordAudioDao) {
            @Override
            protected void onPostExecute(List<RecordAudio> recordAudios) {
                super.onPostExecute(recordAudios);
                activity.setList(recordAudios);
            }
        }.execute();
    }

    //????????????
    public void synchronizedRecord() {
        //??????????????????????????????????????????????????????????????????????????????????????????????????????
        new SynchronizeRecordAudio(recordAudioDao, context) {
            @Override
            protected void onPostExecute(Boolean isSucceed) {
                super.onPostExecute(isSucceed);
                if (!isSucceed) {
                    MyToast.showToast(context, "??????????????????????????????????????????");
                }
                loadRecordList();
            }
        }.execute();
    }

    //??????????????????
    public void deleteRecord(RecordAudio item) {
        //????????????
        File file = new File(item.getPath());
        file.delete();
        //???????????????
        recordAudioDao.delete(item);
    }


    /**
     * ??????????????????????????????????????????
     */
    private MediaReceiver mMediaReceiver;
    private IAudioPlayerService mService;//???????????????
    private int position;//????????????????????????
    private boolean isDestroy;//????????????
    private SimpleDateFormat durationFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IAudioPlayerService.Stub.asInterface(service);
            if (mService != null) {
                try {
                    //????????????
                    mService.openAudio(position);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //????????????
            try {
                mService.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    //??????????????????,????????????isDestroy=true???????????????????????????
                    if (!isDestroy) {
                        mHandler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    }
                    int currentPosition;
                    try {
                        currentPosition = mService.getCurrentPosition();
                        String time = durationFormat.format(new Date(mService.getCurrentPosition())) + "/" + durationFormat.format(new Date(mService.getDuration()));
                        activity.updateProgress(currentPosition, time);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
            }
        }
    };


    //?????????????????????????????????,??????????????????????????????
    private class MediaReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String notify = intent.getStringExtra(MediaPlayerService.NOTIFY);//???????????????notify;
                if (notify.equals(MediaPlayerService.PROGRESS)) {//?????????
                    String name = mService.getAudioName();
                    String time = durationFormat.format(new Date(mService.getCurrentPosition())) + "/" + durationFormat.format(new Date(mService.getDuration()));
                    int duration = mService.getDuration();
                    activity.setViewStatus(name, time, duration);
                    isDestroy = false;
                    mHandler.sendEmptyMessage(PROGRESS);
                } else if (notify.equals(MediaPlayerService.COMPLETE)) {//??????
                    isDestroy = true;
                    activity.setPlayOrPauseImage(true);
                    mService.openAudio(position);//?????????
//                    mService.pause();//???????????????????????????????????????????????????

                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    //?????????????????????
    public void registerReceiver() {
        IntentFilter filter = new IntentFilter(MediaPlayerService.RECEIVER_ACTION);
        mMediaReceiver = new MediaReceiver();
        context.registerReceiver(mMediaReceiver, filter);
        //Log.e("TAG", "registerReceiver()");
    }

    //????????????,????????????
    public void bindService(int position) {
        this.position = position;
        Intent intent = new Intent(context, MediaPlayerService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //Log.e("TAG", "bindService");
    }


    public void seekTo(int progress) {
        try {
            mService.seekTo(progress);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean playOrPause() {
        try {
            if (mService.isPlaying()) {
                mService.pause();
                return false;
            } else {
                mService.play();
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    //????????????,??????????????????
    public void closeMedia() {
        isDestroy = true;
        try {
            mService.stop();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        context.unbindService(connection);
        context.unregisterReceiver(mMediaReceiver);
        mMediaReceiver = null;
    }


    public interface IView {
        void setList(List<RecordAudio> list);

        void setViewStatus(String name, String time, int duration);

        void updateProgress(int currentPosition, String time);

        void setPlayOrPauseImage(boolean status);

    }
}
