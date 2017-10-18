package org.tan.mylife.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.tan.mylife.MainActivity;
import org.tan.mylife.R;

public class AccumulateTimeService extends Service {
    public AccumulateTimeService() {
    }       //空构造函数

    private String ntfTitle;
    private int ntfImageId;
    private int accumulatedTime;        //已经累积的时间，默认为0

    private AccumulateTask accumulateTask;

    private AccumulateListener listener = new AccumulateListener() {
        @Override
        public void onAccumulating(int progress) {
            accumulatedTime = progress;
            getNotificationManager().notify(1, getNotification(ntfTitle, ntfImageId, progress));
        }

        @Override
        public void onStop() {
            //accumulatedTime = accumulateTask.returnTheTime();
            //Log.d("Yes","onStop被执行  "+String.valueOf(accumulateTask.returnTheTime()));
            //Log.d("Yes","onStop被执行  "+String.valueOf(accumulatedTime));
            accumulateTask = null;
            stopForeground(true);
        }

        @Override
        public void pauseAccumulate() {
            accumulatedTime = accumulateTask.returnTheTime();
            accumulateTask = null;
            getNotificationManager().notify(1, getNotification(ntfTitle, ntfImageId, accumulatedTime));
        }
    };

    //以下是Binder部分
    private AccumulateBinder mBinder = new AccumulateBinder();

    public class AccumulateBinder extends Binder{
        public void startAccumulate(String title, int imageId){
            ntfTitle = title;
            ntfImageId = imageId;
            initTime();     //重置accumulatedTime为0
            accumulateTask = new AccumulateTask(listener, accumulatedTime);
            accumulateTask.execute();
            Log.d("Yes: "+AccumulateTimeService.class.toString(), "来到  1");
            startForeground(1, getNotification(ntfTitle,ntfImageId,0));
        }
        public int stopAccumulateTime(){
            if (accumulateTask != null)
                accumulateTask.stopAccumulateTime();
            return accumulatedTime;
        }
    }

    //初始化accumulatedTime，因为每次进行累积的item可能不同，所以必须在开始计时时初始化
    private void initTime(){
        accumulatedTime = 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //获取NotificationManager的对象，实时管理Notification
    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    //返回一个Notification对象
    private Notification getNotification(String title, int imageId, int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.ntf_title, title);
        remoteViews.setTextViewText(R.id.ntf_time, progress+"分钟");
        remoteViews.setImageViewResource(R.id.ntf_image,imageId);
        remoteViews.setImageViewResource(R.id.ntf_change,R.drawable.start);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.eating);
        builder.setContent(remoteViews);
        builder.setContentIntent(pi);
        return builder.build();
    }
}
