package com.example.connecttosoapapiapp.ItemAvailability.Module;

public class ItemAvailabilityModule {

    public static final String TABLE_ItemAvailability_NAME = "ItemAvailability";

    public static final String UserName = "UserName";
    public static final String BarCode = "BarCode";
    public static final String Message = "Message";

    private String UserName1;
    private String BarCode1;
    private String Message1;

    // Create table SQL query
    public static final String CREATE_ItemAvailabilityModule_TABLE =
            "CREATE TABLE " + TABLE_ItemAvailability_NAME + "("
                    + UserName + " VARCHAR(10),"
                    + BarCode + " VARCHAR(50),"
                    + Message + " VARCHAR(200)"
                    + ")";

    public ItemAvailabilityModule() {
    }

    public ItemAvailabilityModule(String userName1, String barCode1, String message1) {
        UserName1 = userName1;
        BarCode1 = barCode1;
        Message1 = message1;
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
}
