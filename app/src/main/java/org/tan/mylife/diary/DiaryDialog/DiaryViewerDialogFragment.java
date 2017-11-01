package org.tan.mylife.diary.DiaryDialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.tan.mylife.R;
import org.tan.mylife.Util.ScreenHelper;
import org.tan.mylife.diary.EntriesEntity;
import org.tan.mylife.diary.spinnerAdapter.ImageArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by a on 2017/10/31.
 */

public class DiaryViewerDialogFragment extends DialogFragment implements View.OnClickListener {

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

    /**
     * UI
     */
    private ScrollView ScrollView_diary_content;
    private RelativeLayout RL_diary_info;
    private LinearLayout LL_diary_time_information;
    private LinearLayout RL_diary_title_content;

    private TextView TV_diary_month, TV_diary_date, TV_diary_day, TV_diary_time;

    private ImageView IV_diary_weather;
    private TextView TV_diary_weather;
    private RelativeLayout RL_diary_weather, RL_diary_mood;
    private Spinner SP_diary_weather, SP_diary_mood;

    private TextView TV_diary_title_content;
    private EditText EDT_diary_title;
    private EditText ED_diary_item_content;
    private TextView TV_diary_item_content;

    //被隐藏的edit_bar
    private LinearLayout LL_diary_edit_bar;
    private ImageView IV_diary_close_dialog, IV_diary_delete, IV_diary_clear, IV_diary_save;

    //正在展现的edit_bar
    private LinearLayout LL_diary_visible;

    /*
        月份和星期
     */
    private String[] MonthsFullName;
    private String[] DaysFullName;

    private Calendar calendar;
    private SimpleDateFormat sdf;

    /**
     * 是展现模式还是编辑模式
     */
    private boolean isEditMode;

    /**
     * 执行删除或者修改的Listener
     */
    public interface DiaryViewerListener {
        void deleteDiary(int diaryId);

        void updateDiary(int diaryId);
    }

    private DiaryViewerListener listener;

    public void setDiaryViewerListener(DiaryViewerListener listener){
        this.listener = listener;
    }

    /**
     * 现在拿到的日记entity项和id
     */
    private int diaryId;
    private EntriesEntity mEntity;

    public static DiaryViewerDialogFragment newInstance(int diaryId, boolean isEditMode) {
        Bundle args = new Bundle();
        DiaryViewerDialogFragment fragment = new DiaryViewerDialogFragment();
        args.putInt("diaryId", diaryId);
        args.putBoolean("isEditMode", isEditMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEditMode = getArguments().getBoolean("isEditMode", false);
        if (isEditMode) {
            Toast.makeText(getActivity(), "进入编辑模式", Toast.LENGTH_SHORT).show();
        }
        MonthsFullName = getActivity().getResources().getStringArray(R.array.months_full_name);
        DaysFullName = getActivity().getResources().getStringArray(R.array.days_full_name);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(),getTheme()){

        };
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //Set background is transparent , for dialog radius
        dialog.getWindow().getDecorView().getBackground().setAlpha(0);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        diaryId = getArguments().getInt("diaryId");
        if (diaryId != -1){
            initData(diaryId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_writediary, container, false);

        //要在dialog中隐藏的控件
        LL_diary_visible = (LinearLayout) rootView.findViewById(R.id.LL_diary_visible);
        LL_diary_visible.setVisibility(View.GONE);

        RL_diary_info = (RelativeLayout) rootView.findViewById(R.id.RL_diary_info);
        RL_diary_info.setBackground(createDiaryViewerInfoBg(getActivity()));

        //要在dialog中一定显示的控件
        LL_diary_edit_bar = (LinearLayout) rootView.findViewById(R.id.LL_diary_edit_bar);
        LL_diary_edit_bar.setVisibility(View.VISIBLE);
        LL_diary_edit_bar.setBackground(createDiaryViewerEditBarBg(getActivity()));

        IV_diary_clear = (ImageView) rootView.findViewById(R.id.IV_diary_clear);
        IV_diary_delete = (ImageView) rootView.findViewById(R.id.IV_diary_delete);
        IV_diary_save = (ImageView) rootView.findViewById(R.id.IV_diary_save);
        IV_diary_close_dialog = (ImageView) rootView.findViewById(R.id.IV_diary_close_dialog);
        IV_diary_close_dialog.setVisibility(View.VISIBLE);
        IV_diary_close_dialog.setOnClickListener(this);

        LL_diary_time_information = (LinearLayout) rootView.findViewById(R.id.LL_diary_time_information);
        TV_diary_month = (TextView) rootView.findViewById(R.id.TV_diary_month);
        TV_diary_date = (TextView) rootView.findViewById(R.id.TV_diary_date);
        TV_diary_day = (TextView) rootView.findViewById(R.id.TV_diary_day);
        TV_diary_time = (TextView) rootView.findViewById(R.id.TV_diary_time);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView){
        if(isEditMode){     //编辑模式

            SP_diary_mood = (Spinner) rootView.findViewById(R.id.SP_diary_mood);
            //SP_diary_mood.setVisibility(View.VISIBLE);
            SP_diary_weather = (Spinner) rootView.findViewById(R.id.SP_diary_weather);
            //SP_diary_weather.setVisibility(View.VISIBLE);
            IV_diary_clear.setOnClickListener(this);
            IV_diary_save.setOnClickListener(this);
            IV_diary_delete.setOnClickListener(this);

            EDT_diary_title = (EditText) rootView.findViewById(R.id.EDT_diary_title);
            EDT_diary_title.getBackground().mutate().setColorFilter(getResources().getColor(R.color.hotpink), PorterDuff.Mode.SRC_ATOP);

            ED_diary_item_content = (EditText) rootView.findViewById(R.id.ED_diary_item_content);

            initWeatherSpinner();
            initMoodSpinner();

        }else{      //展示模式

            EDT_diary_title = (EditText) rootView.findViewById(R.id.EDT_diary_title);
            EDT_diary_title.setVisibility(View.GONE);
            ED_diary_item_content = (EditText) rootView.findViewById(R.id.ED_diary_item_content);
            ED_diary_item_content.setVisibility(View.GONE);
            RL_diary_weather = (RelativeLayout) rootView.findViewById(R.id.RL_diary_weather);
            RL_diary_weather.setVisibility(View.GONE);
            RL_diary_mood = (RelativeLayout) rootView.findViewById(R.id.RL_diary_mood);
            RL_diary_mood.setVisibility(View.GONE);

            IV_diary_weather = (ImageView) rootView.findViewById(R.id.IV_diary_weather);
            TV_diary_weather = (TextView) rootView.findViewById(R.id.TV_diary_weather);
            IV_diary_weather.setVisibility(View.VISIBLE);
            TV_diary_weather.setVisibility(View.VISIBLE);

            TV_diary_title_content = (TextView) rootView.findViewById(R.id.TV_diary_title_content);
            TV_diary_title_content.setVisibility(View.VISIBLE);
            TV_diary_title_content.setTextColor(getResources().getColor(R.color.hotpink));
            TV_diary_item_content = (TextView) rootView.findViewById(R.id.TV_diary_item_content);
            TV_diary_item_content.setVisibility(View.VISIBLE);

            IV_diary_delete.setOnClickListener(this);
            IV_diary_clear.setVisibility(View.GONE);
            IV_diary_save.setVisibility(View.GONE);

        }
    }

    private void initWeatherSpinner(){
        ImageArrayAdapter weatherArrayAdapter = new ImageArrayAdapter(getActivity(), weatherArray);
        SP_diary_weather.setAdapter(weatherArrayAdapter);
    }

    private void initMoodSpinner(){
        ImageArrayAdapter moodArrayAdapter = new ImageArrayAdapter(getActivity(), moodArray);
        SP_diary_mood.setAdapter(moodArrayAdapter);
    }

    //根据项的id从数据库中加载出来实体
    private void loadEntity(int diaryId){
        mEntity = DataSupport.find(EntriesEntity.class, diaryId);
    }

    private void initData(int diaryId){
        loadEntity(diaryId);
        sdf = new SimpleDateFormat("HH:mm");
        calendar = Calendar.getInstance();
        calendar.setTime(mEntity.getCreateDate());
        setDiaryTime();
        if (isEditMode){
            EDT_diary_title.setText(mEntity.getTitle());
            ED_diary_item_content.setText(mEntity.getSummary());
            SP_diary_weather.setSelection(mEntity.getWeatherImgId());
            SP_diary_mood.setSelection(mEntity.getMoodImgId());
        }else{
            String strTitle = mEntity.getTitle();
            if (strTitle == null || strTitle.equals(""))
                strTitle = "无标题";
            TV_diary_title_content.setText(strTitle);
            TV_diary_item_content.setText(mEntity.getSummary());
            setWeatherAndInfo();
        }
    }

    private void setWeatherAndInfo(){
        int weatherId = mEntity.getWeatherImgId();
        IV_diary_weather.setImageResource(weatherArray[mEntity.getWeatherImgId()]);
        switch (weatherId){
            case 0:
                TV_diary_weather.setText("晴天");
                break;
            case 1:
                TV_diary_weather.setText("多云");
                break;
            case 2:
                TV_diary_weather.setText("有风");
                break;
            case 3:
                TV_diary_weather.setText("雨天");
                break;
            case 4:
                TV_diary_weather.setText("下雪");
                break;
            case 5:
                TV_diary_weather.setText("雾天");
                break;
            default:
                break;
        }
    }

    private void setDiaryTime() {
        TV_diary_month.setText(MonthsFullName[calendar.get(Calendar.MONTH)]);
        TV_diary_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        TV_diary_day.setText(DaysFullName[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        TV_diary_time.setText(sdf.format(calendar.getTime()));
    }

    private Drawable createDiaryViewerInfoBg(Context context) {
        int dp10 = ScreenHelper.dpToPixel(context.getResources(), 10);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getResources().getColor(R.color.pink));
        shape.setCornerRadii(new float[]{dp10, dp10, dp10, dp10, 0, 0, 0, 0});
        return shape;
    }

    private Drawable createDiaryViewerEditBarBg(Context context) {
        int dp10 = ScreenHelper.dpToPixel(context.getResources(), 10);
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(getResources().getColor(R.color.pink));
        shape.setCornerRadii(new float[]{0, 0, 0, 0, dp10, dp10, dp10, dp10});
        return shape;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.IV_diary_close_dialog:
                dismiss();
                break;
            case R.id.IV_diary_clear:
                EDT_diary_title.setText("");
                ED_diary_item_content.setText("");
                break;
            case R.id.IV_diary_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("确定要删除吗？");
                dialog.setMessage("删除后的数据无法还原！");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DataSupport.delete(EntriesEntity.class, diaryId);
                        listener.deleteDiary(diaryId);
                        dismiss();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.IV_diary_save:
                mEntity.setTitle(EDT_diary_title.getText().toString());
                mEntity.setSummary(ED_diary_item_content.getText().toString());
                mEntity.setWeatherImgId(SP_diary_weather.getSelectedItemPosition());
                mEntity.setMoodImgId(SP_diary_mood.getSelectedItemPosition());
                mEntity.update(mEntity.getId());
                dismiss();
                listener.updateDiary(diaryId);
                break;
            default:
                break;
        }
    }
}
