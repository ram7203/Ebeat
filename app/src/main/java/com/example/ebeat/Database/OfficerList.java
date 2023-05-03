package com.example.ebeat.Database;

public class OfficerList {
    String id;
    String name;
    String rank;
    String beat_id;
    String supervisor_name;
    long phone_no;
    int no_of_patrols;

    public OfficerList(String id, String name, String rank, String beat_id, String supervisor_name, long phone_no) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.beat_id = beat_id;
        this.supervisor_name = supervisor_name;
        this.phone_no = phone_no;
    }

    public OfficerList(String id, String name, String rank, String beat_id, String supervisor_name, long phone_no, int no_of_patrols) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.beat_id = beat_id;
        this.supervisor_name = supervisor_name;
        this.phone_no = phone_no;
        this.no_of_patrols = no_of_patrols;
    }

    public int getNo_of_patrols() {
        return no_of_patrols;
    }

    public void setNo_of_patrols(int no_of_patrols) {
        this.no_of_patrols = no_of_patrols;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getBeat_id() {
        return beat_id;
    }

    public void setBeat_id(String beat_id) {
        this.beat_id = beat_id;
    }

    public String getSupervisor_name() {
        return supervisor_name;
    }

    public void setSupervisor_name(String supervisor_name) {
        this.supervisor_name = supervisor_name;
    }

    public long getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(long phone_no) {
        this.phone_no = phone_no;
    }
}
