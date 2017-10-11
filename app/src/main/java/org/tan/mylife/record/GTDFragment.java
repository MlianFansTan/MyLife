package org.tan.mylife.record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tan.mylife.R;

/**
 * Created by a on 2017/10/8.
 */

public class GTDFragment extends Fragment {
    public GTDFragment(){

    }

    //替代构造函数的静态方法（预留）
    /*public static GTDFragment newInstance(String param1){
        GTDFragment fragment = new GTDFragment();
        Bundle args = new Bundle();
        args.putString("args1",param1);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gtd, container, false);

        return view;
    }
}
