package org.tan.mylife.accumlateTime;

import org.litepal.crud.DataSupport;

/**
 * 为每一个时间积累项建立Bean类
 *
 * Created by a on 2017/10/10.
 */

public class TimeItem extends DataSupport{
    private int id;

    private int minNums;        //统计积累了多少分钟

    private String itemTitle;   //每项的标题

    private String itemMessage;     //每一项的描述

    /*public TimeItem(String itemTitle, int minNums, String itemMessage, int id){
        this.itemTitle = itemTitle;
        this.itemMessage = itemMessage;
        this.minNums = minNums;
        this.id = id;
    }*/

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
}
