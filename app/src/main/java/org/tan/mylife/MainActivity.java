package org.tan.mylife;

import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.tan.mylife.accumlateTime.AccumulateTimeFragment;
import org.tan.mylife.diary.DiaryFragment;
import org.tan.mylife.record.RecordFragment;
import org.tan.mylife.tomato.TomatoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    /*
     * Back button event
     */
    private static Boolean isExit = false;
    private Timer backTimer = new Timer();

    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    private DrawerLayout drawerLayout;
    private BottomNavigationBar bottomNavigationBar;
    private Toolbar toolbar;

    private int lastSelectedPosition = 0;
    private AccumulateTimeFragment accumulateTimeFragment;
    private TomatoFragment tomatoFragment;
    private DiaryFragment diaryFragment;
    private RecordFragment recordFragment;

    //这里所有的变量用来隐藏fragment和显现fragment的全局变量
    private Fragment currentFragment = new Fragment();
    private int currentIndex = 0;           //记录当前的fragment的索引
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();      //初始化toolbar

        initBottomNavigationBar();//初始化BottomNavigationBar

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(savedInstanceState != null){     //内存重启时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0+""));
            fragments.add(fragmentManager.findFragmentByTag(1+""));
            fragments.add(fragmentManager.findFragmentByTag(2+""));
            fragments.add(fragmentManager.findFragmentByTag(3+""));

            //恢复fragment页面
            restoreFragment();
        }else{
            setDefaultFragment();
        }
    }

    //在这里定义初始化toolbar的逻辑
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    //定义初始化BottomNavigationBar的逻辑
    private void initBottomNavigationBar(){

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        //bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.banana_pic,"积累").setActiveColorResource(R.color.blue).setInActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.apple_pic,"番茄").setActiveColorResource(R.color.red).setInActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.mipmap.grape_pic,"记录").setActiveColorResource(R.color.purple).setInActiveColorResource(R.color.purple))
                .addItem(new BottomNavigationItem(R.mipmap.watermelon_pic,"日记").setActiveColorResource(R.color.pink).setInActiveColorResource(R.color.pink))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
    }

    //当内存被清理时保存当前运行的fragment的索引
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(CURRENT_FRAGMENT,currentIndex);
        super.onSaveInstanceState(outState);
    }

    //设置默认的fragment为accumulateTimeFragment
    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        accumulateTimeFragment = new AccumulateTimeFragment();
        transaction.add(R.id.tb, accumulateTimeFragment,""+currentIndex);
        currentFragment = accumulateTimeFragment;
        transaction.commit();
    }

    //内存重启时，将原fragment重新加载出来
    private void restoreFragment(){
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        for(int i = 0; i < fragments.size(); i++){
            if(i == currentIndex)
                transaction.show(fragments.get(i));
            else
                transaction.hide(fragments.get(i));
        }
    }

    //重写BottomNavigationBar.OnTabSelectedListener的三个抽象方法
    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position){
            case 0:
                currentIndex = 0;
                if(accumulateTimeFragment == null){
                    accumulateTimeFragment = new AccumulateTimeFragment();
                }
                if (!accumulateTimeFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, accumulateTimeFragment,""+currentIndex);
                    toolbar.setBackgroundColor(Color.parseColor("#87CEFA"));
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(accumulateTimeFragment);
                    toolbar.setBackgroundColor(Color.parseColor("#87CEFA"));
                }
                currentFragment = accumulateTimeFragment;
                break;
            case 1:
                currentIndex = 1;
                if(tomatoFragment == null){
                    tomatoFragment = new TomatoFragment();
                }
                if(!tomatoFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, tomatoFragment,""+currentIndex);
                    toolbar.setBackgroundColor(Color.parseColor("#FF4500"));
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(tomatoFragment);
                    toolbar.setBackgroundColor(Color.parseColor("#FF4500"));
                }
                currentFragment = tomatoFragment;
                break;
            case 2:
                currentIndex = 3;
                if(recordFragment == null){
                    recordFragment= new RecordFragment();
                }
                if(!recordFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, recordFragment,""+currentIndex);
                    toolbar.setBackgroundColor(Color.parseColor("#4B0082"));
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(recordFragment);
                    toolbar.setBackgroundColor(Color.parseColor("#4B0082"));
                }
                currentFragment = recordFragment;
                break;
            case 3:
                currentIndex = 2;
                if(diaryFragment == null){
                    diaryFragment= new DiaryFragment();
                }
                if(!diaryFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, diaryFragment,""+currentIndex);
                    toolbar.setBackgroundColor(Color.parseColor("#FFB6C1"));
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(diaryFragment);
                    toolbar.setBackgroundColor(Color.parseColor("#FFB6C1"));
                }
                currentFragment = diaryFragment;
                break;
            default:
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }


    /*
     *双击返回键退出
     */
    @Override
    public void onBackPressed() {
    if (!isExit){
        isExit = true;
        Toast.makeText(this, "再点一次推出哦~", Toast.LENGTH_SHORT).show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isExit = false;
            }
        };
        backTimer.schedule(task, 2000);
    }else {
        super.onBackPressed();
        }
    }
}
