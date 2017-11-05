package org.tan.mylife.record.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.tan.mylife.R;
import org.tan.mylife.Util.ScreenHelper;
import org.tan.mylife.record.Record;
import org.tan.mylife.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by a on 2017/11/1.
 */

public class RecordDialog extends Dialog implements View.OnClickListener{

    private Context mContext;

    //时间操作工具
    private SimpleDateFormat sdf;
    private CustomDatePicker customDatePicker;

    //UI
    private TextView record_dialog_time;
    private EditText record_dialog_matter;
    private Button cancel;
    private Button submit;
    private RelativeLayout dialog_show_record;
    //TextView和EditText内容是否改变   默认0为没有改变
    private int isTVChange = 0;
    private int isEDChange = 0;

    //是新建项目还是修改原项目
    private boolean isNewModel = false;     //默认为修改原项目
    //是哪个Recycler点击进入的
    private int clickRecycler;

    /**
     *  确定时的监听者
     */
    private dialogSubmitListener listener;

    public interface dialogSubmitListener{
        void submit(int recordId);

        void delete(int recordId, int category);

        void addRecord(int category);
    }

    public void setDialogSubmitListener(dialogSubmitListener listener){
        this.listener = listener;
    }

    //拿到的record对象及其id
    private int recordId;
    private Record mRecord;

    public RecordDialog(Context context){
        super(context);
        this.mContext = context;
    }

    public RecordDialog(Context context, int ThemeId, int recordID){
        super(context, ThemeId);
        this.mContext = context;
        this.recordId = recordID;
    }

    public RecordDialog(Context context, int ThemeId, boolean isNewModel, int clickRecycler){
        super(context, ThemeId);
        this.mContext = context;
        this.isNewModel = isNewModel;
        this.clickRecycler = clickRecycler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_record);
        setCanceledOnTouchOutside(true);
        initView();
        initDatePicker();
    }

    private void initView(){
        dialog_show_record = (RelativeLayout) findViewById(R.id.dialog_show_record);

        record_dialog_time = (TextView) findViewById(R.id.record_dialog_time);
        record_dialog_time.setOnClickListener(this);

        record_dialog_matter = (EditText) findViewById(R.id.record_dialog_matter);

        cancel = (Button) findViewById(R.id.record_dialog_cancel);
        cancel.setOnClickListener(this);
        submit = (Button) findViewById(R.id.record_dialog_submit);
        submit.setOnClickListener(this);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        if (isNewModel){
            mRecord = new Record();
            record_dialog_time.setText(sdf.format(new Date(System.currentTimeMillis())));
            cancel.setText("取消");
        }else{
            loadRecord();

            record_dialog_time.setText(sdf.format(mRecord.getMatterDate()));
            record_dialog_time.addTextChangedListener(textWatcher1);

            record_dialog_matter.setText(mRecord.getMatter());
            record_dialog_matter.addTextChangedListener(textWatcher2);
        }
    }

    //从数据库加载对象
    private void loadRecord(){
        mRecord = DataSupport.find(Record.class, recordId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_dialog_time:
                customDatePicker.show(record_dialog_time.getText().toString());
                break;
            case R.id.record_dialog_cancel:
                if (isNewModel){
                    dismiss();
                }else{
                    listener.delete(mRecord.getId(), mRecord.getCategory());
                    DataSupport.delete(Record.class, mRecord.getId());
                    dismiss();
                }
                break;
            case R.id.record_dialog_submit:
                if (isNewModel){
                    mRecord.setMatter(record_dialog_matter.getText().toString());
                    mRecord.setCategory(clickRecycler);
                    try {
                        mRecord.setMatterDate(sdf.parse(record_dialog_time.getText().toString()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mRecord.save();
                    listener.addRecord(clickRecycler);
                }else{
                    if (isTVChange == 1 || isEDChange == 1){
                        try {
                            mRecord.setMatterDate(sdf.parse(record_dialog_time.getText().toString()));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        mRecord.setMatter(record_dialog_matter.getText().toString());
                        mRecord.update(mRecord.getId());
                        listener.submit(mRecord.getId());
                        Log.d("Yes", "更改提交了！" + "isTVChange=" + isTVChange + "  " + "isEDChange=" + isEDChange);
                    }
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private void initDatePicker(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        customDatePicker = new CustomDatePicker(mContext, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                record_dialog_time.setText(time);
                show();
            }
        }, now, "2049-09-30 00:00");// 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动
    }

    //record_dialog_time内容是否改变的监听
    TextWatcher textWatcher1 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isTVChange = 1;
        }
    };

    //record_dialog_matter内容是否改变的监听
    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isEDChange = 1;
        }
    };
}
