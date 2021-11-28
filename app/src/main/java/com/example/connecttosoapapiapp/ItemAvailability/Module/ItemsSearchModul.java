package com.example.connecttosoapapiapp.ItemAvailability.Module;

public class ItemsSearchModul {
    private String BarCode ,Describtion ,itemean;

    public ItemsSearchModul(String barCode, String describtion, String itemean) {
        BarCode = barCode;
        Describtion = describtion;
        this.itemean = itemean;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getDescribtion() {
        return Describtion;
    }

    public void setDescribtion(String describtion) {
        Describtion = describtion;
    }

    public String getItemean() {
        return itemean;
    }

    public void setItemean(String itemean) {
        this.itemean = itemean;
    }
}
