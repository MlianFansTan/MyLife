package org.tan.mylife.service;

import android.os.AsyncTask;

import java.util.Date;

/**
 * Created by a on 2017/10/17.
 */

public class AccumulateTask extends AsyncTask<String, Integer, Integer> {

    private int acctumulatedTime = 0;      //记录累积过后的时间,小于1分钟时为0

    private static final int TYPE_PAUSED = 0;
    private static final int TYPE_STOP = 1;
    private static final int TYPE_CLOSE = 1;

    private boolean isPaused = false;   //是否暂停
    private boolean isStop = false;     //是否停止

    private AccumulateListener listener;        //建立监听器

    //构造函数, 每次暂停过后要重启Task，必须传入一个累加过的时间
    public AccumulateTask(AccumulateListener listener, int time){
        this.listener = listener;
        this.acctumulatedTime = time;
    }

    @Override
    protected Integer doInBackground(String... params) {
        long diff;
        Date dateLast = new Date(System.currentTimeMillis());
        Date dateCurrent;
        try{
            while (true){
                //实时更新积累的时间的逻辑
                //Thread.sleep(1000*60);
                dateCurrent = new Date(System.currentTimeMillis());
                diff = dateCurrent.getTime() - dateLast.getTime();
                if(diff > 1000*60){
                    dateLast = dateCurrent;
                    acctumulatedTime += 1;
                    publishProgress(acctumulatedTime);
                }
                if (isPaused == true){
                    return TYPE_PAUSED;
                }else if (isStop == true){
                    return TYPE_STOP;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return TYPE_CLOSE;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        listener.onAccumulating(acctumulatedTime);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_PAUSED:
                break;
            case TYPE_STOP:
                listener.onStop();
                break;
            default:
                break;
        }
    }

    public void pauseAccumulate(){
        isPaused = true;
    }

    public void stopAccumulateTime(){
        isStop = true;
    }

    public int returnTheTime(){
        return acctumulatedTime;
    }
}
