package org.tan.mylife.accumlateTime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.tan.mylife.R;
import org.tan.mylife.Util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2017/10/8.
 */

public class AccumulateTimeFragment extends Fragment {

    private List<TimeItem> timeItems = new ArrayList<>();
    RecyclerView recyclerView;
    private ItemAdapter adapter;

    public AccumulateTimeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initItem();
        View view = inflater.inflate(R.layout.fragment_accumlate, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.accumulate_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(timeItems);
        recyclerView.setAdapter(adapter);
        return view;
    }

    //从数据库中得到timeItems
    /*private void initItem(){
        timeItems = DataSupport.findAll(TimeItem.class);
    }*/
    private void initItem(){
        timeItems.add(new TimeItem(1,"哈哈哈","哈哈哈",R.mipmap.sleeping, 50));
        timeItems.add(new TimeItem(2,"呵呵呵","呵呵呵",R.mipmap.study, 60));
    }
    //替代构造函数的静态方法（预留）
    /*public static AccumulateTimeFragment newInstance(String param1){
        AccumulateTimeFragment fragment = new AccumulateTimeFragment();
        Bundle args = new Bundle();
        args.putString("args1",param1);
        fragment.setArguments(args);
        return fragment;
    }*/
}
