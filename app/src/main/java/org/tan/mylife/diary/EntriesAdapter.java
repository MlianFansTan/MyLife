package org.tan.mylife.diary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.tan.mylife.R;
import org.tan.mylife.diary.DiaryDialog.DiaryViewerDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by a on 2017/10/29.
 */

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.EntriesViewHolder> {

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

    private Context mContext;
    private List<EntriesEntity> mEntriesList;
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private EntriesFragment mFragment;
    private String[] daysSimpleName;

    public EntriesAdapter(EntriesFragment fragment, List<EntriesEntity> list){
        this.mFragment = fragment;
        this.mEntriesList = list;
        this.daysSimpleName = mFragment.getResources().getStringArray(R.array.days_simple_name);
    }

    @Override
    public EntriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.entries_item_diary, parent, false);
        final EntriesViewHolder holder = new EntriesViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiaryViewerDialogFragment diaryViewerDialog = DiaryViewerDialogFragment.newInstance(mEntriesList.get(holder.getAdapterPosition()).getId(),
                        false);
                diaryViewerDialog.setDiaryViewerListener(mFragment);
                diaryViewerDialog.show(mFragment.getFragmentManager(), "diaryViewerDialog");
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DiaryViewerDialogFragment diaryViewerDialog = DiaryViewerDialogFragment.newInstance(mEntriesList.get(holder.getAdapterPosition()).getId(),
                        true);
                diaryViewerDialog.setDiaryViewerListener(mFragment);
                diaryViewerDialog.show(mFragment.getFragmentManager(), "diaryViewerDialog");
                return true;
            }
        });
        return holder;
    }

    @Override
    public int getItemCount() {
        return mEntriesList.size();
    }

    @Override
    public void onBindViewHolder(EntriesViewHolder holder, int position) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mEntriesList.get(position).getCreateDate());

        if (showHeader(position)){
            holder.TV_entries_item_header.setVisibility(View.VISIBLE);
            holder.TV_entries_item_header.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        }else{
            holder.TV_entries_item_header.setVisibility(View.GONE);
        }

        holder.TV_entries_item_date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.TV_entries_item_day.setText(daysSimpleName[calendar.get(Calendar.DAY_OF_WEEK) - 1]);
        holder.TV_entries_item_time.setText(String.valueOf(dateFormat.format(calendar.getTime())));
        holder.TV_entries_item_title.setText(mEntriesList.get(position).getTitle());
        holder.TV_entries_item_summary.setText(mEntriesList.get(position).getSummary());

        holder.IV_entries_item_weather.setImageResource(weatherArray[mEntriesList.get(position).getWeatherImgId()]);
        holder.IV_entries_item_mood.setImageResource(moodArray[mEntriesList.get(position).getMoodImgId()]);
    }

    private boolean showHeader(final int position){
        if (position == 0)
            return true;   //这里和大神的代码有点不同
        else{
            Calendar previousCalendar = new GregorianCalendar();
            previousCalendar.setTime(mEntriesList.get(position - 1).getCreateDate());
            Calendar currentCalendar = new GregorianCalendar();
            currentCalendar.setTime(mEntriesList.get(position).getCreateDate());
            if (previousCalendar.get(Calendar.YEAR) != currentCalendar.get(Calendar.YEAR)) {
                return true;
            } else {
                if (previousCalendar.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    protected class EntriesViewHolder extends RecyclerView.ViewHolder{
        private TextView TV_entries_item_header;
        private TextView TV_entries_item_date, TV_entries_item_day, TV_entries_item_time,
                TV_entries_item_title, TV_entries_item_summary;

        private ImageView IV_entries_item_weather, IV_entries_item_mood, IV_entries_item_bookmark;

        private RelativeLayout RL_entries_item_content;

        public EntriesViewHolder(View rootView){
            super(rootView);
            this.TV_entries_item_header = (TextView) rootView.findViewById(R.id.TV_entries_item_header);

            this.TV_entries_item_date = (TextView) rootView.findViewById(R.id.TV_entries_item_date);
            this.TV_entries_item_day = (TextView) rootView.findViewById(R.id.TV_entries_item_day);
            this.TV_entries_item_time = (TextView) rootView.findViewById(R.id.TV_entries_item_time);
            this.TV_entries_item_title = (TextView) rootView.findViewById(R.id.TV_entries_item_title);
            this.TV_entries_item_summary = (TextView) rootView.findViewById(R.id.TV_entries_item_summary);

            this.IV_entries_item_weather = (ImageView) rootView.findViewById(R.id.IV_entries_item_weather);
            this.IV_entries_item_mood = (ImageView) rootView.findViewById(R.id.IV_entries_item_mood);
            this.IV_entries_item_bookmark = (ImageView) rootView.findViewById(R.id.IV_entries_item_bookmark);

            this.RL_entries_item_content = (RelativeLayout) rootView.findViewById(R.id.RL_entries_item_content);
        }

    }
}
