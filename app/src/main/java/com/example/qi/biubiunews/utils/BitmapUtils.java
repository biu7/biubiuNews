package com.example.qi.biubiunews.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaActionSound;

/**
 * Created by qi on 17-5-8.
 */

public class BitmapUtils {
public static Bitmap circleBitmap(Bitmap source){
    int width = source.getWidth();
    Bitmap bitmap = Bitmap.createBitmap(width,width,Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    canvas.drawCircle(width/2,width/2,width/2,paint);

    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

    canvas.drawBitmap(source,0,0,paint);
    return bitmap;
}


}
