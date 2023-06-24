package com.example.praktikamobile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class splashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            OkHttpClient client = new OkHttpClient();
            String url = "https://smtpservers.ru/projects/praktikaMobile/status";
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();


                    if (!responseBody.equals("200")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                startTechnicalWork();
                            }
                        });
                    } else {
                        try {
                            String email = null;
                            String FirstName = null;
                            String SurName = null;
                            String rememberme = null;
                            String Password = null;

                            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("users.db", MODE_PRIVATE, null);
                            Cursor query = db.rawQuery("SELECT id, email, firstName, surName, Password, remberMe FROM (SELECT * FROM users ORDER BY id DESC LIMIT 1) t ORDER BY id;", null);
                            while (query.moveToNext()) {
                                email = query.getString(1);
                                FirstName = query.getString(2);
                                SurName = query.getString(3);
                                Password = query.getString(4);
                                rememberme = query.getString(5);
                            }
                            if (rememberme.equals("1")) {
                                startProfile();
                            } else {
                                startAuth();
                            }
                        } catch (Exception e) {
                            startAuth();
                        }
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    String error = e.toString();
                    startTechnicalWork();
                }
            });
        }
        catch (Exception exception){
            startTechnicalWork();
        }
    }
    public void startTechnicalWork(){
        startActivity(new Intent(this, technikalWorks.class));

    }
    public void startProfile(){
        startActivity(new Intent(this, mainPage.class));

    }
    public void startAuth(){
        startActivity(new Intent(this, authorizationPage.class));

    }

}
