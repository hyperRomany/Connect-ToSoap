package com.example.connecttosoapapiapp.ReceivingModule.model;

public class Users {

    //public static final String ID="ID";
    public static final String User_ID="User_ID";
    public static final String User_Name="User_Name";
   // public static String Password;
    public static final String User_Describtion="User_Describtion";
    public static final String Group_Name="Group_Name";
    public static final String User_status="User_status";
    public static final String User_Department="User_Department";
    public static final String Company="Company";
    public static final String Group_ID="Group_ID";
    public static final String ComplexID="ComplexID";

    public static final String TABLE_User_Name = "User";

    // Create table SQL query
    public static final String CREATE_User_TABLE =
            "CREATE TABLE " + TABLE_User_Name + "("
                   // +ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    +User_ID + " VARCHAR(15),"
                    +User_Name  + " VARCHAR(15),"
                    +User_Describtion + " VARCHAR(15),"
                    +Group_Name  + " VARCHAR(15),"
                    +User_status  + " VARCHAR(15),"
                    +User_Department  + " VARCHAR(15),"
                    +Company  + " VARCHAR(15),"
                    +Group_ID  + " VARCHAR(15),"
                    + ComplexID + " VARCHAR(15)"
                    + ")";


    private   String User_ID1,User_Name1,User_Describtion1,Group_Name1,User_status1
            ,User_Department1,Company1,Group_ID1,ComplexID1;

    public Users() {
    }

    public Users(String user_ID, String user_Name, String user_Describtion, String group_Name,
                 String user_status, String user_Department, String company, String group_ID, String complexID) {
        User_ID1 = user_ID;
        User_Name1 = user_Name;
        User_Describtion1 = user_Describtion;
        Group_Name1 = group_Name;
        User_status1 = user_status;
        User_Department1 = user_Department;
        Company1 = company;
        Group_ID1 = group_ID;
        ComplexID1 = complexID;
    }

    public String getUser_ID1() {
        return User_ID1;
    }

    public void setUser_ID1(String user_ID1) {
        User_ID1 = user_ID1;
    }

    public String getUser_Name1() {
        return User_Name1;
    }

    public void setUser_Name1(String user_Name1) {
        User_Name1 = user_Name1;
    }

    public String getUser_Describtion1() {
        return User_Describtion1;
    }

    public void setUser_Describtion1(String user_Describtion1) {
        User_Describtion1 = user_Describtion1;
    }

    public String getGroup_Name1() {
        return Group_Name1;
    }

    public void setGroup_Name1(String group_Name1) {
        Group_Name1 = group_Name1;
    }

    public String getUser_status1() {
        return User_status1;
    }

    public void setUser_status1(String user_status1) {
        User_status1 = user_status1;
    }

    public String getUser_Department1() {
        return User_Department1;
    }

    public void setUser_Department1(String user_Department1) {
        User_Department1 = user_Department1;
    }

    public String getCompany1() {
        return Company1;
    }

    public void setCompany1(String company1) {
        Company1 = company1;
    }

    public String getGroup_ID1() {
        return Group_ID1;
    }

    public void setGroup_ID1(String group_ID1) {
        Group_ID1 = group_ID1;
    }

    public String getComplexID1() {
        return ComplexID1;
    }

    public void setComplexID1(String complexID1) {
        ComplexID1 = complexID1;
    }
}
