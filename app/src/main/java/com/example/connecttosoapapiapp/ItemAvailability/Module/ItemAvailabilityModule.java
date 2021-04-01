package com.example.connecttosoapapiapp.ItemAvailability.Module;

public class ItemAvailabilityModule {

    public static final String TABLE_ItemAvailability_NAME = "ItemAvailability";

    public static final String UserName = "UserName";
    public static final String BarCode = "BarCode";
    public static final String Todaysales = "Todaysales";
    public static final String Avaliblestock = "Avaliblestock";

    public static final String Message = "Message";

    private String UserName1;
    private String BarCode1;
    private String Message1;

    private String todaysales;
    private String avaliblestock;

    // Create table SQL query
    public static final String CREATE_ItemAvailabilityModule_TABLE =
            "CREATE TABLE " + TABLE_ItemAvailability_NAME + "("
                    + UserName + " VARCHAR(10),"
                    + BarCode + " VARCHAR(50),"
                    + Todaysales + " VARCHAR(50),"
                    + Avaliblestock + " VARCHAR(50),"
                    + Message + " VARCHAR(200)"
                    + ")";

    public ItemAvailabilityModule() {
    }

    public ItemAvailabilityModule(String userName1, String barCode1, String message1, String todaysales, String avaliblestock) {
        UserName1 = userName1;
        BarCode1 = barCode1;
        Message1 = message1;
        this.todaysales = todaysales;
        this.avaliblestock = avaliblestock;
    }

    public String getUserName1() {
        return UserName1;
    }

    public void setUserName1(String userName1) {
        UserName1 = userName1;
    }

    public String getBarCode1() {
        return BarCode1;
    }

    public void setBarCode1(String barCode1) {
        BarCode1 = barCode1;
    }

    public String getMessage1() {
        return Message1;
    }

    public void setMessage1(String message1) {
        Message1 = message1;
    }

    public String getTodaysales() {
        return todaysales;
    }

    public void setTodaysales(String todaysales) {
        this.todaysales = todaysales;
    }

    public String getAvaliblestock() {
        return avaliblestock;
    }

    public void setAvaliblestock(String avaliblestock) {
        this.avaliblestock = avaliblestock;
    }
}
