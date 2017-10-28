package org.tan.mylife.accumlateTime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tan.mylife.R;

import java.util.List;

/**
 * Created by a on 2017/10/25.
 */

public class AcmDialogAimAdapter extends RecyclerView.Adapter<AcmDialogAimAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mAimsList;

    public AcmDialogAimAdapter(List<String> list){
        this.mAimsList = list;
    }

    //留给调用方的代理函数
    public interface OnItemClickListener{
        //回调函数
        void onClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView aimView;

        public ViewHolder(View view){
            super(view);
            aimView = (TextView) view.findViewById(R.id.dialog_aim);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_aim_recycle, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onClick(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String aim = mAimsList.get(position);
        holder.aimView.setText(aim);
    }

    @Override
    public int getItemCount() {
        return mAimsList.size();
    }
}
