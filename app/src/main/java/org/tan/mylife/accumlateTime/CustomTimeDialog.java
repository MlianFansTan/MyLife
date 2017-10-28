package org.tan.mylife.accumlateTime;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.tan.mylife.R;

/**
 * Created by a on 2017/10/26.
 */

public class CustomTimeDialog extends Dialog implements View.OnClickListener{

    private Context mContext;

    //所有的控件
    private Button cancel;
    private Button submit;
    private EditText customTime;

    private CustomTimeListener listener;

    public CustomTimeDialog(Context context){
        super(context);
        this.mContext = context;
    }


    public CustomTimeDialog(Context context,int ThemeId, CustomTimeListener listener){
        super(context, ThemeId);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom_time);
        cancel = (Button) findViewById(R.id.custom_time_cancel);
        cancel.setOnClickListener(this);
        submit = (Button) findViewById(R.id.custom_time_submit);
        submit.setOnClickListener(this);
        customTime = (EditText) findViewById(R.id.custom_time);
        customTime.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.custom_time_cancel:
                listener.onCancel();
                break;
            case R.id.custom_time_submit:
                listener.onSubmit();
                break;
            default:
                break;
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!isNumber(customTime.getText().toString()))
                customTime.setText("");
        }
    };

    public String returnTheTime(){
        return customTime.getText().toString();
    }

    public interface CustomTimeListener{
        void onCancel();

        void onSubmit();
    }

    //判断customTime输入的字符串是否是数字
    private boolean isNumber(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }
}
