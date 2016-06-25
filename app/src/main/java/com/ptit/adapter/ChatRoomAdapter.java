package com.ptit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptit.appchatnodejs.R;
import com.ptit.model.ChatRoom;
import com.ptit.utils.CircleImageBitmap;
import com.ptit.utils.ConvertImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoang Sang on 3/7/2016.
 */
public class ChatRoomAdapter extends ArrayAdapter<ChatRoom> {

    Activity context;
    int resource;
    ArrayList<ChatRoom> objects;

    LayoutInflater inflater;

    public ChatRoomAdapter(Activity context, int resource, ArrayList<ChatRoom> objects) {
        super(context, resource, objects);

        this.context = context;
        this.objects = objects;
        this.resource = resource;

        inflater = LayoutInflater.from(context);
    }

    private static class ViewHolder{
        ImageView imgRoom;
        TextView txtTitle,txtOnlinePeople,txtOwned;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(resource,null);

            viewHolder = new ViewHolder();
            viewHolder.imgRoom = (ImageView) convertView.findViewById(R.id.imgRoom);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
            viewHolder.txtOwned = (TextView) convertView.findViewById(R.id.txtOwned);
            viewHolder.txtOnlinePeople = (TextView) convertView.findViewById(R.id.txtOnlinePeople);

            convertView.setTag(viewHolder);

        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChatRoom room  = this.objects.get(position);

        viewHolder.txtTitle.setText(room.getTitle());
        viewHolder.txtOwned.setText(room.getOwned());
        viewHolder.txtOnlinePeople.setText("(" + String.valueOf(room.getOnlinePeople()) + ")");

        ConvertImage.String_to_Image(room.getImage(), viewHolder.imgRoom);

        Bitmap bitmap = ((BitmapDrawable)viewHolder.imgRoom.getDrawable()).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap,80,80,true);
        bitmap = CircleImageBitmap.getCircleBitmap(bitmap);
        viewHolder.imgRoom.setImageBitmap(bitmap);

        return convertView;
    }

}
