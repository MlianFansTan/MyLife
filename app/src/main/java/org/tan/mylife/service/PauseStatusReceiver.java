package org.tan.mylife.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PauseStatusReceiver extends BroadcastReceiver {

    public interface onStatusChangeListener{
        void solute();
    }

    private onStatusChangeListener onStatusChangeListener;

    public PauseStatusReceiver(onStatusChangeListener onStatusChangeListener){
        this.onStatusChangeListener = onStatusChangeListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == "noAction"){

        }else if (action == "PausedStatusChange"){
            onStatusChangeListener.solute();
        }

    }
}
