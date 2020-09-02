package com.justfoodz.models;

public class GroupModel {
    public String id,groupname,status,group_id ,group_member_id ;

    public GroupModel(String id,String groupname,String status ,String group_id,String group_member_id) {
        this.id=id;
        this.groupname=groupname;
        this.status=status;
        this.group_id=group_id;
        this.group_member_id=group_member_id;

    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_member_id() {
        return group_member_id;
    }

    public void setGroup_member_id(String group_member_id) {
        this.group_member_id = group_member_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
}
