
package com.example.aawaz;


import android.os.Parcel;
import android.os.Parcelable;

public class ItemData implements Parcelable {

    private String userId;
    private String name;
    private String phone;
    private String photo;
    private String relative1;
    private String r1phone;
    private String relative2;
    private String r2phone;
    private String aadhar;
    private String aadharImage;
    private String address;

    public ItemData(){}
    public ItemData(String userId, String name, String phone,String photo, String relative1,
                    String r1phone, String relative2, String r2phone, String aadhar, String aadharImage,String address){

        this.userId=userId;
        this.name=name;
        this.phone=phone;
        this.photo=photo;
        this.relative1=relative1;
        this.r1phone=r1phone;
        this.relative2=relative2;
        this.r2phone=r2phone;
        this.aadhar=aadhar;
        this.aadharImage=aadharImage;
        this.address=address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRelative1() {
        return relative1;
    }

    public void setRelative1(String relative1) {
        this.relative1 = relative1;
    }

    public String getR1phone() {
        return r1phone;
    }

    public void setR1phone(String r1phone) {
        this.r1phone = r1phone;
    }

    public String getRelative2() {
        return relative2;
    }

    public void setRelative2(String relative2) {
        this.relative2 = relative2;
    }

    public String getR2phone() {
        return r2phone;
    }

    public void setR2phone(String r2phone) {
        this.r2phone = r2phone;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAadharImage() {
        return aadharImage;
    }

    public void setAadharImage(String aadharImage) {
        this.aadharImage = aadharImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(photo);
        dest.writeString(relative1);
        dest.writeString(r1phone);
        dest.writeString(relative2);
        dest.writeString(r2phone);
        dest.writeString(aadhar);
        dest.writeString(aadharImage);
        dest.writeString(address);
    }

    protected ItemData(Parcel in) {
        userId = in.readString();
        name = in.readString();
        phone = in.readString();
        photo = in.readString();
        relative1= in.readString();
        r1phone= in.readString();
        relative2=in.readString();
        r2phone= in.readString();
        aadhar= in.readString();
        aadharImage= in.readString();
        address= in.readString();
    }

    public static final Creator<ItemData> CREATOR = new Creator<ItemData>() {
        @Override
        public ItemData createFromParcel(Parcel source) {
            return new ItemData(source);
        }

        @Override
        public ItemData[] newArray(int size) {
            return new ItemData[size];
        }
    };
}
