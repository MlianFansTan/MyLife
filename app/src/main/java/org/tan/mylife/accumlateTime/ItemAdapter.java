package org.tan.mylife.accumlateTime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.tan.mylife.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * accumulateTimeFragment中RecyclerView的适配器类
 * Created by a on 2017/10/13.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContent;

    private List<TimeItem> mTimeItems;

    //构造函数
    public ItemAdapter(List<TimeItem> timeItems){
        mTimeItems = timeItems;
    }

    //静态内部类ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView itemImage;

        TextView itemTitle;

        TextView itemMessage;

        TextView itemTime;

        CircleImageView changImage;

        public ViewHolder(View view) {
            super(view);
            itemImage = (CircleImageView) view.findViewById(R.id.item_image);
            itemTitle = (TextView) view.findViewById(R.id.item_title);
            itemMessage = (TextView) view.findViewById(R.id.item_message);
            itemTime = (TextView) view.findViewById(R.id.item_time);
            changImage = (CircleImageView) view.findViewById(R.id.change_view);
        }
    }

    //留给调用方的代理函数
    public interface OnItemClickListener{
        //回调函数
        void onClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContent == null)
            mContent = parent.getContext();
        View view = LayoutInflater.from(mContent).inflate(R.layout.timeitem_accumlate, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        TimeItem timeItem = mTimeItems.get(position);
        holder.itemTitle.setText(timeItem.getItemTitle());
        holder.itemMessage.setText(timeItem.getItemMessage());
        holder.itemTime.setText(minToHour(timeItem.getMinNums()));
        Glide.with(mContent).load(timeItem.getImageId()).into(holder.itemImage);
        if (mOnItemClickListener != null){
            holder.changImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTimeItems.size();
    }

    private String minToHour(int mins){
        String str1 = null;
        float a = mins;
        float b = a/60;
        str1 = Float.toString(b);
        return padding(str1).substring(0,4);
        //这里是用数学的方法得到结果，可能效率低一些
        /*float c = ((float)Math.round(b*100))/100;
        str1 = Float.toString(c);
        return str1;*/
    }

    //这里补全字符串的函数，防止下标越界
    private String padding(String str){
        int length = str.length();
        if(length < 4){
            for(int i = 0; i < 3; i++)
                str += "0";
        }
        return str;
    }
}
