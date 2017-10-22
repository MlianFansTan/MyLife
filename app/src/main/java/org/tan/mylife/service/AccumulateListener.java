package org.tan.mylife.service;

/**
 * Server与Task交互的监听者
 * Created by a on 2017/10/17.
 */

public interface AccumulateListener {

    void onAccumulating(int progress);

    void onStop();

    void pauseAccumulate();

}
