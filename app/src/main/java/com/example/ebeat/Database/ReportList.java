package com.example.ebeat.Database;

import java.util.Date;

public class ReportList {
    String officer_name;
    String officer_id;
    Date date, time_stamp;
    String place_name;
    String place_type;
    byte[] image;
    String remarks;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getPlace_type() {
        return place_type;
    }

    public void setPlace_type(String place_type) {
        this.place_type = place_type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ReportList(String officer_name, String officer_id, Date date, Date time_stamp, String place_name, String place_type, byte[] image, String remarks) {
        this.officer_name = officer_name;
        this.officer_id = officer_id;
        this.date = date;
        this.time_stamp = time_stamp;
        this.place_name = place_name;
        this.place_type = place_type;
        this.image = image;
        this.remarks = remarks;
    }

    public String getOfficer_name() {
        return officer_name;
    }

    public void setOfficer_name(String officer_name) {
        this.officer_name = officer_name;
    }

    public String getOfficer_id() {
        return officer_id;
    }

    public void setOfficer_id(String officer_id) {
        this.officer_id = officer_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

}
