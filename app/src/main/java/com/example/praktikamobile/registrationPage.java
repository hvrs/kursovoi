package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class registrationPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

    }
    public void toStartSettingsPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),startSettingsPage.class);
        startActivity(i);
    }
    public void toAuthorizationPage(View v){
        Intent i = new Intent(getApplicationContext(), authorizationPage.class);
        startActivity(i);
    }
    public void registerAcc(View v) {
        EditText emailEditText = (EditText) findViewById(R.id.edit_text_email);
        String email = emailEditText.getText().toString();

        EditText nameEditText = (EditText) findViewById(R.id.edit_text_login);
        String login = nameEditText.getText().toString();

        EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
        String password = passwordEditText.getText().toString();

        if (isValidEmail(email)) {

            OkHttpClient client = new OkHttpClient();
            String url = "https://smtpservers.ru/projects/praktikaMobile/registerUsers?password=" + password + "&login=" + login + "&email=" + email;
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    if(responseBody.equals("004")){
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(), "Аккаунт с таким email-адрессом уже зарегистрирован!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        toStartSettingsPage(v);
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    String error = e.toString();
                }
            });

        }
    }


    public static boolean isValidEmail(String email) {
        // Проверяем на null
        if (email == null) {
            return false;
        }
        // Задаем паттерн регулярного выражения для email
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

        // Сравниваем строку с паттерном
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}