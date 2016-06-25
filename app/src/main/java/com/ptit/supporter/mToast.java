package com.ptit.supporter;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Kalis on 6/14/2016.
 */
public class mToast {
    public static void toastLong(Activity activity,String msg)
    {
        Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
    }


    public static void toastShort(Activity activity,String msg)
    {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

}
