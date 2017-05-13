package com.example.qi.biubiunews.utils;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

/**
 * Created by qi on 17-5-8.
 */

public class CircleTransform implements Transformation {

    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap bitmap = BitmapUtils.circleBitmap(source);
        source.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "";
    }
}
