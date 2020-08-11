package com.example.connecttosoapapiapp.ReceivingModule.model;

public class Groups {

    public static final String Group_ID="Group_ID";
    private String Group_ID1;


    public static final String TABLE_Group_Name = "Groups_table";

    // Create table SQL query
    public static final String CREATE_Groups_TABLE =
            "CREATE TABLE " + TABLE_Group_Name + "("
                    // +ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +Group_ID + " VARCHAR(15)"
                    + ")";

    public String getGroup_ID1() {
        return Group_ID1;
    }

    public void setGroup_ID1(String group_ID1) {
        Group_ID1 = group_ID1;
    }
}
