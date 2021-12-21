package com.example.connecttosoapapiapp.CycleCount;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.CycleCount.Helper.DatabaseHelperForCycleCount;
import com.example.connecttosoapapiapp.MainActivity;
import com.example.connecttosoapapiapp.R;
import com.example.connecttosoapapiapp.ReceivingModule.Classes.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class CycleCountActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    private RadioButton radio_OpenlastCycleCount, radio_NewCycleCount;
    private EditText edit_from,edit_to;
    private TextView txt_describtion,txt_date,txt_items,txt_user,txt_Status;
    private Button btn_get_info,btn_Go_to_Scan;
    private String EnvelopeBodyInConstant,EnvelopeBodyInCurrent , RETURN, MESSAGE
            ,Description ,Date,Items,User,Status;
    private int LOADER_ID = 1;
    private DatabaseHelperForCycleCount databaseHelperForCycleCount;
    private ProgressBar progress_get_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_count);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progress_get_info=findViewById(R.id.progress_get_info);

        radio_OpenlastCycleCount =findViewById(R.id.radio_openlastCycleCount);
        radio_NewCycleCount=findViewById(R.id.radio_NewCycleCount);

        btn_Go_to_Scan=findViewById(R.id.btn_Go_to_Scan);

        databaseHelperForCycleCount=new DatabaseHelperForCycleCount(this);

        radio_OpenlastCycleCount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                List<String> PHYSINVENTORY  = databaseHelperForCycleCount.Get_All_PHYSINVENTORY();
                if (PHYSINVENTORY.size()>0){
                    if (String.valueOf(PHYSINVENTORY.get(0).charAt(0)).equalsIgnoreCase("0")){
                        edit_from.setText(PHYSINVENTORY.get(0).substring(1));
                    }else {
                        edit_from.setText(PHYSINVENTORY.get(0));
                    }

                    if (String.valueOf(PHYSINVENTORY.get(PHYSINVENTORY.size()-1).charAt(0)).equalsIgnoreCase("0")){
                        edit_to.setText(PHYSINVENTORY.get(PHYSINVENTORY.size()-1).substring(1));
                    }else {
                        edit_to.setText(PHYSINVENTORY.get(PHYSINVENTORY.size()-1));
                    }
                    btn_Go_to_Scan.setEnabled(true);

                }else {
                    Toast.makeText(CycleCountActivity.this, "لا يوجد بيانات مسجله", Toast.LENGTH_SHORT).show();
                }
                edit_from.setEnabled(false);
                edit_to.setEnabled(false);
            }
        });

        radio_NewCycleCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    edit_from.setText("");
                    edit_to.setText("");
                edit_from.setEnabled(true);
                edit_to.setEnabled(true);

            }
        });

        edit_from=findViewById(R.id.edit_from);
        edit_to=findViewById(R.id.edit_to);
        txt_describtion=findViewById(R.id.txt_describtion);
        txt_date=findViewById(R.id.txt_date);
        txt_items=findViewById(R.id.txt_items);
        txt_user=findViewById(R.id.txt_user);
        txt_Status=findViewById(R.id.txt_Status);
        btn_get_info=findViewById(R.id.btn_get_info);
        edit_from.setError(null);

        btn_get_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_from.setError(null);
                if (edit_from.getText().toString().isEmpty()){
                    edit_from.setError("أدخل الرقم ");
                }else {
                    progress_get_info.setVisibility(View.VISIBLE);
                    btn_get_info.setVisibility(View.GONE);

                    txt_describtion.setText("Describtion");
                        txt_date.setText("Date");
                        txt_items.setText("Items");
                        txt_user.setText("User");
                        txt_Status.setText("Status");

                    getLoaderManager().initLoader(LOADER_ID, null, CycleCountActivity.this);
                }
            }
        });

    }



    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        @SuppressLint("StaticFieldLeak") AsyncTaskLoader asyncTaskLoader =  new AsyncTaskLoader(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }


            @Override
            public Object loadInBackground() {
                SoapObject request =new SoapObject(Constant.NAMESPACE_For_Check_Cycle_Count , Constant.METHOD_For_Check_Cycle_Count);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                String year = sdf.format(new Date());//+txt_po_number.getText().toString();

                request.addProperty("FISCALYEAR", year);
                request.addProperty("PHYSINVENTORY_FROM", edit_from.getText().toString());
                if (edit_to.getText().toString().isEmpty()){
                    request.addProperty("PHYSINVENTORY_TO", "?");
                }else {
                    request.addProperty("PHYSINVENTORY_TO", edit_to.getText().toString());
                }


                RETURN="Empty";
                MESSAGE="Empty";
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransport=new HttpTransportSE(Constant.URL_For_Check_Cycle_Count,6000000);
                try{
                    httpTransport.call(Constant.SOAP_ACTION_For_Check_Cycle_Count, envelope);
                }catch (Exception e){
                    e.printStackTrace();
                }

                EnvelopeBodyInConstant ="Code: env:Receiver, Reason: Web service processing error; more details in the web service error log on provider side";
                EnvelopeBodyInCurrent = String.valueOf(envelope.bodyIn);
                if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
                    // edit_purchaseorder.setError("Your PURCHASE ORDER Is Wrong");
                }else if (envelope.bodyIn.toString().isEmpty()){

                } else {

                    SoapObject soapObject_All_response = (SoapObject) envelope.bodyIn;
                    Log.e("no_of_item", String.valueOf(soapObject_All_response));

                    Log.e("no_of_item", String.valueOf(soapObject_All_response.getPropertyCount()));


                    SoapObject soapObject_Items_Return = (SoapObject) soapObject_All_response.getProperty(0);
                    SoapObject soapObject_only_Return = (SoapObject) soapObject_All_response.getProperty(1);

                    if (soapObject_Items_Return.getPropertyCount() >0 ){
                        SoapObject soapObject_Items_of_Items = (SoapObject) soapObject_Items_Return.getProperty(0);

                        Description = soapObject_Items_of_Items.getProperty(6).toString();
                         Date = soapObject_Items_of_Items.getProperty(4).toString();
                        Items = soapObject_Items_of_Items.getProperty(14).toString();
                        User = soapObject_Items_of_Items.getProperty(5).toString();
                       Status = soapObject_Items_of_Items.getProperty(3).toString();

                    }else {

                        SoapObject soapObject_Items_of_Return = (SoapObject) soapObject_only_Return.getProperty(0);
                        MESSAGE = (String) soapObject_Items_of_Return.getProperty("MESSAGE").toString();
                    }
                }
                return null;
            }
        };
        return asyncTaskLoader;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        //Toast.makeText(ReceivingActivity.this,"finished ",Toast.LENGTH_LONG).show();
        getLoaderManager().destroyLoader(LOADER_ID);
        Log.e("soaMESSAGE4",""+MESSAGE);
        //Toast.makeText(ReceivingActivity.this,"finished "+MESSAGE,Toast.LENGTH_LONG).show();

        if (EnvelopeBodyInCurrent.contains(EnvelopeBodyInConstant)){
            edit_from.setError("من فضلك انظر لل Log");
            txt_describtion.setText(null);
            txt_date.setText(null);
            txt_items.setText(null);
            txt_user.setText(null);
            txt_Status.setText(null);
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
            edit_from.requestFocus();
        }


        else if(!MESSAGE.contains("Empty") ){
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);

            edit_from.setError(MESSAGE);
            edit_from.requestFocus();
            txt_describtion.setText(null);
            txt_date.setText(null);
            txt_items.setText(null);
            txt_user.setText(null);
            txt_Status.setText(null);


        }else {
            if (Description.equalsIgnoreCase("anytype{}")){
                txt_describtion.setText("Description");
            }else {
                txt_describtion.setText(Description);
            }

            txt_date.setText(Date);
            txt_items.setText(Items);
            txt_user.setText(User);
            if (Status.equalsIgnoreCase("anytype{}")){
                txt_Status.setText("NO");
            }else {
                txt_Status.setText(Status);
            }
            progress_get_info.setVisibility(View.GONE);
            btn_get_info.setVisibility(View.VISIBLE);
            btn_Go_to_Scan.setEnabled(true);

        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    public void GoToScan(View view) {
             if (radio_OpenlastCycleCount.isChecked()){

                long NumItems = databaseHelperForCycleCount.Num_of_items_localDataBase();
                if (NumItems >0) {
                        if (!txt_Status.getText().toString().equalsIgnoreCase("Status")) {
                            if (txt_Status.getText().toString().equalsIgnoreCase("anytype{}") ||
                                    txt_Status.getText().toString().equalsIgnoreCase("NO")
                            ) {
                                Intent GoToScan = new Intent(CycleCountActivity.this, ScanActivity.class);
                                startActivity(GoToScan);
                            }else {
                                Toast.makeText(this, "تم تسليم دورة الجرد", Toast.LENGTH_SHORT).show();
                            }
                    }else {
                        Toast.makeText(this, "من فضلك أضغط أستعلام أولا", Toast.LENGTH_SHORT).show();
                        }
                }else {
                    Toast.makeText(this, "لايوجد دوره مسجله", Toast.LENGTH_SHORT).show();
                }
            }else  if (radio_NewCycleCount.isChecked()){
                if (edit_from.getText().toString().isEmpty()){
                    edit_from.setError("أدخل الرقم ");
                    }else {
                    if (!txt_Status.getText().toString().equalsIgnoreCase("Status")) {
                        if (txt_Status.getText().toString().equalsIgnoreCase("anytype{}") ||
                                txt_Status.getText().toString().equalsIgnoreCase("NO")
                        ) {
                            databaseHelperForCycleCount.DeleteItemsTable();
                            Intent GoToScan = new Intent(CycleCountActivity.this, ScanActivity.class);
                            GoToScan.putExtra("DocumentNuberfrom", edit_from.getText().toString());
                            if (!edit_to.getText().toString().isEmpty()) {
                                GoToScan.putExtra("DocumentNuberto", edit_to.getText().toString());
                            } else {
                                GoToScan.putExtra("DocumentNuberto", "");
                            }
                            startActivity(GoToScan);
                        } else {
                            Toast.makeText(this, "تم تسليم دورة الجرد", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "من فضلك أضغط أستعلام أولا", Toast.LENGTH_SHORT).show();
                    }
                }

            }else {
                 Toast.makeText(this, "من فضلك قم بأختيار", Toast.LENGTH_SHORT).show();
             }

    }

    @Override
    public void onBackPressed() {
        Intent Go_Back= new Intent(CycleCountActivity.this, MainActivity.class);
        Go_Back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Go_Back);
        super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
