package com.example.praktikamobile;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class settingPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
    }
    public void exitFromApp(View v){
        try{
            this.deleteDatabase("users.db");
            startActivity(new Intent(this, authorizationPage.class));
        }
        catch (Exception e){
        }

    }
    public void toMainPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),mainPage.class);
        startActivity(i);
    }
    public void toFavspage(View v)
    {
        Intent i = new Intent(getApplicationContext(),favoritePage.class);
        startActivity(i);
    }
}