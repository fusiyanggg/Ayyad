package com.ddf.fusiyang.ayyad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ddf.fusiyang.ayyad.gameview.GameLayout;

public class MainActivity extends Activity {

    private GameLayout mGameLayout;

          //  btn_eazy, btn_nomal, btn_hard,
    private ImageButton imbtn1;

    private MediaPlayer mMusic;
    private TextView mLevel ;
    private TextView mTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mTime = (TextView) findViewById(R.id.id_time);
        mLevel = (TextView) findViewById(R.id.id_level);

        mGameLayout = (GameLayout) findViewById(R.id.gameview);
        mGameLayout.setTimeEnabled(true);
        mGameLayout.setOnGamePintuListener(new GameLayout.GamePintuListener() {
            @Override
            public void nextLevel(final int nextLevel) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("拼图完成（^_^）").setMessage("挑战下一级")
                        .setPositiveButton("NEXT LEVEL", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                mGameLayout.nextLevel();
                                mLevel.setText(""+nextLevel);
                            }
                        }).setNegativeButton("退出游戏",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                }).show();
            }

            @Override
            public void timechanged(int currentTime) {
                mTime.setText(""+currentTime);
            }

            @Override
            public void gameover() {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Time Over").setMessage("重新开始")
                        .setPositiveButton("RESTART", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                mGameLayout.restart();
                            }
                        }).setNegativeButton("退出游戏",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        finish();
                    }
                }).show();
            }
        });

        mMusic = MediaPlayer.create(this, R.raw.ttaxc);
        mMusic.setLooping(true);
        mMusic.start();


//        btn_eazy = (Button) findViewById(R.id.eazy);
//        btn_nomal = (Button) findViewById(R.id.nomal);
//        btn_hard = (Button) findViewById(R.id.hard);

        imbtn1 = (ImageButton) findViewById(R.id.imageButton);
        imbtn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                       mGameLayout.showpic();
                        break;
                    case MotionEvent.ACTION_UP:
                       mGameLayout.disapic();
                        break;
                }

                return false;
            }
        });

    }

//    @Override
//    public void onClick(View v) {
//
//        int id = v.getId();
//        if (id == R.id.eazy) {
//            mGameLayout.eazyLevel();
//
//        } else if (id == R.id.nomal) {
//            mGameLayout.nomalLevel();
//        } else if (id == R.id.hard) {
//            mGameLayout.hardLevel();
//        } else if (id == R.id.changeiv) {
//
//            mGameLayout.changeImage();
//        }
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
        mMusic.stop();
        mGameLayout.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameLayout.resume();
    }
}
