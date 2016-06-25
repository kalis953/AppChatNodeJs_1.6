package com.ptit.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hoang Sang on 3/7/2016.
 */
public class ChatRoom implements Serializable{

    private String title;
    private String owned;
    private String image;
    private int onlinePeople;
    private String content;
    private ArrayList<User> arrayListUser;

    public ChatRoom() {
    }

    public ChatRoom(String title, String owned, String image, int onlinePeople, String content, ArrayList<User> arrayListUser) {
        this.title = title;
        this.owned = owned;
        this.image = image;
        this.onlinePeople = onlinePeople;
        this.content = content;
        this.arrayListUser = arrayListUser;
    }

    public ArrayList<User> getArrayListUser() {
        return arrayListUser;
    }

    public void setArrayListUser(ArrayList<User> arrayListUser) {
        this.arrayListUser = arrayListUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwned() {
        return owned;
    }

    public void setOwned(String owned) {
        this.owned = owned;
    }

    public int getOnlinePeople() {
        return onlinePeople;
    }

    public void setOnlinePeople(int onlinePeople) {
        this.onlinePeople = onlinePeople;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
