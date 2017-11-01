package org.tan.mylife.diary;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tan.mylife.R;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by a on 2017/10/8.
 */

public class DiaryFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{

    /**
     * 拿到控件
     */

    private SegmentedGroup SG_diary_topbar;
    private RadioButton But_diary_topbar_entries, But_diary_topbar_diary;
    private ViewPager ViewPager_diary_content;

    /**
     * View pager
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);


        SG_diary_topbar = (SegmentedGroup) view.findViewById(R.id.SG_diary_topbar);
        SG_diary_topbar.setOnCheckedChangeListener(this);
        But_diary_topbar_entries = (RadioButton) view.findViewById(R.id.But_diary_topbar_entries);
        But_diary_topbar_diary = (RadioButton) view.findViewById(R.id.But_diary_topbar_diary);

        initViewPager(view);

        return view;
    }

    /**
     * Init Viewpager
     */

    private void initViewPager(View view){
        ViewPager_diary_content = (ViewPager)  view.findViewById(R.id.ViewPager_diary_content);
        ViewPager_diary_content.setOffscreenPageLimit(1);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        ViewPager_diary_content.setAdapter(mPagerAdapter);
        ViewPager_diary_content.addOnPageChangeListener(onPageChangeListener);

    }

    public void gotoPage(int position){
        ViewPager_diary_content.setCurrentItem(position);
    }

    public void callEntriesListRefresh() {

        EntriesFragment entriesFragment = ((EntriesFragment) mPagerAdapter.getRegisteredFragment(0));
        if (entriesFragment != null) {
            entriesFragment.updateEntriesData();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch(checkedId){
            case R.id.But_diary_topbar_entries:
                ViewPager_diary_content.setCurrentItem(0);
                break;
            case R.id.But_diary_topbar_diary:
                ViewPager_diary_content.setCurrentItem(1);
                break;
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            switch(position){
                default:
                    But_diary_topbar_entries.setChecked(true);
                    break;
                case 1:
                    But_diary_topbar_diary.setChecked(true);
                    break;
            }
        }
    };

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter{
        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public ScreenSlidePagerAdapter(FragmentManager fm){super(fm);}

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch(position){
                default:
                    fragment = new EntriesFragment();
                    break;
                case 1:
                    fragment = new WriteDiaryFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }

    //替代构造函数的静态方法（预留）
    /*public static DiaryFragment newInstance(String param1){
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putString("args1",param1);
        fragment.setArguments(args);
        return fragment;
    }*/
}
