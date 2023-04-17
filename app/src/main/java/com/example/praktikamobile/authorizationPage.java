package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class authorizationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_page);
    }
    public void toNewActivity(View v)
    {
        Intent i = new Intent(getApplicationContext(),mainPage.class);
        startActivity(i);
    }
    public void toRegistrationPage (View v){
        Intent i = new Intent(getApplicationContext(), registrationPage.class);
        startActivity(i);
    }
    public void toResetPasswordPage (View v){
        startActivity(new Intent(this, resetPasswordPage.class));

    }

}
