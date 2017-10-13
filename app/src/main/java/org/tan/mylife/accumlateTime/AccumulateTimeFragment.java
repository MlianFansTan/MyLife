package org.tan.mylife.accumlateTime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    private List<TimeItem> timeItems;

    public AccumulateTimeFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initItem();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accumlate, container, false);

        return view;
    }

    //从数据库中得到timeItems
    /*private void initItem(){
        timeItems = DataSupport.findAll(TimeItem.class);
    }*/
    private void initItem(){
        //TimeItem item1 =
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
