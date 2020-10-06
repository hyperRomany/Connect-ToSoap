package com.example.connecttosoapapiapp.Promotion.Helper;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ExpiredPormotionData {

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    DatabaseHelperForProotion databaseHelperForProotion;



    public Boolean doInBackground(String condition, Context context) {


        try
        {
            ConnectionHelper conStr=new ConnectionHelper();
            connect =conStr.connectionclasss();        // Connect to database
            if (connect == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                databaseHelperForProotion=new DatabaseHelperForProotion(context);

                // Change below query according to your own database.
                String query = "EXEC SP_PROM_GetPromotions_Expired @Cond=N'"+condition+"'";
                PreparedStatement stmt = connect.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    /*Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("discountno",rs.getString("discountno"));
                    datanum.put("date_from",rs.getString("date_from"));
                    datanum.put("date_to",rs.getString("date_to"));
                    datanum.put("discounttype",rs.getString("discounttype"));
                    datanum.put("prom_desc",rs.getString("prom_desc"));
                    datanum.put("last_modified_time",rs.getString("last_modified_time"));
                    datanum.put("prom_post",rs.getString("prom_post"));
                    datanum.put("status",rs.getString("status"));
                    datanum.put("itemean",rs.getString("itemean"));
                    datanum.put("department",rs.getString("department"));
                    datanum.put("barcode",rs.getString("barcode"));
                    datanum.put("item_desc",rs.getString("item_desc"));
                    datanum.put("return_type",rs.getString("return_type"));
                    datanum.put("sell_price",rs.getString("sell_price"));
                    datanum.put("vatrate",rs.getString("vatrate"));
                    datanum.put("discountvalue",rs.getString("discountvalue"));
                    datanum.put("note_id",rs.getString("note_id"));
                    // datanum.put("ot_doc_id",rs.getString("ot_doc_id"));
                    //datanum.put("Capital",rs.getString("ot_doc_id"));
                    data.add(datanum);*/

                    if (!rs.getString("prom_id").isEmpty()) {
                        databaseHelperForProotion.insertProitem(rs.getString("prom_id"), rs.getString("PROM_SDATE"), rs.getString("PROM_EDATE"), rs.getString("prom_type"),
                                rs.getString("prom_desc"), rs.getString("last_modified_time"), rs.getString("prom_post"), rs.getString("status"), rs.getString("itemean"), rs.getString("department")
                                , rs.getString("barcode"), rs.getString("item_desc"),rs.getString("qty_std_price"), rs.getString("sell_price")
                                , rs.getString("vatrate"), rs.getString("discountvalue"), rs.getString("note_id"));
                        isSuccess = true;
                    }
                    else {
                        isSuccess = false;
                    }
                }



                ConnectionResult = " successful";
                connect.close();
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(context,"العرض غير موجود",Toast.LENGTH_LONG).show();
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }

        return isSuccess;
        //return data;
    }

}
