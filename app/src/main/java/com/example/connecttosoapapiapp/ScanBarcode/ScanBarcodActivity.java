package com.example.connecttosoapapiapp.ScanBarcode;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;
import com.example.connecttosoapapiapp.ReceivingModule.Helper.DatabaseHelper;
import com.example.connecttosoapapiapp.ReceivingModule.model.Users;
import com.example.connecttosoapapiapp.ScanBarcode.Helper.DatabaseHelperForScanBarcod;
import com.example.connecttosoapapiapp.ScanBarcode.Module.ScanBarcodeModule;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ScanBarcodActivity extends AppCompatActivity {
DatabaseHelperForScanBarcod databaseHelperForScanBarcod;
EditText ed_barcode,ed_qty,ed_zone,edit_csv_file;
Button btn_save,btn_export;
TextView txt_response;
    public final static String CHANNEL_ID ="1";
    File filePath;
    String CSVName="test" ,Store="sa", UserComp ="01" ,Usercode="";
    List<ScanBarcodeModule> scanBarcodeList;
    RadioButton radio_salah,radio_aome,radio_karagea,radio_gamla,radio_mortagahat,radio_kamat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcod);
        edit_csv_file=findViewById(R.id.edit_csv_file);
        ed_barcode=findViewById(R.id.ed_barcode);
        ed_qty=findViewById(R.id.ed_qty);
        ed_zone=findViewById(R.id.ed_zone);
        txt_response=findViewById(R.id.txt_response);
        UploadService.NAMESPACE = "com.example.connecttosoapapiapp";

        radio_salah=findViewById(R.id.radio_salah);
        radio_aome=findViewById(R.id.radio_aome);
        radio_karagea=findViewById(R.id.radio_karagea);
        radio_gamla=findViewById(R.id.radio_gamla);
        radio_mortagahat=findViewById(R.id.radio_mortagahat);
        radio_kamat=findViewById(R.id.radio_kamat);

        radio_salah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_salah.setChecked(true);
                Store="sa";
                radio_aome.setChecked(false);
                radio_karagea.setChecked(false);
                radio_gamla.setChecked(false);
                radio_mortagahat.setChecked(false);
                radio_kamat.setChecked(false);

            }
        });


        radio_aome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_aome.setChecked(true);
                Store="mw";
                radio_salah.setChecked(false);
                radio_karagea.setChecked(false);
                radio_gamla.setChecked(false);
                radio_mortagahat.setChecked(false);
                radio_kamat.setChecked(false);

            }
        });
        radio_karagea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_karagea.setChecked(true);
                Store="ow";
                radio_aome.setChecked(false);
                radio_salah.setChecked(false);
                radio_gamla.setChecked(false);
                radio_mortagahat.setChecked(false);
                radio_kamat.setChecked(false);

            }
        });
        radio_gamla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_gamla.setChecked(true);
                Store="ew";
                radio_aome.setChecked(false);
                radio_karagea.setChecked(false);
                radio_salah.setChecked(false);
                radio_mortagahat.setChecked(false);
                radio_kamat.setChecked(false);

            }
        });
        radio_mortagahat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_mortagahat.setChecked(true);
                Store="rt";
                radio_aome.setChecked(false);
                radio_karagea.setChecked(false);
                radio_gamla.setChecked(false);
                radio_salah.setChecked(false);
                radio_kamat.setChecked(false);

            }
        });
        radio_kamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_kamat.setChecked(true);
                Store="cn";
                radio_aome.setChecked(false);
                radio_karagea.setChecked(false);
                radio_gamla.setChecked(false);
                radio_mortagahat.setChecked(false);
                radio_salah.setChecked(false);

            }
        });

        databaseHelperForScanBarcod = new DatabaseHelperForScanBarcod(this);
        btn_export=findViewById(R.id.btn_export);
        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarcodeList=new ArrayList<>();
                scanBarcodeList = databaseHelperForScanBarcod.selectScanBarcodeModule();
                if (edit_csv_file.getText().toString().isEmpty()){
                    edit_csv_file.setError("من فضلك أدخل الأسم");
                    edit_csv_file.requestFocus();
                }
                else if (scanBarcodeList.size() >0) {
                    createCSV();
                    InsertToCSVFile();
                    RequestRunTimePermission();
                } else {
                    Toast.makeText(ScanBarcodActivity.this, "لا يوجد بيانات للرفع", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_barcode.getText().toString().isEmpty()
                        || ed_qty.getText().toString().isEmpty()
                || ed_zone.getText().toString().isEmpty() ){
                    if (ed_barcode.getText().toString().isEmpty()){
                        ed_barcode.setError("أدخل الباركود");
                    }else if (ed_qty.getText().toString().isEmpty()){
                            ed_qty.setError("أدخل الكمية");
                    }else if (ed_zone.getText().toString().isEmpty()){
                        ed_zone.setError("أدخل المنطقة");
                    }
                }else {
                    databaseHelperForScanBarcod.insert_ScanBarcode(ed_barcode.getText().toString(),
                            ed_qty.getText().toString(),ed_zone.getText().toString());
                    ed_barcode.setText("");
                    ed_barcode.requestFocus();
                //    ed_qty.setText("");
                //    ed_zone.setText("");
                }
            }
        });

        // TODO Auto-generated catch block
        DatabaseHelper databaseHelper=new DatabaseHelper(this);
        List<Users> userdataList=new ArrayList<>();
//
        userdataList = databaseHelper.getUserData();
        if (userdataList.size()>0) {
            UserComp = userdataList.get(0).getCompany1();
            UserComp = UserComp.substring(1, 3);
            Usercode = userdataList.get(0).getUser_Name1();
            Log.e("zzzz", "" + UserComp);
        }
    }


    public void createCSV(){
        File folder = new File("/data/user/0/com.example.connecttosoapapiapp/databases/");
        boolean var = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HHmmMM-a");
        if (!edit_csv_file.getText().toString().isEmpty()) {

            CSVName = Usercode +"_"+ sdf.format(new Date()) + "_" + UserComp + Store + "_" + edit_csv_file.getText().toString();
            Log.e("zzzz", "" + CSVName);
        }else {
            CSVName = sdf.format(new Date()) + "_" + UserComp + Store + "_" + edit_csv_file.getText().toString();
            Log.e("zzzz", "" + CSVName);
        }
        CSVName= CSVName.replaceAll(" ","");
        if (!folder.exists()) {
            var = folder.mkdir();
            Toast.makeText(this,"هذا الملف غير موجود",Toast.LENGTH_LONG).show();
        }
        filePath = new File(folder.toString(), CSVName+".CSV");

        try {
            new FileOutputStream(filePath);
            Log.d("UploadActivity11","getDatabasePath"+filePath.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void InsertToCSVFile() {
        try {
            FileWriter writer = new FileWriter(filePath);
//            "PSTNG_DATE","DOC_DATE""PO_NUMBER""PO_ITEM""NO_MORE_GR""SPEC_STOCK""MATERIAL"
//            "PLANT""STGE_LOC""ENTRY_QNT""ENTRY_UOM""EAN_UPC""SERNP"

            scanBarcodeList = databaseHelperForScanBarcod.selectScanBarcodeModule();
           /* writer.append("BarCode");
            writer.append(',');
            writer.append("QTY");
            writer.append(',');
            writer.append("Zone");
            writer.append('\n');*/

            if (scanBarcodeList.size() ==0){
                Log.e("envelope", "Po_Item_For_ftp_Upload.size() ==0");
            }else {
                Log.e("envelope", "Po_Item_For_ftp_Upload" + scanBarcodeList.size());

                for (int i = 0; i < scanBarcodeList.size(); i++) {

                    writer.append(scanBarcodeList.get(i).getBarCode1());
                    writer.append(',');
                    writer.append(scanBarcodeList.get(i).getQTY1());
                    writer.append(',');
                    writer.append(scanBarcodeList.get(i).getZone1());
                    writer.append('\n');
                }

            }
            //generate whatever data you want

            writer.flush();
            writer.close();

//            UplaodingToFtp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UplaodingToFtp() {
        String PdfID = UUID.randomUUID().toString();
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.e("VERSION.SDK_INT " ,"API 26 or higher");

                    // CharSequence name = getString(R.string.channel_name);
                    CharSequence name ="Chanel1";
                    Log.e("VERSION.SDK_INT " , String.valueOf(name));

                    //String description = getString(R.string.channel_description);
                    String description ="chanelforrooms";
                    Log.e("VERSION.SDK_INT " ,description);

                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    // NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    NotificationManager notificationManager = (NotificationManager) getSystemService
                            (Context.NOTIFICATION_SERVICE);

                    notificationManager.createNotificationChannel(channel);
                }else
                    Log.e("VERSION.SDK_INT " ,"Not API 26 or higher");

            }
            MultipartUploadRequest multipartUploadRequest=  new MultipartUploadRequest(this, PdfID, Constant.UploadCSVFile_ScanBarcode);
            multipartUploadRequest.addFileToUpload(filePath.getPath(), "pdf");

            Log.e("bbbbbb","nm,.,mcgvbnjmkl,"+filePath.getPath());
            // name & room_id & user_id & user_name &  type  this is key between android and php only

            multipartUploadRequest.addParameter("UserType", UserComp);
            multipartUploadRequest.addParameter("name", CSVName);

            // .addParameter("type", String.valueOf(3))
//              if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.e("bbbbbb","nm,.,mcgvbnjmkl,");
            multipartUploadRequest .setNotificationConfig(new UploadNotificationConfig());
//              }else {
//                  Toast.makeText(this,"تم الرفع",Toast.LENGTH_SHORT).show();
//                  Log.e("bbbbbb","nmelsemcgvbnjmkl,");
//              }
            multipartUploadRequest.setAutoDeleteFilesAfterSuccessfulUpload(true);

            multipartUploadRequest .setMaxRetries(2);
            multipartUploadRequest .startUpload();
//            txt_response.setText("تم الرفع بهذا الأسم\n"+CSVName);
            edit_csv_file.setText("");
            edit_csv_file.setHint("تم");
            btn_export.setVisibility(View.VISIBLE);
       //     progress_for_export.setVisibility(View.GONE);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_all_items))
                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            databaseHelperForScanBarcod.DeleteScanBarCodeTable( );
                        }
                    })
                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    }).show();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(ScanBarcodActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(ScanBarcodActivity.this,"أذن قراء من الكارت", Toast.LENGTH_LONG).show();
            UplaodingToFtp();
        } else {

            ActivityCompat.requestPermissions(ScanBarcodActivity.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ScanBarcodActivity.this,"تم أعطاء الأذن", Toast.LENGTH_LONG).show();
                    UplaodingToFtp();
                } else {

                    Toast.makeText(ScanBarcodActivity.this,"تم إلغاء الأذن", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }



}
