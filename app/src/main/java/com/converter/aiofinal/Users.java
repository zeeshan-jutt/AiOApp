package com.converter.aiofinal;

public class Users {

    String uid;
    String name;
    String emailId;
    String phone;
    String address;
    String service;
    String imageUri;

    public Users() {
    }

    public Users(String uid, String name, String emailId, String phone, String address, String service, String imageUri) {
        this.uid = uid;
        this.name = name;
        this.emailId = emailId;
        this.phone = phone;
        this.address = address;
        this.service = service;
        this.imageUri = imageUri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
