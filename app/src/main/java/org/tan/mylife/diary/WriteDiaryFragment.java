package org.tan.mylife.diary;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.tan.mylife.R;
import org.tan.mylife.diary.spinnerAdapter.ImageArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by a on 2017/10/28.
 */

public class WriteDiaryFragment extends Fragment implements View.OnClickListener{

    private String TAG = "DiaryFragment";

    /**
     *  天气图片ID线性表
     */
    private Integer[] weatherArray = new Integer[]{R.drawable.ic_weather_sunny, R.drawable.ic_weather_cloud,
            R.drawable.ic_weather_windy, R.drawable.ic_weather_rainy, R.drawable.ic_weather_snowy,
            R.drawable.ic_weather_foggy};

    /**
     *  心情图片id资源
     */
    private Integer[] moodArray = new Integer[]{R.drawable.ic_mood_happy, R.drawable.ic_mood_soso,
            R.drawable.ic_mood_unhappy};

    /*
        月份和星期
     */
    private String[] MonthsFullName;
    private String[] DaysFullName;

    /*
     * UI
     */
    private ScrollView ScrollView_diary_content;
    private LinearLayout  LL_diary_time_information;
    private RelativeLayout RL_diary_info;
    private TextView TV_diary_month, TV_diary_date, TV_diary_day, TV_diary_time;

    private Spinner SP_diary_weather, SP_diary_mood;
    private EditText EDT_diary_title;
    private ImageView IV_diary_clear_visi, IV_diary_save_visi;
    private EditText ED_diary_item_content;

    /*
     * Time
     */
    private Calendar calendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MonthsFullName = getActivity().getResources().getStringArray(R.array.months_full_name);
        DaysFullName = getActivity().getResources().getStringArray(R.array.days_full_name);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_writediary, container, false);

        ScrollView_diary_content = (ScrollView) rootView.findViewById(R.id.ScrollView_diary_content);

        RL_diary_info = (RelativeLayout) rootView.findViewById(R.id.RL_diary_info);
        RL_diary_info.getBackground().setAlpha(100);
        LL_diary_time_information = (LinearLayout) rootView.findViewById(R.id.LL_diary_time_information);
        LL_diary_time_information.setOnClickListener(this);
        TV_diary_month = (TextView) rootView.findViewById(R.id.TV_diary_month);
        TV_diary_date = (TextView) rootView.findViewById(R.id.TV_diary_date);
        TV_diary_day = (TextView) rootView.findViewById(R.id.TV_diary_day);
        TV_diary_time = (TextView) rootView.findViewById(R.id.TV_diary_time);

        SP_diary_weather = (Spinner) rootView.findViewById(R.id.SP_diary_weather);
        SP_diary_weather.setVisibility(View.VISIBLE);
        SP_diary_mood = (Spinner) rootView.findViewById(R.id.SP_diary_mood);
        SP_diary_mood.setVisibility(View.VISIBLE);

        //在写日记时，这些控件显示
        EDT_diary_title = (EditText) rootView.findViewById(R.id.EDT_diary_title);
        EDT_diary_title.getBackground().mutate().setColorFilter(getResources().getColor(R.color.hotpink), PorterDuff.Mode.SRC_ATOP);
        //日记内容控件
        ED_diary_item_content = (EditText) rootView.findViewById(R.id.ED_diary_item_content);
        IV_diary_clear_visi = (ImageView) rootView.findViewById(R.id.IV_diary_clear_visi);
        IV_diary_clear_visi.setOnClickListener(this);
        IV_diary_save_visi = (ImageView) rootView.findViewById(R.id.IV_diary_save_visi);
        IV_diary_save_visi.setOnClickListener(this);

        initWeatherSpinner();
        initMoodSpinner();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentTime(true);
    }

    private void initWeatherSpinner(){
        ImageArrayAdapter weatherArrayAdapter = new ImageArrayAdapter(getActivity(), weatherArray);
        SP_diary_weather.setAdapter(weatherArrayAdapter);
    }

    private void initMoodSpinner(){
        ImageArrayAdapter moodArrayAdapter = new ImageArrayAdapter(getActivity(), moodArray);
        SP_diary_mood.setAdapter(moodArrayAdapter);
    }

    private void setCurrentTime(boolean updateCurrentTime) {
        calendar = Calendar.getInstance();
        if (updateCurrentTime) {
            calendar.setTimeInMillis(System.currentTimeMillis());
        }
        TV_diary_month.setText(MonthsFullName[calendar.get(Calendar.MONTH)]);
        TV_diary_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        TV_diary_day.setText(DaysFullName[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        TV_diary_time.setText(sdf.format(calendar.getTime()));
    }

    /**
     * 清空UI
     */
    private void clearDiaryPage(){
        SP_diary_mood.setSelection(0);
        SP_diary_weather.setSelection(0);
        EDT_diary_title.setText("");
        ED_diary_item_content.setText("");
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.IV_diary_clear_visi:
                clearDiaryPage();
                break;
            case R.id.IV_diary_save_visi:
                if (!EDT_diary_title.getText().toString().equals("")){
                    EntriesEntity entity = new EntriesEntity();
                    entity.setTitle(EDT_diary_title.getText().toString());
                    entity.setSummary(ED_diary_item_content.getText().toString());
                    entity.setMoodImgId(SP_diary_weather.getSelectedItemPosition());
                    entity.setWeatherImgId(SP_diary_mood.getSelectedItemPosition());
                    entity.setCreateDate(new Date(System.currentTimeMillis()));
                    entity.save();

                    ((DiaryFragment)getParentFragment()).callEntriesListRefresh();
                    ((DiaryFragment)getParentFragment()).gotoPage(0);

                    clearDiaryPage();
                }else
                    Toast.makeText(getActivity(), "没有标题吗？",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
