package org.tan.mylife.record;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.tan.mylife.R;
import org.tan.mylife.record.dialogs.RecordDialog;

import java.util.Date;
import java.util.List;

/**
 * Created by a on 2017/11/1.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Fragment mFragment;
    private Context mContext;
    private List<Record> mRecordList;

    public RecordAdapter(List<Record> recordList, Fragment fragment){
        this.mRecordList = recordList;
        mFragment = fragment;
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView matter_record_simple;
        TextView matter_remainDay;
        ImageView matter_record_delete;

        public ViewHolder(View view){
            super(view);
            matter_record_simple = (TextView) view.findViewById(R.id.matter_simple);
            matter_remainDay = (TextView) view.findViewById(R.id.matter_remain_day);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.matter_record, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordDialog dialog = new RecordDialog(mFragment.getContext(), R.style.dialog, mRecordList.get(holder.getAdapterPosition()).getId());
                dialog.setDialogSubmitListener((RecordDialog.dialogSubmitListener) mFragment);
                dialog.show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = mRecordList.get(position);
        holder.matter_record_simple.setText(record.getMatter());
        try{
            long remainTime = record.getMatterDate().getTime() - new Date(System.currentTimeMillis()).getTime();
            long remainDay = remainTime/(1000 * 60 * 60 * 24);
            String day = String.valueOf(remainDay) + "天";
            holder.matter_remainDay.setText(day);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }
}
