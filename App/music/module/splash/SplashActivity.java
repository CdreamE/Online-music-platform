package com.example.q.pocketmusic.module.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.q.pocketmusic.R;
import com.example.q.pocketmusic.config.BmobInfo;
import com.example.q.pocketmusic.module.common.BaseActivity;
import com.example.q.pocketmusic.util.MyToast;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, SplashPresenter.IView {
    private static final int ENTER_HOME_ACTIVITY = 1001;
    @BindView(R.id.bmob_info_tv)
    TextView bmobInfoTv;
    @BindView(R.id.activity_splash)
    RelativeLayout activitySplash;
    private boolean isEnter = false;
    private static final int REQUEST_STORAGE = 3001;//READ_EXTERNAL_STORAGE    ,    WRITE_EXTERNAL_STORAGE
    private static final int REQUEST_PHONE = 4001;//READ_PHONE_STATE
    private static final int REQUEST_MICROPHONE = 5001;//RECORD_AUDIO
    private static String[] requestPermissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private SplashPresenter presenter;
    public static final int REQUEST_PERMISSION = 1111;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ENTER_HOME_ACTIVITY:
                    if (!isEnter) {
                        presenter.enterHomeActivity();
                        isEnter = true;
                    }
                    finish();
                    break;
            }
        }
    };


    @Override
    public int setContentResource() {
        return R.layout.activity_splash;
    }

    @Override
    public void setListener() {

    }

    @Override
    public void init() {
        presenter = new SplashPresenter(this, this);
        presenter.getBmobInfo();
        requestPermissions();
    }


    //????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        MyToast.showToast(context, "??????????????????");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        for (String permission : perms) {
            MyToast.showToast(context, permission + "???????????????,????????????????????????");
        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).
                    setRationale("??????????????????--??????????????????????????????????????????????????????????????????????????????app??????????????????")
                    .setTitle("????????????")
                    .build()
                    .show();
        }
    }

    private void requestPermissions() {
        if (!EasyPermissions.hasPermissions(context, requestPermissions)) {
            EasyPermissions.requestPermissions(this, "???????????????", REQUEST_PERMISSION, requestPermissions);
        } else {
            sendEnterHomeMessage();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION)
    public void sendEnterHomeMessage() {
        handler.removeMessages(ENTER_HOME_ACTIVITY);
        handler.sendEmptyMessageDelayed(ENTER_HOME_ACTIVITY, 1200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(context, requestPermissions)) {
                sendEnterHomeMessage();
            }
        }
    }

    @Override
    public void showRefreshing(boolean isShow) {

    }

    @Override
    public void setLaBaText(BmobInfo bmobInfo) {
        if (isEnter) {
            return;
        }
        bmobInfoTv.setText(bmobInfo.getContent());
    }

}
