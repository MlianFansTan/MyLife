package org.tan.mylife.diary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.crud.DataSupport;
import org.tan.mylife.R;
import org.tan.mylife.diary.DiaryDialog.DiaryViewerDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by a on 2017/10/28.
 */

public class EntriesFragment extends Fragment implements DiaryViewerDialogFragment.DiaryViewerListener{

    private RecyclerView recyclerView_entries;

    private List<EntriesEntity> entriesList;
    private EntriesAdapter entriesAdapter;
    /**
     * 无参构造
     */
    public EntriesFragment(){};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);

        recyclerView_entries = (RecyclerView) view.findViewById(R.id.RecyclerView_entries);

        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_entries.setLayoutManager(layoutManager);
        getEntriesList();
        entriesAdapter = new EntriesAdapter(EntriesFragment.this, entriesList);
        recyclerView_entries.setAdapter(entriesAdapter);
    }

    //从数据库加载entity项出来
    private void getEntriesList() {

        if (DataSupport.isExist(EntriesEntity.class))
            entriesList = DataSupport.findAll(EntriesEntity.class);
        else{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String dateStr = "2017-10-20 22-01-16";
            Date date = null;
            try {
                date = dateFormat.parse(dateStr);
            }catch (Exception e){
                e.printStackTrace();
            }
            EntriesEntity entity = new EntriesEntity();
            entity.setTitle("第一篇日记~");
            entity.setSummary("本来今天很累的，但是我还是很高兴！");
            entity.setMoodImgId(0);
            entity.setWeatherImgId(4);
            if (date != null)
                entity.setCreateDate(date);
            entity.save();
            entriesList = DataSupport.findAll(EntriesEntity.class);
            }
    }

    //更新列表项
    public void updateEntriesData(){
        EntriesEntity entity = DataSupport.findLast(EntriesEntity.class);
        entriesList.add(entity);
        entriesAdapter.notifyDataSetChanged();
    }

    /**
     *  listener方法重写
     */
    @Override
    public void deleteDiary(int diaryID) {
        int position;
        for (int i = 0; i < entriesList.size(); i ++){
            if (entriesList.get(i).getId() == diaryID){
                position = i;
                entriesList.remove(position);
                break;
            }
        }
        entriesAdapter.notifyDataSetChanged();
        DataSupport.delete(EntriesEntity.class, diaryID);
    }

    @Override
    public void updateDiary(int diaryID) {
        int position;
        for (int i = 0; i < entriesList.size(); i ++){
            if (entriesList.get(i).getId() == diaryID){
                position = i;
                entriesList.remove(position);
                break;
            }
        }
        EntriesEntity entity = DataSupport.find(EntriesEntity.class, diaryID);
        entriesList.add(entity);
        entriesAdapter.notifyDataSetChanged();
    }
}
