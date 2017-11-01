package org.tan.mylife.diary;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by a on 2017/10/28.
 */

public class EntriesEntity extends DataSupport implements Comparable<CalendarDay>{

    private int id;
    private String title;
    private String summary;
    private int weatherImgId;
    private int moodImgId;
    private Date createDate;

    public EntriesEntity(){};

    public EntriesEntity(int id, String title, String summary, int weatherImgId, int moodImgId,Date createDate) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.weatherImgId = weatherImgId;
        this.moodImgId = moodImgId;
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getWeatherImgId() {
        return weatherImgId;
    }

    public void setWeatherImgId(int weatherImgId) {
        this.weatherImgId = weatherImgId;
    }

    public int getMoodImgId() {
        return moodImgId;
    }

    public void setMoodImgId(int moodImgId) {
        this.moodImgId = moodImgId;
    }

    @Override
    public int compareTo(@NonNull CalendarDay calendarDay) {
        //TODO improve the compare performance
        Calendar cal = Calendar.getInstance();
        cal.setTime(createDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return Long.valueOf(calendarDay.getCalendar().getTimeInMillis()).compareTo(
                cal.getTimeInMillis());
    }
}
