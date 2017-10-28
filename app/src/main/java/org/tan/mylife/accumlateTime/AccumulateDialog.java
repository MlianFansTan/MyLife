package org.tan.mylife.accumlateTime;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import org.tan.mylife.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2017/10/25.
 */

public class AccumulateDialog extends Dialog {


    private int IsImageOrAim;       //为0时是image的适配器，为1时是aim的适配器
    private int selectedPosition = 0;    //选择的哪一项,默认为0

    private List<Integer> imageIdList = new ArrayList<Integer>();
    private int[] image =  {R.mipmap.eating, R.mipmap.game, R.mipmap.home, R.mipmap.homework,
                    R.mipmap.music, R.mipmap.rolling, R.mipmap.sleeping, R.mipmap.strength,
                    R.mipmap.study, R.mipmap.working};

    private List<String> aimsList = new ArrayList<>();
    private String[] aims = {"业界Top1%", "业界Top5%", "业界Top10%", "业界Top20%","考研", "所有科目期末90+","其它"};

    private Context mContext;
    private String dialogTileContent;
    private AcmDialogListener listener;

    private RecyclerView recyclerView;
    private TextView dialogTitle;

    //image列表的适配器
    private AcmDialogImageAdapter imageAdapter;
    //aim列表的适配器
    private AcmDialogAimAdapter aimAdapter;

    public AccumulateDialog(Context context){
        super(context);
        this.mContext = context;
    }

    public AccumulateDialog(Context context, int ThemeId, int isImageOrAim, String dialogTileContent, AcmDialogListener listener){
        super(context, ThemeId);
        this.mContext = context;
        this.IsImageOrAim = isImageOrAim;
        this.dialogTileContent = dialogTileContent;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Yes", AccumulateDialog.class.toString()+"来到onCreate");
        setContentView(R.layout.dialog_common);
        Log.d("Yes", AccumulateDialog.class.toString()+"设置setContentView完成");
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView(){
        dialogTitle = (TextView) findViewById(R.id.dialog_title);
        dialogTitle.setText(dialogTileContent);
        recyclerView = (RecyclerView) findViewById(R.id.dialog_recycler);
        if (IsImageOrAim == 0){
            initImage();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            imageAdapter = new AcmDialogImageAdapter(imageIdList);
            recyclerView.setAdapter(imageAdapter);
            imageAdapter.setmOnItemClickListener(new AcmDialogImageAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    selectedPosition = position;
                    listener.onImageSelect();
                }
            });
        }else if (IsImageOrAim == 1){
            initAims();
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(linearLayoutManager);
            aimAdapter = new AcmDialogAimAdapter(aimsList);
            recyclerView.setAdapter(aimAdapter);
            aimAdapter.setmOnItemClickListener(new AcmDialogAimAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    selectedPosition = position;
                    listener.onAimSelect();
                }
            });
        }

    }

    private void initImage(){
        for(int i = 0; i < image.length; i++){
            imageIdList.add(image[i]);
        }
    }

    private void initAims(){
        for (int i = 0; i < aims.length; i++)
            aimsList.add(aims[i]);
    }

    public int returnTheImageId(){
        return imageIdList.get(selectedPosition);
    }

    public String returnTheAimId(){
        return aimsList.get(selectedPosition);
    }

    public interface AcmDialogListener{
        void onImageSelect();

        void onAimSelect();
    }
}
