package com.ddf.fusiyang.ayyad.CutPicture;

import android.graphics.Bitmap;

/**
 * Created by fusiyang on 2016/5/14.
 */

public class ImagePiece {

    private int index;
    private Bitmap bitmap;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImagePiece(int index, Bitmap bitmap) {
        this.index = index;
        this.bitmap = bitmap;

    }

    public ImagePiece() {


    }

    @Override
    public String toString() {
        return "ImagePiece{" +
                "index=" + index +
                ", bitmap=" + bitmap +
                '}';
    }
}
