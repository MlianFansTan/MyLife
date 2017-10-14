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
 * Created by a on 2017/10/13.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContent;

    private List<TimeItem> mTimeItems;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView itemImage;

        TextView itemTItle;

        TextView itemMessage;

        TextView itemTime;

        public ViewHolder(View view) {
            super(view);
            itemImage = (CircleImageView) view.findViewById(R.id.item_image);
            itemTItle = (TextView) view.findViewById(R.id.item_title);
            itemMessage = (TextView) view.findViewById(R.id.item_message);
            itemTime = (TextView) view.findViewById(R.id.item_time);
        }
    }

    public ItemAdapter(List<TimeItem> timeItems){
        mTimeItems = timeItems;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {Log.d("Yes","com 8");
        TimeItem timeItem = mTimeItems.get(position);
        holder.itemTItle.setText(timeItem.getItemTitle());
        holder.itemMessage.setText(timeItem.getItemMessage());
        holder.itemTime.setText(minToHour(timeItem.getMinNums()));
        Glide.with(mContent).load(timeItem.getImageId()).into(holder.itemImage);
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
