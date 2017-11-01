package org.tan.mylife.diary.spinnerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.tan.mylife.R;

/**
 * Created by a on 2017/10/30.
 */

public class ImageArrayAdapter extends ArrayAdapter<Integer> {

    private Integer[] images;
    private LayoutInflater inflater;
    private Context mContext;

    public ImageArrayAdapter(Context context, Integer[] images) {
        super(context, R.layout.spinner_imageview, images);
        this.images = images;
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_imageview, parent, false);
        }
        return getImageForPosition(position, convertView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_imageview, parent, false);
        }
        return getImageForPosition(position, convertView);
    }

    private View getImageForPosition(int position, View rootView) {
        ImageView imageView = (ImageView) rootView.findViewById(R.id.IV_spinner);
        imageView.setImageResource(images[position]);
        imageView.setColorFilter(mContext.getResources().getColor(R.color.hotpink));
        return rootView;
    }
}
