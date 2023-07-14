package com.example.cvm_mobile_application.data.db.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Citizen implements Parcelable {
    private String id;
    private String last_name;
    private String first_name;
    private String full_name;
    private Timestamp birthday;
    private String gender;
    private String hometown;
    private String province_name;
    private String district_name;
    private String ward_name;
    private String street;
    private String phone;
    private String email;
    private String guardian;

    public Citizen() {
        id = "";
        last_name = "";
        full_name = "";
        birthday = new Timestamp(new Date());
        gender = "";
        hometown = "";
        province_name = "";
        district_name = "";
        ward_name = "";
        street = "";
        phone = "";
        email = "";
        guardian = "";
    }

    protected Citizen(Parcel in) {
        id = in.readString();
        last_name = in.readString();
        first_name = in.readString();
        full_name = in.readString();

        String dateString = in.readString();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            birthday =  new Timestamp(df.parse(dateString));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        gender = in.readString();
        hometown = in.readString();
        province_name = in.readString();
        district_name = in.readString();
        ward_name = in.readString();
        street = in.readString();
        phone = in.readString();
        email = in.readString();
        guardian = in.readString();
    }

    public static final Creator<Citizen> CREATOR = new Creator<Citizen>() {
        @Override
        public Citizen createFromParcel(Parcel in) {
            return new Citizen(in);
        }

        @Override
        public Citizen[] newArray(int size) {
            return new Citizen[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public String getBirthdayString() {
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = birthday.toDate();
        return df.format(date);
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public void setBirthdayFromString(String birthday) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = df.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.birthday = new Timestamp(date);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getWard_name() {
        return ward_name;
    }

    public void setWard_name(String ward_name) {
        this.ward_name = ward_name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getFull_name() {
        return full_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(last_name);
        dest.writeString(first_name);
        dest.writeString(full_name);

        Date date = birthday.toDate();
        @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = df.format(date);
        dest.writeString(birthday);

        dest.writeString(gender);
        dest.writeString(hometown);
        dest.writeString(province_name);
        dest.writeString(district_name);
        dest.writeString(ward_name);
        dest.writeString(street);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(guardian);
    }
}
