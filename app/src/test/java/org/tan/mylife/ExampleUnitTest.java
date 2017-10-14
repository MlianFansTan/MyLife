package org.tan.mylife;

import org.junit.Test;
import org.tan.mylife.accumlateTime.TimeItem;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        TimeItem timeItem = new TimeItem(1,"哈哈哈","哈哈哈",R.mipmap.sleeping, 50);
        String str = timeItem.getMinNums()+"";
        System.out.print(str);
    }
}