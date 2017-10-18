package org.tan.mylife.accumlateTime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;
import org.tan.mylife.R;
import org.tan.mylife.service.AccumulateTimeService;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by a on 2017/10/8.
 */

public class AccumulateTimeFragment extends Fragment {
    //空构造函数
    public AccumulateTimeFragment(){

    }

    private List<TimeItem> timeItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    private Boolean isInService = false;        //服务是否正在运行的标志
    private int currentPosition;        //正在进行服务的项的position
    private int time;

    //与服务进行连接
    private AccumulateTimeService.AccumulateBinder accumulateBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            accumulateBinder = (AccumulateTimeService.AccumulateBinder) service;
            Log.d("Yes","被调用了！");
            }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initItem();         //先从数据库拿到所有项的记录

        //绑定是异步执行的，所以要先进行绑定，防止binder空指针
        Intent bindIntent = new Intent(getActivity(), AccumulateTimeService.class);
        getActivity().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);

        View view = inflater.inflate(R.layout.fragment_accumlate, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.accumulate_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(timeItems);
        recyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new ItemAdapter.OnItemClickListener(){
            @Override
            public void onClick(View view, int position) {
                TimeItem t = timeItems.get(position);
                Toast.makeText(getActivity(),t.getItemTitle(),Toast.LENGTH_SHORT).show();
                Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"该项的position为："+String.valueOf(position));
                Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"原来的时间为："+String.valueOf(t.getMinNums()));
                Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"该项的id为："+String.valueOf(t.getId()));
                if (isInService == false){
                    accumulateBinder.startAccumulate(t.getItemTitle(),t.getImageId());
                    isInService = true;
                    currentPosition = position;
                    Glide.with(AccumulateTimeFragment.this).load(R.drawable.end).into((CircleImageView)view.findViewById(R.id.change_view));
                }else if (currentPosition == position){
                    time = accumulateBinder.stopAccumulateTime();
                    Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"累积的时间为"+String.valueOf(time));
                    isInService = false;
                    Glide.with(AccumulateTimeFragment.this).load(R.drawable.start).into((CircleImageView)view.findViewById(R.id.change_view));
                    t.setMinNums(t.getMinNums()+time);
                    Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"累积后时间："+String.valueOf(t.getMinNums()));
                    adapter.notifyItemChanged(currentPosition);
                    t.update(t.getId());
                }
            }
        });

        return view;
    }

    //从数据库中得到timeItems
    private void initItem(){
        timeItems = DataSupport.findAll(TimeItem.class);
    }






    /*private void initItem(){
        //timeItems.add(new TimeItem(1,"哈哈哈","哈哈哈",R.mipmap.sleeping, 50));
        //timeItems.add(new TimeItem(2,"呵呵呵","呵呵呵",R.mipmap.study, 60));
        /*TimeItem t1 = new TimeItem();
        t1.setItemTitle("哈哈哈");
        t1.setItemMessage("哈哈哈哈");
        t1.setImageId(R.mipmap.eating);
        t1.setMinNums(50);
        t1.save();
        TimeItem t2 = new TimeItem();
        t2.setItemTitle("呵呵呵");
        t2.setItemMessage("呵呵呵");
        t2.setImageId(R.mipmap.game);
        t2.setMinNums(60);
        t2.save();
        //DataSupport.deleteAll(TimeItem.class);
    }*/
    //替代构造函数的静态方法（预留）
    /*public static AccumulateTimeFragment newInstance(String param1){
        AccumulateTimeFragment fragment = new AccumulateTimeFragment();
        Bundle args = new Bundle();
        args.putString("args1",param1);
        fragment.setArguments(args);
        return fragment;
    }*/
}
