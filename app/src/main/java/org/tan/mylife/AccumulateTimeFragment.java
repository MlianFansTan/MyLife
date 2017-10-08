package org.tan.mylife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by a on 2017/10/8.
 */

public class AccumulateTimeFragment extends Fragment {
    public AccumulateTimeFragment(){

    }

    public static AccumulateTimeFragment newInstance(String param1){
        AccumulateTimeFragment fragment = new AccumulateTimeFragment();
        Bundle args = new Bundle();
        args.putString("args1",param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accumlate, container, false);
        Bundle bundle = getArguments();
        String args1 = bundle.getString("args1");
        TextView tv = (TextView) view.findViewById(R.id.tv_accumulate);
        tv.setText(args1);
        return view;
    }
}