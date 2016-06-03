package com.ddf.fusiyang.ayyad.CutPicture;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by fusiyang on 2016/5/13.
 * bitmap cut of tu piece^2
 */
public class ImageSplitterUtil {

    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece) {

        List<ImagePiece> imagePieces = new ArrayList<ImagePiece>();

        int width = bitmap.getWidth(); //get W&h from bitmap
        int height = bitmap.getHeight();

        int pieceWidth = Math.min(width, height) / piece; //each W of piece

        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * piece);
                int x = j * pieceWidth;
                int y = i * pieceWidth;

                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y,
                        pieceWidth, pieceWidth));

                imagePieces.add(imagePiece);
                //  imagePieces.remove(piece*piece-1);
            }
        }
        return imagePieces;
    }
}
