package org.tan.mylife.Util;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by a on 2017/10/13.
 */

public class DateUtil {
    public static List<Object> findAll(Class<Object>  modelClass){
        List<Object> objects = DataSupport.findAll(modelClass);
        return objects;
    }
}
