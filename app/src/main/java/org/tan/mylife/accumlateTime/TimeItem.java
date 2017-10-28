package org.tan.mylife.accumlateTime;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * 为每一个时间积累项建立Bean类
 *
 * Created by a on 2017/10/10.
 */

public class TimeItem extends DataSupport implements Parcelable{
    private int id;

    private int minNums;        //统计积累了多少分钟

    private String itemTitle;   //每项的标题

    private String itemMessage;     //每一项的描述

    private int imageId;        //每一项用到的图片资源id

    private String aimLevel;    //每一项的目标等级

    private String aimHour;     //每一项所花时间

    private String aimDate;     //目标期限 yy-MM-dd

    private String everyDayHour;   //每天要花多少小时

    public String getAimLevel() {
        return aimLevel;
    }

    public void setAimLevel(String aimLevel) {
        this.aimLevel = aimLevel;
    }

    public String getAimHour() {
        return aimHour;
    }

    public void setAimHour(String aimHour) {
        this.aimHour = aimHour;
    }


    public String getAimDate() {
        return aimDate;
    }

    public void setAimDate(String aimDate) {
        this.aimDate = aimDate;
    }

    public String getEveryDayHour() {
        return everyDayHour;
    }

    public void setEveryDayHour(String everyDayHour) {
        this.everyDayHour = everyDayHour;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemMessage() {
        return itemMessage;
    }

    public void setItemMessage(String itemMessage) {
        this.itemMessage = itemMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMinNums() {
        return minNums;
    }

    public void setMinNums(int minNums) {
        this.minNums = minNums;
    }

    public TimeItem(int id, String itemTitle, String itemMessage, int imageId, int minNums, String aimLevel ,String aimHour, String aimDate, String everyDayHour){
        this.itemTitle = itemTitle;
        this.itemMessage = itemMessage;
        this.minNums = minNums;
        this.id = id;
        this.imageId = imageId;
        this.aimDate = aimDate;
        this.everyDayHour = everyDayHour;
        this.aimLevel = aimLevel;
        this.aimHour = aimHour;
    }

    public TimeItem(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemTitle);
        dest.writeString(itemMessage);
        dest.writeInt(imageId);
        dest.writeString(aimLevel);
        dest.writeString(aimHour);
        dest.writeString(aimDate);
        dest.writeString(everyDayHour);
    }

    public static final Parcelable.Creator<TimeItem> CREATOR = new Parcelable.Creator<TimeItem>(){
        @Override
        public TimeItem createFromParcel(Parcel source) {
            TimeItem timeItem = new TimeItem();
            timeItem.itemTitle = source.readString();
            timeItem.itemMessage = source.readString();
            timeItem.imageId = source.readInt();
            timeItem.aimLevel = source.readString();
            timeItem.aimHour = source.readString();
            timeItem.aimDate = source.readString();
            timeItem.everyDayHour = source.readString();
            return timeItem;
        }

        @Override
        public TimeItem[] newArray(int size) {
            return new TimeItem[size];
        }
    };
}
