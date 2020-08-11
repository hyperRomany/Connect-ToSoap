package com.example.connecttosoapapiapp.Promotion.Modules;

public class Prom_item_Module {

    public static final String TABLE_Prom_NAME = "PROMITEM";

    public static final String Discountno = "Discountno";
    public static final String Date_from = "Date_from";
    public static final String Date_to = "Date_to";
    public static final String Discounttype = "Discounttype";
    public static final String Prom_desc = "Prom_desc";
    public static final String last_modified_time = "last_modified_time";
    public static final String prom_post = "prom_post";
    public static final String status = "status";
    public static final String itemean = "itemean";
    public static final String department = "department";
    public static final String barcode = "barcode";
    public static final String item_desc = "item_desc";
    public static final String return_type = "return_type";
    public static final String qty_std_price = "qty_std_price";

    public static final String sell_price = "sell_price";
    public static final String vatrate = "vatrate";
    public static final String discountvalue = "discountvalue";
    public static final String note_id = "note_id";

    public static final String supervisor_id = "supervisor_id";
    public static final String supervisor_name = "supervisor_name";
    public static final String OtherNotes = "OtherNotes";

    // Create table SQL query
    public static final String CREATE_Prom_TABLE =
            "CREATE TABLE " + TABLE_Prom_NAME + "("
                    + Discountno + " VARCHAR(15),"
                    + Date_from + " VARCHAR(10),"
                    + Date_to + " VARCHAR(10),"
                    + Discounttype + " VARCHAR(35),"
                    + Prom_desc + " VARCHAR(12),"
                    + last_modified_time + " VARCHAR(12),"
                    + prom_post + " VARCHAR(10),"
                    + status + " VARCHAR(10),"
                    + itemean + " VARCHAR(10),"
                    + department + " VARCHAR(20),"
                    + barcode + " VARCHAR(50),"
                    + item_desc + " VARCHAR(20),"
                    + qty_std_price + " VARCHAR(20),"
                    + return_type + " VARCHAR(20),"

                    + sell_price + " VARCHAR(20),"
                    + vatrate + " VARCHAR(20),"
                    + discountvalue + " VARCHAR(20),"
                    + note_id + " VARCHAR(20),"
                    + supervisor_id + " VARCHAR(20),"
                    + supervisor_name + " VARCHAR(20),"
                    + OtherNotes + " VARCHAR(200)"

                    + ")";

    private String Discountno1, Date_from1, Date_to1, Discounttype1, Prom_desc1, last_modified_time1,
            prom_post1, status1, itemean1, department1, barcode1, item_desc1,return_type1,
            sell_price1,qty_std_price1,vatrate1,discountvalue1,note_id1,supervisor_id1,supervisor_name1,OtherNotes1;
private  Boolean UpdateOrInsert;

    public Boolean getUpdateOrInsert() {
        return UpdateOrInsert;
    }

    public String getQty_std_price1() {
        return qty_std_price1;
    }

    public void setQty_std_price1(String qty_std_price1) {
        this.qty_std_price1 = qty_std_price1;
    }

    public void setUpdateOrInsert(Boolean updateOrInsert) {
        UpdateOrInsert = updateOrInsert;
    }

    public String getOtherNotes1() {
        return OtherNotes1;
    }

    public void setOtherNotes1(String otherNotes1) {
        OtherNotes1 = otherNotes1;
    }

    public String getDiscountno1() {
        return Discountno1;
    }

    public void setDiscountno1(String discountno1) {
        Discountno1 = discountno1;
    }

    public String getDate_from1() {
        return Date_from1;
    }

    public void setDate_from1(String date_from1) {
        Date_from1 = date_from1;
    }

    public String getDate_to1() {
        return Date_to1;
    }

    public void setDate_to1(String date_to1) {
        Date_to1 = date_to1;
    }

    public String getDiscounttype1() {
        return Discounttype1;
    }

    public void setDiscounttype1(String discounttype1) {
        Discounttype1 = discounttype1;
    }

    public String getProm_desc1() {
        return Prom_desc1;
    }

    public void setProm_desc1(String prom_desc1) {
        Prom_desc1 = prom_desc1;
    }

    public String getLast_modified_time1() {
        return last_modified_time1;
    }

    public void setLast_modified_time1(String last_modified_time1) {
        this.last_modified_time1 = last_modified_time1;
    }

    public String getProm_post1() {
        return prom_post1;
    }

    public void setProm_post1(String prom_post1) {
        this.prom_post1 = prom_post1;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getItemean1() {
        return itemean1;
    }

    public void setItemean1(String itemean1) {
        this.itemean1 = itemean1;
    }

    public String getDepartment1() {
        return department1;
    }

    public void setDepartment1(String department1) {
        this.department1 = department1;
    }

    public String getBarcode1() {
        return barcode1;
    }

    public void setBarcode1(String barcode1) {
        this.barcode1 = barcode1;
    }

    public String getItem_desc1() {
        return item_desc1;
    }

    public void setItem_desc1(String item_desc1) {
        this.item_desc1 = item_desc1;
    }

    public String getReturn_type1() {
        return return_type1;
    }

    public void setReturn_type1(String return_type1) {
        this.return_type1 = return_type1;
    }

    public String getSell_price1() {
        return sell_price1;
    }

    public void setSell_price1(String sell_price1) {
        this.sell_price1 = sell_price1;
    }

    public String getVatrate1() {
        return vatrate1;
    }

    public void setVatrate1(String vatrate1) {
        this.vatrate1 = vatrate1;
    }

    public String getDiscountvalue1() {
        return discountvalue1;
    }

    public void setDiscountvalue1(String discountvalue1) {
        this.discountvalue1 = discountvalue1;
    }

    public String getNote_id1() {
        return note_id1;
    }

    public void setNote_id1(String note_id1) {
        this.note_id1 = note_id1;
    }

    public String getSupervisor_id1() {
        return supervisor_id1;
    }

    public void setSupervisor_id1(String supervisor_id1) {
        this.supervisor_id1 = supervisor_id1;
    }

    public String getSupervisor_name1() {
        return supervisor_name1;
    }

    public void setSupervisor_name1(String supervisor_name1) {
        this.supervisor_name1 = supervisor_name1;
    }
}
