package com.ddf.fusiyang.ayyad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ddf.fusiyang.ayyad.adapter.GalleryAdapter;
import com.ddf.fusiyang.ayyad.gameview.MyRecyclerView;
import com.ddf.fusiyang.ayyad.gameview.MyRecyclerView.OnItemScrollChangeListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 程序主界面：显示默认图片列表、自选图片按钮
 *
 * @author xys
 */
public class Main2Activity extends Activity {

    // 返回码：系统图库
    private static final int RESULT_IMAGE = 100;
    // 返回码：相机
    private static final int RESULT_CAMERA = 200;
    // Temp照片路径
    public static String TEMP_IMAGE_PATH;
    // IMAGE TYPE
    private static final String IMAGE_TYPE = "image/*";
    // GridView 显示图片
    private GridView gv_Pic_List;
    private List<Bitmap> picList;
    // 主页图片资源ID


    // 本地图册、相机选择
    private String[] customItems = new String[]{"本地图册", "相机拍照"};


    private MyRecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mDatas;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main2);


        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/ttt.jpg";

        initDatas();
//get xml
        mRecyclerView = (MyRecyclerView) findViewById(R.id.recyclerview_h);
        mImg = (ImageView) findViewById(R.id.content);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);


        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //set adapter
        mAdapter = new GalleryAdapter(this, mDatas);


        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnItemScrollChangeListener(new OnItemScrollChangeListener() {
            @Override
            public void onChange(View view, int position) {
                mImg.setImageResource(mDatas.get(position));
            }
        });


        mAdapter.setOnItemClickLitentner(new GalleryAdapter.OnItemClickLitentner() {
            @Override
            public void onItemClick(View view, int position) {

                if (position == 15) {
                    // 选择本地图库 相机
                    showDialogCustom();
                } else {
                    // 选择默认图片
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                    intent.putExtra("picSelectedID", position);
                    //intent.putExtra("type", type);
                    startActivity(intent);
                }
                Toast.makeText(Main2Activity.this, position + "", Toast.LENGTH_SHORT).show();
                mImg.setImageResource(mDatas.get(position));
            }
        });
    }


    private void initDatas() {

        mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5,
                R.drawable.img6, R.drawable.img7, R.drawable.img8, R.drawable.img9, R.drawable.img10, R.drawable.img11, R.drawable.img12, R.drawable.img13, R.drawable.img14,
                R.drawable.img15, R.drawable.plus));
    }

    // 显示选择系统图库 相机对话框
    private void showDialogCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("选择：");
        builder.setItems(customItems, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (0 == which) {
                    // 本地相册
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_TYPE);
                    startActivityForResult(intent, RESULT_IMAGE);
                } else if (1 == which) {
                    // 系统相机
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri photoUri = Uri.fromFile(new File(TEMP_IMAGE_PATH));
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, RESULT_CAMERA);
                }
            }
        });
        builder.create().show();
    }

    /**
     * 调用图库相机回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_IMAGE && data != null) {
                // 相册
                Cursor cursor = this.getContentResolver().query(data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                intent.putExtra("picPath", imagePath);
                //   intent.putExtra("type", type);
                startActivity(intent);
            } else if (requestCode == RESULT_CAMERA) {
                // 相机
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                intent.putExtra("picPath", TEMP_IMAGE_PATH);
                // intent.putExtra("type", type);
                startActivity(intent);
            }
        }
    }



}
