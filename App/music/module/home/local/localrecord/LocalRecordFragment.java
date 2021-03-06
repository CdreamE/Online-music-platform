package com.example.q.pocketmusic.module.home.local.localrecord;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.q.pocketmusic.R;
import com.example.q.pocketmusic.model.bean.local.RecordAudio;
import com.example.q.pocketmusic.module.common.BaseFragment;
import com.example.q.pocketmusic.util.LogUtils;
import com.example.q.pocketmusic.util.MyToast;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class LocalRecordFragment extends BaseFragment implements LocalRecordFragmentPresenter.IView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener, LocalRecordFragmentAdapter.OnSelectListener {
    @BindView(R.id.recycler)
    EasyRecyclerView recycler;
    @BindView(R.id.activity_audio_record)
    LinearLayout activityAudioRecord;
    private SeekBar seekBar;
    private ImageView playIv;
    private TextView durationTv;
    private TextView titleTv;
    private Button closeBtn;
    private LocalRecordFragmentAdapter adapter;
    private LocalRecordFragmentPresenter presenter;
    private AlertDialog dialog;


    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public int setContentResource() {
        return R.layout.fragment_local_record;
    }

    @Override
    public void setListener() {
        adapter = new LocalRecordFragmentAdapter(getActivity());
        adapter.setOnItemClickListener(this);
        adapter.setListener(this);
        recycler.setRefreshListener(this);
    }

    public void init() {
        presenter = new LocalRecordFragmentPresenter(getContext(), this);
        initRecyclerView(recycler, adapter, 1,true);
        //??????????????????
        recycler.setRefreshing(true);
        presenter.loadRecordList();
    }

    @Override
    public void onItemClick(int position) {
        alertPlayerDialog(position);
    }

    @Override
    public void onSelectMore(int position) {
        alertDeleteDialog(adapter.getItem(position));
    }


    //??????????????????
    @Override
    public void setList(List<RecordAudio> list) {
        LogUtils.e(TAG, "???????????????" + list.size());
        adapter.clear();
        adapter.addAll(list);
        recycler.setRefreshing(false);
    }

    //??????dialog
    @Override
    public void setPlayOrPauseImage(boolean status) {
        if (status) {
            playIv.setImageResource(R.drawable.ico_media_play);
        } else {
            playIv.setImageResource(R.drawable.ico_media_pause);
        }
    }

    //??????dialog
    private void alertDeleteDialog(final RecordAudio recordAudio) {
        BottomSheetMenuDialog dialog = new BottomSheetBuilder(getContext())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_home_local)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.upload://????????????
                                MyToast.showToast(context, "????????????????????????");
                                break;
                            case R.id.delete://??????
                                presenter.deleteRecord(recordAudio);
                                adapter.remove(recordAudio);
                                if (adapter.getCount() == 0) {
                                    recycler.showEmpty();
                                }
                                break;
                        }
                    }
                })
                .createDialog();
        dialog.show();
    }

    //??????
    @Override
    public void onRefresh() {
        presenter.synchronizedRecord();
    }


    /**
     *
     *
     * ??????????????????????????????
     *
     */

    /**
     * --------------?????????-----------------
     * registerReceiver(???????????????)
     * startService(????????????,????????????)
     * bindService(?????????????????????binder???conn?????????mService????????????)
     * -----------------------------------------
     * mService.openAudio(MediaPlayer????????????,????????????????????????prepareAsync??????)
     * prepare????????????play(),????????????????????????????????????????????????????????????
     * ???????????????Handler????????????MessageQueue?????????handleMessage???????????????????????????????????????????????????????????????mService??????MediaPlayer???????????????????????????
     * -----------------------------------------------------------------
     * ?????????,isDestroy?????????????????????????????????
     * ??????mService??????MediaPlayer?????????Service??????????????????
     * ------------------------------
     * ????????????,onCompleteListener???????????????????????????,seekBar,?????????????????????
     *
     * @param position
     */
    private void alertPlayerDialog(int position) {
        //?????????
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_audio_play, null);
        dialog = new AlertDialog.Builder(getContext()).setView(view).setCancelable(false).create();
        playIv = (ImageView) view.findViewById(R.id.play_iv);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        titleTv = (TextView) view.findViewById(R.id.title_tv);
        durationTv = (TextView) view.findViewById(R.id.duration_tv);
        closeBtn = (Button) view.findViewById(R.id.close_btn);

        //?????????????????????,????????????????????????
        presenter.registerReceiver();
        presenter.bindService(position);

        //???????????????
        setPlayOrPauseImage(true);
        //????????????
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    presenter.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //????????????,??????
        playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean status = presenter.playOrPause();
                //true???????????????false????????????
                setPlayOrPauseImage(status);
            }
        });

        //??????
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.closeMedia();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //?????????
    @Override
    public void setViewStatus(String name, String time, int duration) {
        titleTv.setText(name);
        durationTv.setText(time);
        seekBar.setMax(duration);
    }

    //????????????seekBar
    @Override
    public void updateProgress(int currentPosition, String time) {
        seekBar.setProgress(currentPosition);
        durationTv.setText(time);
    }


//    //????????????,????????????
//    @Override
//    public void onStop() {
//        super.onStop();
//        presenter.closeMedia();
//        dialog.dismiss();
//    }
}
