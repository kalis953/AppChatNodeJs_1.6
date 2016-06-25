package com.ptit.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by nguye on 16-Jun-16.
 */
public class ConvertImage {

    public static String Image_to_String(ImageView v){
        BitmapDrawable drawable = (BitmapDrawable) v.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String strHinh = Base64.encodeToString(byteArray, 0);
        return strHinh;
    }

    public static void String_to_Image(String strBase64, ImageView v){
        byte[] decodeString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        v.setImageBitmap(decodeByte);
    }
}
