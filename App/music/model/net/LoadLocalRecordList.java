package com.example.q.pocketmusic.model.net;

import android.content.Context;
import android.os.AsyncTask;

import com.example.q.pocketmusic.model.bean.local.RecordAudio;
import com.example.q.pocketmusic.model.db.RecordAudioDao;

import java.util.List;



public class LoadLocalRecordList extends AsyncTask<Void, Void, List<RecordAudio>> {
    private Context context;
    private RecordAudioDao recordAudioDao;

    public LoadLocalRecordList(Context context, RecordAudioDao recordAudioDao) {
        this.context = context;
        this.recordAudioDao = recordAudioDao;
    }

    @Override
    protected List<RecordAudio> doInBackground(Void... params) {
        return recordAudioDao.queryForAll();
    }


}
