package com.example.pms_client;

public class FavoriteListItem {

    private int id = 0;
    private String lookupKey = "";
    private String name = "";
    private String phone = "";
    private String phoneTitle = "";
    private int color = 0;
    private boolean shared = false;

    public FavoriteListItem(int id, String lookupKey, String name, String phone, String phoneTitle, int color, boolean shared) {

        this.id = id;
        this.lookupKey = lookupKey;
        this.name = name;
        this.phone = phone;
        this.phoneTitle = phoneTitle;
        this.color = color;
        this.shared = shared;
    }

    public int getId() {
        return id;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return name.substring(0, 1);
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneTitle() {
        return phoneTitle;
    }

    public int getIconColor() {
        return color;
    }

    public boolean getShared() {
        return shared;
    }

}
