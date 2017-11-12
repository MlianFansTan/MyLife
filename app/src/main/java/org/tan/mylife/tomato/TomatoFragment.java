package org.tan.mylife.tomato;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.tan.mylife.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by a on 2017/10/8.
 */

public class TomatoFragment extends Fragment implements View.OnClickListener{

    private MyTomato myTomato;
    private MyRest myRest;

    //震动对象
    private Vibrator vibrator;

    private SimpleDateFormat sdf;

    private int isTomatoIng = 0;        //是否正在进行番茄，默认0没有进行
    private int isInResting = 0;        //是否正在休息，默认0没有进行

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomato, container, false);
        myTomato = (MyTomato) view.findViewById(R.id.tomato_circle);
        myTomato.setShowTextAndSweepValue("",100);
        myTomato.setOnClickListener(this);

        myRest = (MyRest) view.findViewById(R.id.tomato_rest);
        myRest.setOnClickListener(this);

        vibrator = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        sdf = new SimpleDateFormat("mm:ss");

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tomato_circle:
                if (isTomatoIng == 0){
                    startTomato();
                }else if (isInResting == 1){
                    endRest();
                }
                break;
            case R.id.tomato_rest:
                if (isTomatoIng == 1){
                    endTomato();
                }
                break;
            default:
                break;
        }
    }

    //整合开始番茄的逻辑
    private void startTomato(){
        myRest.setVisibility(View.VISIBLE);
        Toast.makeText(getActivity(), "清空大脑，专心投入！",Toast.LENGTH_SHORT).show();
        timer1.start();
        isTomatoIng = 1;
    }

    //整合结束番茄的逻辑
    private void endTomato(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(true);
        dialog.setTitle("番茄正在进行中");
        dialog.setMessage("您确定要休息吗？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer1.cancel();
                startRest();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    //整合休息的逻辑
    private void startRest(){
        myRest.setVisibility(View.GONE);
        myTomato.changeColor(1);
        Toast.makeText(getActivity(), "好好放松放松",Toast.LENGTH_SHORT).show();
        timer2.start();
        isTomatoIng = 1;
        isInResting = 1;
    }

    //整合结束休息的逻辑
    private void endRest(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(true);
        dialog.setTitle("正在休息中");
        dialog.setMessage("又要开始工作啦？");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer2.cancel();
                isInResting = 0;
                isTomatoIng = 0;
                myTomato.changeColor(0);
                myTomato.setShowTextAndSweepValue("开始番茄",100);
                myRest.setVisibility(View.VISIBLE);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    /**
     * 倒计时器
     * timer1 为tomato计时
     */
    private CountDownTimer timer1 = new CountDownTimer(1500000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            try {
                Date date = new Date(millisUntilFinished);
                String remainTime = sdf.format(date);
                float percent = (((float)millisUntilFinished)/((float)1500000))*100;
                myTomato.setShowTextAndSweepValue(remainTime, percent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            isTomatoIng = 0;
            vibrator.vibrate(new long[]{500,500,500,500}, -1);
            myTomato.setShowTextAndSweepValue("", 0);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("番茄结束");
            dialog.setMessage("请问您还要继续吗？");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startTomato();
                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startRest();
                }
            });
            dialog.show();
        }
    };

    private CountDownTimer timer2 = new CountDownTimer(300000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            try {
                Date date = new Date(millisUntilFinished);
                String remainTime = sdf.format(date);
                float percent = (((float)millisUntilFinished)/((float)300000))*100;
                myTomato.setShowTextAndSweepValue(remainTime, percent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onFinish() {
            isTomatoIng = 0;
            vibrator.vibrate(new long[]{500,500,500,500}, -1);
            myRest.setVisibility(View.VISIBLE);
            myTomato.changeColor(0);
            myTomato.setShowTextAndSweepValue("开始番茄",100);
        }
    };






    public TomatoFragment() {}

    //替代构造函数的静态方法（预留）
    /*public static TomatoFragment newInstance(String param1){
        TomatoFragment fragment = new TomatoFragment();
        Bundle args = new Bundle();
        args.putString("args1",param1);
        fragment.setArguments(args);
        return fragment;
    }*/
}
