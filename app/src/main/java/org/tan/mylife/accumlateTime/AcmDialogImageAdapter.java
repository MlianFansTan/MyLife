package org.tan.mylife.accumlateTime;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.tan.mylife.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by a on 2017/10/25.
 */

public class AcmDialogImageAdapter extends RecyclerView.Adapter<AcmDialogImageAdapter.ViewHolder> {
    private Context mContext;
    private List<Integer> mImageList;

    //留给调用方的代理函数
    public interface OnItemClickListener{
        //回调函数
        void onClick(int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener = onItemClickListener;
    }

    public AcmDialogImageAdapter(List<Integer> list){
        this.mImageList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
         CircleImageView imageView;
        public ViewHolder(View view){
            super(view);
            imageView = (CircleImageView) view.findViewById(R.id.dialog_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_image_recycle, parent, false);
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
        int imageId = mImageList.get(position);
        Glide.with(mContext).load(imageId).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }
}
