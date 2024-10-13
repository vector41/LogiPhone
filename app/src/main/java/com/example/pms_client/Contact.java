package com.example.pms_client;

public class Contact {
    private String name;
    private String officePhone;
    private String mobilePhone;
    private int iconResourceId;

    public Contact(String name, String officePhone, String mobilePhone, int iconResourceId) {
        this.name = name;
        this.officePhone = officePhone;
        this.mobilePhone = mobilePhone;
        this.iconResourceId = iconResourceId;
    }

    public String getName() {
        return name;
    }

    public String getOfficePhone() {
        return officePhone;
    }
    public String getMobilePhone() {
        return mobilePhone;
    }
    public int getIconResourceId() {
        return iconResourceId;
    }
}
