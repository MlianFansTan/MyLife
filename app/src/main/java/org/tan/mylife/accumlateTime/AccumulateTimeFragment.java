package org.tan.mylife.accumlateTime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import org.litepal.crud.DataSupport;
import org.tan.mylife.MainActivity;
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

    private Fragment itemManager;

    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefresh;

    private List<TimeItem> timeItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    private Boolean isInService = false;        //服务是否正在运行的标志
    private int currentPosition;        //正在进行服务的项的position
    private int time;                   //服务停止时传入的累积时间

    private int clickedPosition;        //被点击的某项

    //与服务进行连接的Binder
    private AccumulateTimeService.AccumulateBinder accumulateBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Yes", "Binder绑定了！");
            accumulateBinder = (AccumulateTimeService.AccumulateBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Yes", "进入onCreateView方法");
        initItem();         //先从数据库拿到所有项的记录
        //Log.d("Yes", timeItems.get(1).getItemTitle());

        //绑定是异步执行的，所以要先进行绑定，防止binder空指针
        Intent bindIntent = new Intent(getActivity(), AccumulateTimeService.class);
        getActivity().bindService(bindIntent, connection, Context.BIND_AUTO_CREATE);
        //Log.d("Yes", "绑定完成");

        View view = inflater.inflate(R.layout.fragment_accumlate, container, false);
        initFab(view);      //初始化FloatingActionButton
        initSwipRefresh(view);   //初始化swipRefresh

        recyclerView = (RecyclerView) view.findViewById(R.id.accumulate_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(timeItems);
        recyclerView.setAdapter(adapter);
        adapter.setmOnItemClickListener(new ItemAdapter.OnItemClickListener(){

            //点击某项开始计时
            @Override
            public void onClick(View view, int position) {
                TimeItem t = timeItems.get(position);
                //Toast.makeText(getActivity(),t.getItemTitle(),Toast.LENGTH_SHORT).show();
                //Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"该项的position为："+String.valueOf(position));
                //Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"原来的时间为："+String.valueOf(t.getMinNums()));
                //Log.d("Yes:"+AccumulateTimeFragment.class.toString(),"该项的id为："+String.valueOf(t.getId()));
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

            //点击某项进入管理
            @Override
            public void onItemClick(int position) {
                clickedPosition = position;
                Intent intent = new Intent(getActivity(), ItemManeger.class);
                intent.putExtra("acT_from", "itemClick");
                intent.putExtra("item_from", timeItems.get(position));
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    //初始化SwipRefresh
    private void initSwipRefresh(View view){
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                upload();
            }
        });
    }

    /**
     * 下拉刷新，上传数据
     */
    private void upload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //初始化FloatingActionButton的逻辑
    private void initFab(View view){
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), ItemManeger.class);
                intent.putExtra("acT_from", "fab");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == 10){
                    Log.d("Yes","             返回数据了!");
                    TimeItem item = (TimeItem) data.getParcelableExtra("save_date");
                    Log.d("Yes",item.getItemTitle());
                    item.save();
                    TimeItem item1 = DataSupport.findLast(TimeItem.class);
                    timeItems.add(item1);
                    adapter.notifyDataSetChanged();
                }
                if (resultCode == 20){
                    TimeItem t = timeItems.get(clickedPosition);
                    TimeItem item2 = (TimeItem) data.getParcelableExtra("save_date");
                    t.setItemTitle(item2.getItemTitle());
                    t.setItemMessage(item2.getItemMessage());
                    t.setImageId(item2.getImageId());
                    t.setAimLevel(item2.getAimLevel());
                    t.setAimHour(item2.getAimHour());
                    t.setAimDate(item2.getAimDate());
                    t.setEveryDayHour(item2.getEveryDayHour());
                    adapter.notifyItemChanged(clickedPosition);
                    t.update(t.getId());
                }
                if (resultCode == 30){
                    TimeItem t = timeItems.get(clickedPosition);
                    t.delete();
                    timeItems.remove(clickedPosition);
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    //从数据库中得到timeItems
    private void initItem(){
        if (DataSupport.isExist(TimeItem.class))
            timeItems = DataSupport.findAll(TimeItem.class);
        else{
            TimeItem timeItem = new TimeItem();
            timeItem.setItemTitle("高树高数");
            timeItem.setItemMessage("难，但是还是有意思");
            timeItem.setImageId(R.mipmap.study);
            timeItem.setMinNums(240);
            timeItem.setAimLevel("业界Top5%");
            timeItem.setAimHour("5400");
            timeItem.setAimDate("2017-10-09");
            timeItem.setEveryDayHour("8");
            timeItem.save();
            timeItems = DataSupport.findAll(TimeItem.class);
        }
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
