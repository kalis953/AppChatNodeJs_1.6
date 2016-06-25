package com.ptit.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.ptit.appchatnodejs.R;
import com.ptit.supporter.mToast;

/**
 * Created by nguye on 17-Jun-16.
 */
public class ConnectionManager {

    private Activity context;

    public ConnectionManager(Activity context) {
        this.context = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager check = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = check.getAllNetworkInfo();

        for (int i = 0; i < infos.length; i++){
            if (infos[i].getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
        }
        mToast.toastLong(context, context.getString(R.string.txtConnectFail));
        return false;
    }
}
