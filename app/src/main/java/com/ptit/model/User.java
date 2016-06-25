package com.ptit.model;

import java.io.Serializable;

/**
 * Created by Hoang Sang on 3/7/2016.
 */
public class User implements Serializable{

    private String name;
    private String password;
    private String email;
    private String phone;
    private String room;
    private String avatar;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public User() {

    }

    public User(String name, String password, String email, String phone, String avatar) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
    }

    public User(String name, String password, String email, String phone, String room, String avatar) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.room = room;
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
