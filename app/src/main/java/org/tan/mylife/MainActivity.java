package org.tan.mylife;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private AccumulateTimeFragment accumulateTimeFragment;
    private TomatoFragment tomatoFragment;
    private DiaryFragment diaryFragment;
    private GTDFragment gtdFragment;
    private QuadrantFragment quadrantFragment;

    //这里所有的变量用来隐藏fragment和显现fragment
    private Fragment currentFragment = new Fragment();
    private int currentIndex = 0;       //记录当前的fragment的索引
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //Activity中添加BottomNavigationItem
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        //bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.banana_pic,"积累").setActiveColorResource(R.color.blue).setInActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.apple_pic,"番茄").setActiveColorResource(R.color.red).setInActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.mipmap.grape_pic,"记录").setActiveColorResource(R.color.purple).setInActiveColorResource(R.color.purple))
                .addItem(new BottomNavigationItem(R.mipmap.watermelon_pic,"紧急").setActiveColorResource(R.color.pink).setInActiveColorResource(R.color.pink))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);

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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(CURRENT_FRAGMENT,currentIndex);
        super.onSaveInstanceState(outState);
    }

    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        accumulateTimeFragment = AccumulateTimeFragment.newInstance("累积");
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
                    accumulateTimeFragment = AccumulateTimeFragment.newInstance("积累");
                }
                if (!accumulateTimeFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, accumulateTimeFragment,""+currentIndex);
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(accumulateTimeFragment);
                }
                currentFragment = accumulateTimeFragment;
                break;
            case 1:
                currentIndex = 1;
                if(tomatoFragment == null){
                    tomatoFragment = TomatoFragment.newInstance("番茄");
                }
                if(!tomatoFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, tomatoFragment,""+currentIndex);
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(tomatoFragment);
                }
                currentFragment = tomatoFragment;
                break;
            case 2:
                currentIndex = 2;
                if(diaryFragment == null){
                    diaryFragment= DiaryFragment.newInstance("记录");
                }
                if(!diaryFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, diaryFragment,""+currentIndex);
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(diaryFragment);
                }
                currentFragment = diaryFragment;
                break;
            case 3:
                currentIndex = 3;
                if(quadrantFragment == null){
                    quadrantFragment= QuadrantFragment.newInstance("紧急");
                }
                if(!quadrantFragment.isAdded()){
                    transaction
                            .hide(currentFragment)
                            .add(R.id.tb, quadrantFragment,""+currentIndex);
                }else{
                    transaction
                            .hide(currentFragment)
                            .show(quadrantFragment);
                }
                currentFragment = quadrantFragment;
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
}
