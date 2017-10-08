package org.tan.mylife;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{

    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private AccumulateTimeFragment accumulateTimeFragment;
    private TomatoFragment tomatoFragment;
    private DiaryFragment diaryFragment;
    private GTDFragment gtdFragment;
    private QuadrantFragment quadrantFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Activity中添加BottomNavigationItem
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.apple_pic,"积累").setActiveColorResource(R.color.red).setInActiveColorResource(R.color.red))
                .addItem(new BottomNavigationItem(R.mipmap.banana_pic,"番茄").setActiveColorResource(R.color.yellow).setInActiveColorResource(R.color.yellow))
                .addItem(new BottomNavigationItem(R.mipmap.grape_pic,"记录").setActiveColorResource(R.color.purple).setInActiveColorResource(R.color.purple))
                .addItem(new BottomNavigationItem(R.mipmap.watermelon_pic,"紧急").setActiveColorResource(R.color.green).setInActiveColorResource(R.color.green))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    private void setDefaultFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        accumulateTimeFragment = AccumulateTimeFragment.newInstance("累积");
        transaction.add(R.id.tb, accumulateTimeFragment);
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position){
            case 0:
                if(accumulateTimeFragment == null){
                    accumulateTimeFragment = AccumulateTimeFragment.newInstance("积累");
                }
                if (!accumulateTimeFragment.isAdded()){
                    Log.d("Yes","accumulateTimeFragment没有被添加");
                    transaction.add(R.id.tb, accumulateTimeFragment);
                }else{
                    Log.d("Yes","accumulateTimeFragment被添加了!");    //判断是否被活动的fragmentManager管理
                    transaction.show(accumulateTimeFragment);
                }
                break;
            case 1:
                if(tomatoFragment == null){
                    tomatoFragment = TomatoFragment.newInstance("番茄");
                }
                if(!tomatoFragment.isAdded()){
                    transaction.add(R.id.tb, tomatoFragment);
                }else{
                    transaction.show(tomatoFragment);
                }
                break;
            case 2:
                if(diaryFragment == null){
                    diaryFragment= DiaryFragment.newInstance("记录");
                }
                if(!diaryFragment.isAdded()){
                    transaction.add(R.id.tb, diaryFragment);
                }else{
                    transaction.show(diaryFragment);
                }
                break;
            case 3:
                if(quadrantFragment == null){
                    quadrantFragment= QuadrantFragment.newInstance("紧急");
                }
                if(!quadrantFragment.isAdded()){
                    transaction.add(R.id.tb, quadrantFragment);
                }else{
                    transaction.show(quadrantFragment);
                }
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
