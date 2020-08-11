package com.example.connecttosoapapiapp.Promotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.connecttosoapapiapp.R;

public class MainPromotionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_promotion);
    }

    public void TodayPromotion(View view) {
        Intent GetToDayPromotion=new Intent(MainPromotionActivity.this,SearchForGetPromotionActivity.class);
        GetToDayPromotion.putExtra("TodayOrActive","Today");
        startActivity(GetToDayPromotion);
    }

    public void ActivePromotion(View view) {
        Intent GetToDayPromotion=new Intent(MainPromotionActivity.this,SearchForGetPromotionActivity.class);
        GetToDayPromotion.putExtra("TodayOrActive","Active");
        startActivity(GetToDayPromotion);
    }

    public void ExpiredPromotion(View view) {
        Intent GetToDayPromotion=new Intent(MainPromotionActivity.this,SearchForGetPromotionActivity.class);
        GetToDayPromotion.putExtra("TodayOrActive","Expired");
        startActivity(GetToDayPromotion);
    }

    public void StopedPromotion(View view) {
        Intent GetToDayPromotion=new Intent(MainPromotionActivity.this,SearchForGetPromotionActivity.class);
        GetToDayPromotion.putExtra("TodayOrActive","Stoped");
        startActivity(GetToDayPromotion);
    }
}
