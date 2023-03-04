package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class authorizationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_page);
    }
    public void toNewActivity(View v)
    {
        Intent i = new Intent(getApplicationContext(),settingPage.class);
        startActivity(i);

    }
}
