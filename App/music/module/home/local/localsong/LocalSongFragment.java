package com.example.q.pocketmusic.module.home.local.localsong;

import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.q.pocketmusic.R;
import com.example.q.pocketmusic.model.bean.local.Img;
import com.example.q.pocketmusic.model.bean.local.LocalSong;
import com.example.q.pocketmusic.model.db.LocalSongDao;
import com.example.q.pocketmusic.module.common.BaseFragment;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetBuilder;
import com.github.rubensousa.bottomsheetbuilder.BottomSheetMenuDialog;
import com.github.rubensousa.bottomsheetbuilder.adapter.BottomSheetItemClickListener;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class LocalSongFragment extends BaseFragment implements LocalSongFragmentPresenter.IView, SwipeRefreshLayout.OnRefreshListener, RecyclerArrayAdapter.OnItemClickListener, LocalSongFragmentAdapter.OnItemSelectListener {
    @BindView(R.id.recycler)
    EasyRecyclerView recycler;
    private LocalSongFragmentPresenter presenter;
    private LocalSongFragmentAdapter adapter;


    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public int setContentResource() {
        return R.layout.fragment_local_song;
    }

    @Override
    public void setListener() {
        adapter = new LocalSongFragmentAdapter(getContext());
        adapter.setOnItemClickListener(this);
        adapter.setOnSelectListener(this);
        recycler.setRefreshListener(this);
    }

    @Override
    public void init() {
        presenter = new LocalSongFragmentPresenter(getActivity(), this);
        initRecyclerView(recycler, adapter, 1,true);
        initView();
    }

    private void initView() {
        recycler.setEmptyView(R.layout.view_local_empty);
        if (adapter.getCount() == 0) {
            recycler.showEmpty();
        }
    }

    private void alertBottomDialog(final int position) {
        BottomSheetMenuDialog dialog = new BottomSheetBuilder(getContext())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .setMenu(R.menu.menu_home_local)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.upload://????????????
                                presenter.enterShareActivity(adapter.getItem(position));
                                break;
                            case R.id.delete://??????
                                showDeleteDialog(position);
                                break;
                            case R.id.top:
                                presenter.setTop(adapter.getItem(position));//??????
                                break;
                        }
                    }
                })
                .createDialog();
        dialog.show();

    }


    @Override
    public void onStart() {
        super.onStart();
        presenter.loadLocalSong();
    }

    //?????????????????????addAll????????????????????????List?????????,
    @Override
    public void setList(List<LocalSong> lists) {
        adapter.clear();
        adapter.addAll(lists);
        //??????+??????
        adapter.sort(new LocalSongComparator());
        //test();
    }


    private void test() {
        LocalSongDao localSongDao = new LocalSongDao(getContext());
        List<LocalSong> list = localSongDao.queryForAll();
        Log.e("ttt", "????????????" + list.size());
        for (int i = 0; i < list.size(); i++) {
            LocalSong localSong = localSongDao.findBySongId(list.get(i).getId());
            Log.e("TAG", "Top:" + localSong.getSort());
            ForeignCollection<Img> imgs = localSong.getImgs();
            Log.e("ttt", "????????????????????????" + imgs.size());
            CloseableIterator<Img> iterator = imgs.closeableIterator();
            try {
                while (iterator.hasNext()) {
                    Img img1 = iterator.next();
                    Log.e("ttt", img1.toString());
                }
            } finally {
                try {
                    iterator.close();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //???????????????dialog
    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("????????????")
                .setMessage("????????????:\n" + adapter.getItem(position).getName())
                .setIcon(R.drawable.ico_setting_error)
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.deleteSong(adapter.getItem(position));
                        onRefresh();
                        if (adapter.getCount() == 0) {
                            recycler.showEmpty();
                        }
                    }
                })
                .setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onRefresh() {
        presenter.synchronizedSong();
    }

    @Override
    public void onItemClick(int position) {
        LocalSong localSong = adapter.getItem(position);
        presenter.enterPictureActivity(localSong);
    }

    @Override
    public void onSelected(int position) {
        alertBottomDialog(position);
    }
}
