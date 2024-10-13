package com.example.pms_client;

public class SearchContactsItem {

    private String name = "";
    private int contactId = 0;
    private int color = 0;

    public SearchContactsItem(String name, int contactId, int color) {

        this.name = name;
        this.contactId = contactId;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return name.substring(0, 1);
    }

    public int getContactId() {
        return contactId;
    }

    public int getIconColor() {
        return color;
    }
}
