package com.ddf.fusiyang.ayyad.gameview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ddf.fusiyang.ayyad.CutPicture.ImagePiece;
import com.ddf.fusiyang.ayyad.CutPicture.ImageSplitterUtil;
import com.ddf.fusiyang.ayyad.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by fusiyang on 2016/5/13.
 */
public class GameLayout extends RelativeLayout implements View.OnClickListener {
    private int mChunk = 3;  //3*3 chunk
    private int iI = 0;

    private int mPadding;    //vactor内边距
    private int mMagin = 3;      //each chunks magin(W,H) jianju dp
    private int mWidth; //cavaer
    private ImageView[] mGameItems; //存储小图片的数组

    private ImageView pic;
    private int mItemWidth;        // 小图片的高和宽度 W=H

    private Bitmap mBitmap;        //game graphic

    private List<ImagePiece> mItemBitmaps; //切完图要存储

    private int[] imagId = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4,
            R.drawable.img5, R.drawable.img6, R.drawable.img7, R.drawable.img8, R.drawable.img9,};

    private boolean pLock;

    private RelativeLayout mAnimationView;
    private boolean animaing;
private  boolean show;
    private boolean isGameSuccess;
    private boolean isGameOver;














    public interface GamePintuListener
    {
        void nextLevel(int nextLevel);

        void timechanged(int currentTime);

        void gameover();
    }

    public GamePintuListener mListener;

    public void setOnGamePintuListener(GamePintuListener mListener)
    {
        this.mListener = mListener;
    }

    private int mLevel = 1;
    private static final int TIME_CHANGED = 0x110;
    private static final int NEXT_LEVEL = 0x111;
    private  Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case TIME_CHANGED:
                    if (isGameSuccess || isGameOver || isPause)
                        return;
                    if (mListener != null)
                    {
                        mListener.timechanged(mTime);
                    }
                    if (mTime == 0)
                    {
                        isGameOver = true;
                        mListener.gameover();
                        return;
                    }
                    mTime--;
                    mHandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);

                    break;
                case NEXT_LEVEL:
                    mLevel = mLevel + 1;
                    if (mListener != null)
                    {
                        mListener.nextLevel(mLevel);
                    } else
                    {
                        nextLevel();
                    }
                    break;

            }
        }
    };
    private boolean isTimeEnabled = false;
    private int mTime;
    public void setTimeEnabled(boolean isTimeEnabled)
    {
        this.isTimeEnabled = isTimeEnabled;
    }


    public GameLayout(Context context) {
        this(context, null);
    }

    public GameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mMagin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());//将DP转换为px
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //确定当前布局的大小
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!pLock) {
            initBitmap();   //切图及排序
            initItem(); //设置ImageView(item)属性
            soundPlay();
            checkTimeEnable();
            pLock = true;
        }
        setMeasuredDimension(mWidth, mWidth);

    }
    private void checkTimeEnable()
    {
        if (isTimeEnabled)
        {
            // 根据当前等级设置时间
            countTimeBaseLevel();
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }

    }

    private void countTimeBaseLevel()
    {
        mTime = (int) Math.pow(2, mLevel) * 60;
    }

    private void initBitmap() {

         if (mBitmap == null) {
        mBitmap = null;
        mBitmap = BitmapFactory.decodeResource(getResources(), imagId[iI]);
           }

        mItemBitmaps = ImageSplitterUtil.splitImage(mBitmap, mChunk);

        Collections.sort(mItemBitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });
    }

    private void initItem() {

        mItemWidth = (mWidth - mPadding * 2 - mMagin * (mChunk - 1)) / mChunk; //20:00
        mGameItems = new ImageView[mChunk * mChunk];

        for (int i = 0; i < mGameItems.length; i++) {

            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);

            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());

            mGameItems[i] = item;

            item.setId(i + 1);

            item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);

            //设置横向间距 28:00
            if ((i + 1) % mChunk != 0) {
                lp.rightMargin = mMagin;
            }

            if (i % mChunk != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, mGameItems[i - 1].getId());
            }

            if ((i + 1) > mChunk)

            {
                lp.topMargin = mMagin;
                lp.addRule(RelativeLayout.BELOW, mGameItems[i - mChunk].getId());
            }

            addView(item, lp);

        }

    }
    ///@@@@@@@@@@@@@@@@@
    public void restart()
    {
        isGameOver = false;
        mChunk--;
        nextLevel();
    }
    private boolean isPause ;
    public void pause()
    {
        isPause = true ;
        mHandler.removeMessages(TIME_CHANGED);
    }

    public void resume()
    {
        if(isPause)
        {
            isPause = false ;
            mHandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    public void nextLevel()
    {
        this.removeAllViews();
        mAnimationView=null;
        mChunk++;
        isGameSuccess = false;
        checkTimeEnable();
        initBitmap();
        initItem();
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@
    //min 获取多个参数的最小值 test(int... a) 多参数传入
    private int min(int... params) {

        int min = params[0];

        for (int param : params)     //增强型for循环，遍历
        {
            if (param < min)

                min = param;

        }
        return min;
    }


    private Map<Integer,Integer> soundMap;
    private SoundPool soundpool;


    public void soundPlay(){

        soundpool =new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        soundMap =new HashMap<Integer,Integer>();
        soundMap.put(1,soundpool.load(getContext(),R.raw.step,1));
        soundMap.put(2,soundpool.load(getContext(),R.raw.step2,1));


    }


    private ImageView mFirst;
    private ImageView mSecond;

    @Override
    public void onClick(View v) {

        if (animaing||show)return;

        if (mFirst == v) {
            mFirst.setColorFilter(null);

            mFirst = null;
            return;

        }
        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#4400FF00"));
            soundpool.play(soundMap.get(1),1,1,0,0,1);

        } else {
            mSecond = (ImageView) v;
            exchangeView();

        }


    }

//    public void eazyLevel() {
//        this.removeAllViews();
//        mChunk = 3;
//
//        initBitmap();
//        initItem();
//    }
//
//    public void nomalLevel() {
//        this.removeAllViews();
//        mChunk = 4;
//
//        initBitmap();
//        initItem();
//    }
//
//    public void hardLevel() {
//        this.removeAllViews();
//        mChunk = 5;
//
//        initBitmap();
//        initItem();
//    }

    public void changeImage() {
        this.removeAllViews();
        mBitmap =null;
        if (iI == 8) {
            iI = 0;
        } else {
            iI++;
        }
       isGameOver=false;
        mChunk--;
        nextLevel();



    }

    public void showpic(){
        show=true;
    pic = new ImageView(getContext());
        pic.setImageResource(imagId[iI]);
        addView(pic);

    }

    public void disapic(){
        pic.setVisibility(GONE);
        show=false;
    }


    private void exchangeView() {

        mFirst.setColorFilter(null);

        setUpAnimView();
        /*
         * donghuaceng1
         */
        ImageView animfirst = new ImageView(getContext());
        animfirst.setImageBitmap(mItemBitmaps.get(getImageId((String) mFirst.getTag())).getBitmap());

        LayoutParams alp = new LayoutParams(mItemWidth, mItemWidth);
        alp.leftMargin = mFirst.getLeft() - mPadding;
        alp.topMargin = mFirst.getTop() - mPadding;

        animfirst.setLayoutParams(alp);
        mAnimationView.addView(animfirst);
        soundpool.play(soundMap.get(2),1,1,0,0,1);

/*donghuaceng 2
*
* */
        ImageView animsecond = new ImageView(getContext());
        animsecond.setImageBitmap(mItemBitmaps.get(getImageId((String) mSecond.getTag())).getBitmap());

        LayoutParams alp2 = new LayoutParams(mItemWidth, mItemWidth);
        alp2.leftMargin = mSecond.getLeft() - mPadding;
        alp2.topMargin = mSecond.getTop() - mPadding;

        animsecond.setLayoutParams(alp2);
        mAnimationView.addView(animsecond);

        /*
        * 设置动画
        *
        * */

        TranslateAnimation animation1;
        animation1 = new TranslateAnimation(0, mSecond.getLeft() - mFirst.getLeft(),
                0, mSecond.getTop() - mFirst.getTop());

        animation1.setDuration(300);
        animation1.setFillAfter(true);
        animfirst.startAnimation(animation1);

        TranslateAnimation animation2;
        animation2 = new TranslateAnimation(0, -mSecond.getLeft() + mFirst.getLeft(),
                0, -mSecond.getTop() + mFirst.getTop());

        animation2.setDuration(300);
        animation2.setFillAfter(true);
        animsecond.startAnimation(animation2);

        /*
        * jianting
        * */

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                animaing =true;

                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onAnimationEnd(Animation animation) {


                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();

                String[] firstParams = firstTag.split("_");
                String[] secondParams = secondTag.split("_");
                mFirst.setImageBitmap(mItemBitmaps.get(Integer.parseInt(secondParams[0])).getBitmap());
                mSecond.setImageBitmap(mItemBitmaps.get(Integer.parseInt(firstParams[0])).getBitmap());


                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);

                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);

                mAnimationView.removeAllViews();
                mFirst = mSecond = null;

                //1-5 juge completed

                checkWin();
                animaing =false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });





    }


    public int getImageId(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

//    public int getImageIndex(String tag) {
//        String[] split = tag.split("_");
//        return Integer.parseInt(split[1]);
//
//    }


    private void setUpAnimView() {
        if (mAnimationView == null) {
            mAnimationView = new RelativeLayout(getContext());
            addView(mAnimationView);
        }
    }

    private void checkWin() {
        boolean odered = true;

        for (int i = 0; i < mGameItems.length; i++) {

            ImageView imageView = mGameItems[i];

            Log.e("TAG", getIndexByTag((String) imageView.getTag()) + "");
            if (getIndexByTag((String) imageView.getTag()) != i) {
                odered = false;

            }

        }
        if (odered) {
            Log.e("TAG", "WIN");
            Toast.makeText(getContext(), "YOU ARE WINNER! ", Toast.LENGTH_LONG).show();
            isGameSuccess = true;
            mHandler.removeMessages(TIME_CHANGED);
            mHandler.sendEmptyMessage(NEXT_LEVEL);
        }


    }

    private int getIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);

    }


}
