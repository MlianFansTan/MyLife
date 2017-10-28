package org.tan.mylife.accumlateTime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;
import org.tan.mylife.R;
import org.tan.mylife.widget.CustomDatePicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemManeger extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private LinearLayout selectLevel;
    private RelativeLayout selectDate;
    private RelativeLayout selectImage;
    private TextView aimDate, everydayHour, aimLevel, aimHour;
    private EditText magTitle, magMessage;
    private Button delete, save;
    private CircleImageView magImageView;
    private int selectedImageId;

    private CustomDatePicker customDatePicker;
    private Date currentDate;

    //是fab进入还是点击item进入
    private int fab_or_item = 0;    //默认0为点击fab

    //以下为dialog的通信部分
    private int IMAGE_DIALOG = 0;
    private int AIM_DIALOG = 1;
    private AccumulateDialog accumulateDialog;
    private CustomTimeDialog customTimeDialog;

    //自带的TimeItem项
    private TimeItem mTimeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_maneger);

        currentDate = new Date();       //获取系统时间

        toolbar = (Toolbar) findViewById(R.id.mag_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_left);
        }

        //拿到所需要的控件
        selectLevel = (LinearLayout) findViewById(R.id.mag_select_level);
        selectLevel.setOnClickListener(this);

        selectDate = (RelativeLayout) findViewById(R.id.mag_selectDate);
        selectDate.setOnClickListener(this);

        selectImage = (RelativeLayout) findViewById(R.id.mag_image_select);
        selectImage.setOnClickListener(this);

        magTitle = (EditText) findViewById(R.id.mag_title);
        magMessage = (EditText) findViewById(R.id.mag_message);

        aimDate = (TextView) findViewById(R.id.mag_aim_time);

        aimDate.addTextChangedListener(textWatcher);

        everydayHour = (TextView) findViewById(R.id.mag_day_hour);

        magImageView = (CircleImageView) findViewById(R.id.mag_image);

        aimLevel = (TextView) findViewById(R.id.mag_level);
        aimLevel.addTextChangedListener(textWatcher2);

        aimHour = (TextView) findViewById(R.id.mag_level_hour);
        aimHour.setOnClickListener(this);
        aimHour.addTextChangedListener(textWatcher);

        delete = (Button) findViewById(R.id.mag_delete);
        delete.setOnClickListener(this);

        save = (Button) findViewById(R.id.mag_save);
        save.setOnClickListener(this);

        initDatePicker();       //初始化日期选择控件

        //判断是fab进入的or点击某个item进入的
        Intent intent = getIntent();
        if (intent.getStringExtra("acT_from").equals("fab")){
            delete.setVisibility(View.GONE);
            delete.setEnabled(false);
            mTimeItem = new TimeItem();
        }
        if(intent.getStringExtra("acT_from").equals("itemClick")){
            mTimeItem = (TimeItem) intent.getParcelableExtra("item_from");
            magTitle.setText(mTimeItem.getItemTitle());
            magMessage.setText(mTimeItem.getItemMessage());
            selectedImageId = mTimeItem.getImageId();
            magImageView.setImageResource(selectedImageId);
            aimLevel.setText(mTimeItem.getAimLevel());
            aimHour.setText(mTimeItem.getAimHour());
            aimDate.setText(mTimeItem.getAimDate());
            everydayHour.setText(mTimeItem.getEveryDayHour());
            fab_or_item = 1;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mag_select_level:
                Log.d("Yes","来到 mag_select_level");
                accumulateDialog = new AccumulateDialog(this, R.style.dialog, AIM_DIALOG, "选择目标大小", new AccumulateDialog.AcmDialogListener (){
                    @Override
                    public void onAimSelect() {
                        aimLevel.setText(accumulateDialog.returnTheAimId());
                        accumulateDialog.dismiss();
                    }
                    @Override
                    public void onImageSelect() {}
                });
                accumulateDialog.show();
                break;
            case R.id.mag_selectDate:
                if (aimLevel.getText().toString().equals("点这里~"))
                    Toast.makeText(ItemManeger.this, "请先选择目标级别", Toast.LENGTH_SHORT).show();
                else
                    customDatePicker.show(aimDate.getText().toString());
                break;
            case R.id.mag_image_select:
                accumulateDialog = new AccumulateDialog(this, R.style.dialog, IMAGE_DIALOG, "选择图标", new AccumulateDialog.AcmDialogListener (){
                    @Override
                    public void onAimSelect() {}
                    @Override
                    public void onImageSelect() {
                        selectedImageId = accumulateDialog.returnTheImageId();
                        Glide.with(ItemManeger.this).load(selectedImageId).into(magImageView) ;
                        accumulateDialog.dismiss();
                    }
                });
                accumulateDialog.show();
                break;
            case R.id.mag_level_hour:
                customTimeDialog = new CustomTimeDialog(this, R.style.dialog, new CustomTimeDialog.CustomTimeListener() {
                    @Override
                    public void onCancel() {
                        customTimeDialog.dismiss();
                    }

                    @Override
                    public void onSubmit() {
                        aimHour.setText(customTimeDialog.returnTheTime());
                        Log.d("Yes",customTimeDialog.returnTheTime());
                        customTimeDialog.dismiss();
                    }
                });
                customTimeDialog.show();
                break;
            case R.id.mag_delete:
                //Log.d("Yes", "进入delete");
                AlertDialog.Builder dialog = new AlertDialog.Builder(ItemManeger.this);
                dialog.setTitle("你确定要删除吗？");
                dialog.setMessage("删除后的数据无法还原！");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        setResult(30, intent);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.mag_save:
                if (fab_or_item == 1){          //由点击项目进入的情况
                    if (magTitle.getText().toString().equals("")){
                        Toast.makeText(ItemManeger.this,"没有目标项吗？",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mTimeItem.setItemTitle(magTitle.getText().toString());
                        Log.d("Yes", magTitle.getText().toString());
                        mTimeItem.setItemMessage(magMessage.getText().toString());
                        mTimeItem.setImageId(selectedImageId);
                        mTimeItem.setAimLevel(aimLevel.getText().toString());
                        mTimeItem.setAimHour(aimHour.getText().toString());
                        mTimeItem.setAimDate(aimDate.getText().toString());
                        mTimeItem.setEveryDayHour(everydayHour.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra("save_date", mTimeItem);
                        setResult(20, intent);
                        finish();
                    }
                }else{          //是由fab进入，即新增item的情况
                    if (magTitle.getText().toString().equals("")){
                        Toast.makeText(ItemManeger.this,"没有目标项吗？",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mTimeItem.setItemTitle(magTitle.getText().toString());
                        mTimeItem.setItemMessage(magMessage.getText().toString());
                        mTimeItem.setImageId(selectedImageId);
                        mTimeItem.setAimLevel(aimLevel.getText().toString());
                        mTimeItem.setAimHour(aimHour.getText().toString());
                        mTimeItem.setAimDate(aimDate.getText().toString());
                        mTimeItem.setEveryDayHour(everydayHour.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra("save_date", mTimeItem);
                        setResult(10, intent);
                        finish();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Log.d("Yes", "来到这里！");
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    //当aimHour改变时，触发监听，改变everydayHour的值
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            setEverydayHour();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    //当aimLevel改变时，触发监听，改变aimHour即完成目标总共需要多少小时的值
    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            String aimStr = aimLevel.getText().toString();
            switch (aimStr){
                case "业界Top1%":
                    aimHour.setText("10000");
                    aimHour.setClickable(false);
                    break;
                case "业界Top5%":
                    aimHour.setText("5400");
                    aimHour.setClickable(false);
                    break;
                case "业界Top10%":
                    aimHour.setText("1800");
                    aimHour.setClickable(false);
                    break;
                case "业界Top20%":
                    aimHour.setText("600");
                    aimHour.setClickable(false);
                    break;
                case "考研":
                    aimHour.setText("2500");
                    aimHour.setClickable(false);
                    break;
                case "所有科目期末90+":
                    aimHour.setText("1000");
                    aimHour.setClickable(false);
                    break;
                case "其它":
                    aimHour.setText("点击自定");
                    aimHour.setClickable(true);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };

    //处理每天需要多少小时的逻辑
    private void setEverydayHour(){
        float neededHour = 0;
        if (!aimHour.getText().toString().equals("点击自定"))
             neededHour = Float.valueOf(aimHour.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String str = aimDate.getText().toString()+ " "+"00:00:00";  //+ " "+"00:00:00"
        Log.d("Yes",str+"     "+currentDate.getTime());
        try {
            Date theAimDate = sdf.parse(str);
            long data = (theAimDate.getTime() - currentDate.getTime())/(24*60*60*1000);
            float data2 = neededHour/((float) data + 1);
            String str2 = String.format("%.1f",data2);
            everydayHour.setText(str2);
            /*if (data2 < 0.5)
                Toast.makeText(ItemManeger.this, "每天30分钟都不投入吗？", Toast.LENGTH_SHORT).show();*/
            Log.d("Yes", str2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        aimDate.setText(now.split(" ")[0]);

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                aimDate.setText(time.split(" ")[0]);
            }
        }, now, "2049-09-30 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }

}
