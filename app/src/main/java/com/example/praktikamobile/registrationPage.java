package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class registrationPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

    }
    public void toNewActivity(View v)
    {
        Intent i = new Intent(getApplicationContext(),photoInfoPage.class);
        startActivity(i);
    }
    public void toAuthorizationPage(View v){
        Intent i = new Intent(getApplicationContext(), authorizationPage.class);
        startActivity(i);
    }


}