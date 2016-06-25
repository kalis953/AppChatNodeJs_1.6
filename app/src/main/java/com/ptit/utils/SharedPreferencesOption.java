package com.ptit.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.ptit.appchatnodejs.R;
import com.ptit.model.User;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Kalis on 6/24/2016.
 */
public class SharedPreferencesOption {

     public static SharedPreferences getPreferences (Activity activity,String fileName){
         SharedPreferences pre =activity.getSharedPreferences(fileName, activity.MODE_PRIVATE);
         return pre;
    }

    public static User getUserCurrent (SharedPreferences pre, String key){
        User user = new User();
        if (pre.contains(key)){
            String jsonUser = pre.getString(key, "");
            if (jsonUser!=null)
            {
                User[] arrUser = new Gson().fromJson(jsonUser, User[].class);
                user = arrUser[0];
            }
//                user = new Gson().fromJson(jsonUser, User.class);
        }
        return user;
    }

    public static ArrayList<User> getListUserPreferences(SharedPreferences pre,String key)
    {
        ArrayList<User> users = new ArrayList<>();
        if (pre.contains(key) ){
            String jsonUser = pre.getString(key,null);
            if (jsonUser != null){
                User[] arrUsers = new Gson().fromJson(jsonUser, User[].class);
                users = new ArrayList<>(Arrays.asList(arrUsers));
            }
            else {
                users = new ArrayList<>();
            }
        }
        return users;
    }

    public static void saveUserPreferences(SharedPreferences pre,String key, User user,boolean isSaved)
    {
//        boolean ttdn = pre.getBoolean("statusSaved", false);
        if(isSaved)
        {
            ArrayList<User> users = getListUserPreferences(pre,key);
            for(User u:users){
                if (u.getName().equalsIgnoreCase(user.getName())){
                    users.remove(u);
                    break;
                }
            }
            users.add(user);
            String jsonUser = new Gson().toJson(users);

            SharedPreferences.Editor editor = pre.edit();
            editor.putString(key,jsonUser);
            editor.commit();

        }

    }

    public void readUserPreferences(SharedPreferences pre,String key, User user)
    {
//        boolean ttdn = pre.getBoolean("statusSaved", false);
//        chksaveaccount.setChecked(ttdn);
//        ArrayList<User> users = getListUserPreferences(pre,key);
    }
}
