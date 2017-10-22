package org.tan.mylife.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import org.tan.mylife.R;

public class AccumulateTimeService extends Service {
    public AccumulateTimeService() {
    }       //空构造函数

    private String ntfTitle;            //传入项的项目名和图片ID
    private int ntfImageId;
    private int accumulatedTime;        //已经累积的时间，默认为0

    private int isInAccumulating = 0;     //Task是否正在运行,为0时没有运行，为1时正在运行

    private AccumulateTask accumulateTask;

    //为Receiver建立对象，并传入接口实例
    private PauseStatusReceiver myReceiver = new PauseStatusReceiver(new PauseStatusReceiver.onStatusChangeListener() {
        @Override
        public void solute() {
            Log.d("Yes","接口收到通知！");
            solutePauseStatus();
        }
    });

    //accumulateTask中的Task对象，并传入listener实例
    private  AccumulateListener listener = new AccumulateListener() {
        @Override
        public void onAccumulating(int progress) {
            accumulatedTime = progress;
            getNotificationManager().notify(1, getNotification(ntfTitle, ntfImageId, progress));
        }

        @Override
        public void onStop() {
            accumulateTask = null;
            stopForeground(true);
        }

        @Override
        public void pauseAccumulate() {
            //accumulatedTime = accumulateTask.returnTheTime();
            if (accumulateTask != null)
                accumulateTask = null;
            getNotificationManager().notify(1, getNotification(ntfTitle, ntfImageId, accumulatedTime));
        }
    };

    //以下是Binder部分，起到与Fragment交互的作用
    private AccumulateBinder mBinder = new AccumulateBinder();

    public class AccumulateBinder extends Binder{
        public void startAccumulate(String title, int imageId){
            ntfTitle = title;
            ntfImageId = imageId;
            initTime();     //重置accumulatedTime为0
            accumulateTask = new AccumulateTask(listener, accumulatedTime);
            accumulateTask.execute();
            isInAccumulating = 1;
            Log.d("Yes: "+AccumulateTimeService.class.toString(), "来到  1");
            startForeground(1, getNotification(ntfTitle,ntfImageId,0));
        }
        public int stopAccumulateTime(){
            if (accumulateTask != null)
                accumulateTask.stopAccumulateTime();
            else
                stopForeground(true);
            return accumulatedTime;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //初始化accumulatedTime，因为每次进行累积的item可能不同，所以必须在开始计时时初始化
    private void initTime(){
        accumulatedTime = 0;
    }

    //获取NotificationManager的对象，实时管理Notification
    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    //返回一个Notification对象
    private Notification getNotification(String title, int imageId, int progress){
        Intent intent = new Intent("noAction");
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        PendingIntent clickPending = PendingIntent.getBroadcast(this, 1 ,new Intent("PausedStatusChange"), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ntf_change,clickPending);
        remoteViews.setTextViewText(R.id.ntf_title, title);
        remoteViews.setTextViewText(R.id.ntf_time, progress+"分钟");
        remoteViews.setImageViewResource(R.id.ntf_image,imageId);
        if (isInAccumulating == 1)
            remoteViews.setImageViewResource(R.id.ntf_change,R.drawable.end);
        else if (isInAccumulating == 0)
            remoteViews.setImageViewResource(R.id.ntf_change,R.drawable.start);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.eating);
        builder.setContent(remoteViews);
        builder.setContentIntent(pi);
        return builder.build();
    }

    //当Receiver接收到广播时，代理函数调用的，用来解决在notification上点击暂停按钮的逻辑
    private void solutePauseStatus(){
        if (isInAccumulating == 1){
            accumulateTask.pauseAccumulate();
            accumulateTask = null;
            isInAccumulating = 0;
            Log.d("Yes:", "isInAccumuting为"+String.valueOf(isInAccumulating));
        }else if(isInAccumulating == 0){
            accumulateTask = new AccumulateTask(listener, accumulatedTime);
            accumulateTask.execute();
            isInAccumulating = 1;
            getNotificationManager().notify(1, getNotification(ntfTitle, ntfImageId, accumulatedTime));
            Log.d("Yes:", "isInAccumuting为"+String.valueOf(isInAccumulating));
        }
    }

    //注册广播接收器
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter("noAction");
        intentFilter.addAction("PausedStatusChange");
        registerReceiver(myReceiver, intentFilter);
    }

    //与onCreate方法相对应，解除注册广播接收器
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
