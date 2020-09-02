package com.justfoodz.models;

public class MemberModel {

    public String id,friend_name,friend_email,friend_mobile,statusm ,groupmemberid;

    public MemberModel(String id,String friend_name,String friend_email,String friend_mobile,String statusm,String groupmemberid) {
        this.id=id;
        this.friend_name=friend_name;
        this.friend_email=friend_email;
        this.friend_mobile=friend_mobile;
        this.statusm=statusm;
        this.groupmemberid=groupmemberid;

        }

    public String getGroupmemberid() {
        return groupmemberid;
    }

    public void setGroupmemberid(String groupmemberid) {
        this.groupmemberid = groupmemberid;
    }

    public String getStatusM() {

        return statusm;
    }

    public void setStatusM(String statusm) {
        this.statusm = statusm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public String getFriend_email() {
        return friend_email;
    }

    public void setFriend_email(String friend_email) {
        this.friend_email = friend_email;
    }

    public String getFriend_mobile() {
        return friend_mobile;
    }

    public void setFriend_mobile(String friend_mobile) {
        this.friend_mobile = friend_mobile;
    }
}
