package com.example.praktikamobile;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class startSettingsPage extends AppCompatActivity{
public static String Login;

    ImageButton userPhoto;
    private static final int REQUEST_SELECT_PHOTOS = 1;
    private static final int MAX_PHOTOS = 1;
    private List<String> selectedPhotos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_settings_page);

    }
    public void SaveSettings(View view){
            EditText tb_name = (EditText) findViewById(R.id.tb_name);
            String name = tb_name.getText().toString();

            EditText tb_surname = (EditText) findViewById(R.id.tb_surname);
            String surname = tb_surname.getText().toString();

            EditText tb_bday = (EditText) findViewById(R.id.tb_bday);
            String bday = tb_bday.getText().toString();

            if (!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(surname) &&!TextUtils.isEmpty(bday)){

                OkHttpClient client = new OkHttpClient();
                String url = "https://smtpservers.ru/projects/praktikaMobile/editSettings?name="+name+"&surname="+surname+"&bday="+bday+"&login="+Login;
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseBody = response.body().string();
                        if(responseBody.equals("500")){
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getApplicationContext(), "Неизвестная ошибка с индексом 500!!!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            settingPage.LoginSett = Login;
                            toMainPage();
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        String error = e.toString();
                    }
                });

            }
    }
    public void toMainPage(){
        Intent i = new Intent(getApplicationContext(), mainPage.class);
        startActivity(i);
    }
}