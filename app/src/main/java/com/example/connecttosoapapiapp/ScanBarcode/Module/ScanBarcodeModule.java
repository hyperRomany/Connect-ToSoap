package com.example.connecttosoapapiapp.ScanBarcode.Module;

public class ScanBarcodeModule {

    public static final String TABLE_ScanBarcode_NAME = "ScanBarCode";

    public static final String BarCode = "BarCode";
    public static final String QTY = "QTY";
    public static final String Zone = "Zone";

    private String BarCode1;
    private String QTY1;
    private String Zone1;

    // Create table SQL query
    public static final String CREATE_ScanBarCodeModule_TABLE =
            "CREATE TABLE " + TABLE_ScanBarcode_NAME + "("
                    + BarCode + " VARCHAR(10),"
                    + QTY + " VARCHAR(10),"
                    + Zone + " VARCHAR(10)"
                    + ")";

    public ScanBarcodeModule(String barCode1, String QTY1, String zone1) {
        BarCode1 = barCode1;
        this.QTY1 = QTY1;
        Zone1 = zone1;
    }

    public ScanBarcodeModule() {
    }

    public static String getBarCode() {
        return BarCode;
    }

    public static String getQTY() {
        return QTY;
    }

    public static String getZone() {
        return Zone;
    }

    public String getBarCode1() {
        return BarCode1;
    }

    public void setBarCode1(String barCode1) {
        BarCode1 = barCode1;
    }

    public String getQTY1() {
        return QTY1;
    }

    public void setQTY1(String QTY1) {
        this.QTY1 = QTY1;
    }

    public String getZone1() {
        return Zone1;
    }

    public void setZone1(String zone1) {
        Zone1 = zone1;
    }

}
