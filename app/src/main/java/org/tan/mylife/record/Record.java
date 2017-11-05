package org.tan.mylife.record;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by a on 2017/11/1.
 */

public class Record extends DataSupport{

    private int id;
    private String matter;
    private Date matterDate;
    /**
     *  1：重要不紧急
     *  2：重要紧急
     *  3：不重要不紧急
     *  4：不重要紧急
     */
    private int category;

    public Record(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public Date getMatterDate() {
        return matterDate;
    }

    public void setMatterDate(Date matterDate) {
        this.matterDate = matterDate;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
