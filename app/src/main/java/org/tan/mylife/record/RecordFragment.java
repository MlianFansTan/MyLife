package org.tan.mylife.record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.litepal.crud.DataSupport;
import org.tan.mylife.R;
import org.tan.mylife.record.dialogs.RecordDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.tan.mylife.R.id.Card_add_four;
import static org.tan.mylife.R.id.Card_add_one;
import static org.tan.mylife.R.id.Card_add_three;
import static org.tan.mylife.R.id.Card_add_two;
import static org.tan.mylife.R.id.record_not_vital_not_urgent;
import static org.tan.mylife.R.id.record_not_vital_urgent;
import static org.tan.mylife.R.id.record_vital_not_urgent;
import static org.tan.mylife.R.id.record_vital_urgent;

/**
 * Created by a on 2017/10/9.
 */

public class RecordFragment extends Fragment implements RecordDialog.dialogSubmitListener, View.OnClickListener{

    /**
     * UI
     */
    private CardView CDV_not_urgent_vital;
    private RecyclerView record_recycler_one;
    private ImageView card_add_one;

    private CardView CDV_urgent_vital;
    private RecyclerView record_recycler_two;
    private ImageView card_add_two;

    private CardView CDV_not_urgent_not_vital;
    private RecyclerView record_recycler_three;
    private ImageView card_add_three;

    private CardView CDV_urgent_not_vital;
    private RecyclerView record_recycler_four;
    private ImageView card_add_four;

    /**
     *  需要显示的自定义dialog
     */
    private RecordDialog dialog;

    /**
     * 要对recyclerView进行适配的数据list
     */
    private List<Record> allRecordList = new ArrayList<>();
    private List<Record> not_Urgent_Vital_list = new ArrayList<>();
    private List<Record> urgent_Vital_list = new ArrayList<>();
    private List<Record> not_Urgent_not_Vital_list = new ArrayList<>();
    private List<Record> urgent_not_Vital_list = new ArrayList<>();
    //适配器
    private RecordAdapter adapter_one;
    private RecordAdapter adapter_two;
    private RecordAdapter adapter_three;
    private RecordAdapter adapter_four;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRecordDate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        CDV_not_urgent_vital = (CardView) view.findViewById(R.id.record_not_vital_urgent);
        record_recycler_one = (RecyclerView) view.findViewById(R.id.record_recyclerView1);
        card_add_one = (ImageView) view.findViewById(R.id.Card_add_one);
        card_add_one.setOnClickListener(this);

        CDV_urgent_vital = (CardView) view.findViewById(R.id.record_vital_urgent);
        record_recycler_two = (RecyclerView) view.findViewById(R.id.record_recyclerView2);
        card_add_two = (ImageView) view.findViewById(R.id.Card_add_two);
        card_add_two.setOnClickListener(this);

        CDV_not_urgent_not_vital = (CardView) view.findViewById(R.id.record_not_vital_not_urgent);
        record_recycler_three = (RecyclerView) view.findViewById(R.id.record_recyclerView3);
        card_add_three = (ImageView) view.findViewById(R.id.Card_add_three);
        card_add_three.setOnClickListener(this);

        CDV_urgent_not_vital = (CardView) view.findViewById(record_vital_not_urgent);
        record_recycler_four = (RecyclerView) view.findViewById(R.id.record_recyclerView4);
        card_add_four = (ImageView) view.findViewById(R.id.Card_add_four);
        card_add_four.setOnClickListener(this);

        initView();     //初始化view及适配adapter

        return view;
    }

    //初始化view
    private void initView(){

        adapter_one = new RecordAdapter(not_Urgent_Vital_list, RecordFragment.this);
        adapter_two = new RecordAdapter(urgent_Vital_list, RecordFragment.this);
        adapter_three = new RecordAdapter(not_Urgent_not_Vital_list, RecordFragment.this);
        adapter_four = new RecordAdapter(urgent_not_Vital_list, RecordFragment.this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        record_recycler_one.setLayoutManager(layoutManager1);
        record_recycler_one.setAdapter(adapter_one);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        record_recycler_two.setLayoutManager(layoutManager2);
        record_recycler_two.setAdapter(adapter_two);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        record_recycler_three.setLayoutManager(layoutManager3);
        record_recycler_three.setAdapter(adapter_three);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        record_recycler_four.setLayoutManager(layoutManager4);
        record_recycler_four.setAdapter(adapter_four);
    }

    //加载并分类record记录
    private void loadRecordDate(){
        initRecordDate();
        for (int i = 0; i < allRecordList.size(); i++){
            Record record = allRecordList.get(i);
            if (record.getCategory() == 1){
                not_Urgent_Vital_list.add(record);
            }else if(record.getCategory() == 2){
                urgent_Vital_list.add(record);
            }else if(record.getCategory() == 3){
                not_Urgent_not_Vital_list.add(record);
            }else if (record.getCategory() == 4){
                urgent_not_Vital_list.add(record);
            }
        }
    }

    //初始化record记录
    private void initRecordDate(){
        if (DataSupport.isExist(Record.class)){
            allRecordList = DataSupport.findAll(Record.class);
        }else{
            Record record1 = new Record();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateStr = "2017-11-20 22:01";
            Date date = null;
            try {
                date = dateFormat.parse(dateStr);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (date != null)
                record1.setMatterDate(date);
            record1.setMatter("编译原理作业");
            record1.setCategory(1);
            record1.save();

            Record record2 = new Record();
            String dateStr2 = "2017-11-12 19:50";
            try {
                date = dateFormat.parse(dateStr2);
            }catch (Exception e){
                e.printStackTrace();
            }
            if (date != null)
                record2.setMatterDate(date);
            record2.setMatter("移动程序设计考试");
            record2.setCategory(2);
            record2.save();

            allRecordList = DataSupport.findAll(Record.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case Card_add_one:
                dialog = new RecordDialog(getContext(), R.style.dialog, true, 1);
                dialog.setDialogSubmitListener(this);
                dialog.show();
                break;
            case Card_add_two:
                dialog = new RecordDialog(getContext(), R.style.dialog, true, 2);
                dialog.setDialogSubmitListener(this);
                dialog.show();
                break;
            case Card_add_three:
                dialog = new RecordDialog(getContext(), R.style.dialog, true, 3);
                dialog.setDialogSubmitListener(this);
                dialog.show();
                break;
            case Card_add_four:
                dialog = new RecordDialog(getContext(), R.style.dialog, true, 4);
                dialog.setDialogSubmitListener(this);
                dialog.show();
                break;
            default:
                break;
        }
    }

    /**
     *  重写RecordsubmitListener的方法
     */
    @Override
    public void submit(int recordId) {
        int position;
        Record record = DataSupport.find(Record.class, recordId);
        switch (record.getCategory()){
            case 1:
                for (int i = 0; i < not_Urgent_Vital_list.size(); i++){
                    if (not_Urgent_Vital_list.get(i).getId() == recordId){
                        position = i;
                        not_Urgent_Vital_list.remove(position);
                        break;
                    }
                }
                not_Urgent_Vital_list.add(record);
                adapter_one.notifyDataSetChanged();
                break;
            case 2:
                for (int i = 0; i < urgent_Vital_list.size(); i++){
                    if (urgent_Vital_list.get(i).getId() == recordId){
                        position = i;
                        urgent_Vital_list.remove(position);
                        break;
                    }
                }
                urgent_Vital_list.add(record);
                adapter_two.notifyDataSetChanged();
                break;
            case 3:
                for (int i = 0; i < not_Urgent_not_Vital_list.size(); i++){
                    if (not_Urgent_not_Vital_list.get(i).getId() == recordId){
                        position = i;
                        not_Urgent_not_Vital_list.remove(position);
                        break;
                    }
                }
                not_Urgent_not_Vital_list.add(record);
                adapter_three.notifyDataSetChanged();
                break;
            case 4:
                for (int i = 0; i < not_Urgent_Vital_list.size(); i++){
                    if (not_Urgent_Vital_list.get(i).getId() == recordId){
                        position = i;
                        not_Urgent_Vital_list.remove(position);
                        break;
                    }
                }
                not_Urgent_Vital_list.add(record);
                adapter_four.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void delete(int recordId, int category) {
        int position;   //需要删除的项在某个list中的位置
        switch (category){
            case 1:
                for (int i = 0; i < not_Urgent_Vital_list.size(); i++){
                    if (not_Urgent_Vital_list.get(i).getId() == recordId){
                        position = i;
                        not_Urgent_Vital_list.remove(position);
                        break;
                    }
                }
                adapter_one.notifyDataSetChanged();
                break;
            case 2:
                for (int i = 0; i < urgent_Vital_list.size(); i++){
                    if (urgent_Vital_list.get(i).getId() == recordId){
                        position = i;
                        urgent_Vital_list.remove(position);
                        break;
                    }
                }
                adapter_two.notifyDataSetChanged();
                break;
            case 3:
                for (int i = 0; i < not_Urgent_not_Vital_list.size(); i++){
                    if (not_Urgent_not_Vital_list.get(i).getId() == recordId){
                        position = i;
                        not_Urgent_not_Vital_list.remove(position);
                        break;
                    }
                }
                adapter_three.notifyDataSetChanged();
                break;
            case 4:
                for (int i = 0; i < not_Urgent_Vital_list.size(); i++){
                    if (not_Urgent_Vital_list.get(i).getId() == recordId){
                        position = i;
                        not_Urgent_Vital_list.remove(position);
                        break;
                    }
                }
                adapter_four.notifyDataSetChanged();
                break;
            default:
                break;
        }

    }

    @Override
    public void addRecord(int category) {
        Log.d("Yes", "来到addRecord!");
        Record record = DataSupport.findLast(Record.class);
        switch (category){
            case 1:
                not_Urgent_Vital_list.add(record);
                adapter_one.notifyDataSetChanged();
                break;
            case 2:
                urgent_Vital_list.add(record);
                adapter_two.notifyDataSetChanged();
                break;
            case 3:
                not_Urgent_not_Vital_list.add(record);
                adapter_three.notifyDataSetChanged();
                break;
            case 4:
                not_Urgent_Vital_list.add(record);
                adapter_four.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
