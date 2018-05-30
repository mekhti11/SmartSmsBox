package com.mekhti.smartsmsbox.Entity;

public class Contact {

    private String name;
    private String phoneNum;
    private ContactType type;

    public Contact(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public Contact(String name, String phoneNum, ContactType type) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.type = type;
    }

    public Contact(){}

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "Name :"+name+"\nPhoneNum : "+phoneNum;
    }
}
