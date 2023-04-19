package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class resetPassword_newp extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_newp);
    }
    public void saveNewPassword(View v){
        EditText passwordEditText = (EditText) findViewById(R.id.tb_passwNew);
        String password = passwordEditText.getText().toString();

        String email = null;

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT email FROM (SELECT * FROM RecoveryPassword ORDER BY id DESC LIMIT 1) t ORDER BY email;", null);
        while(query.moveToNext()) {
            email = query.getString(0);
        }

        OkHttpClient client = new OkHttpClient();
        String url = "https://smtpservers.ru/projects/praktikaMobile/resetPassword?email="+email+"&password="+password;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SuccessUpdatePassword(v);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.toString();
            }
        });
    }
    public void SuccessUpdatePassword(View view){
        Toast toast = Toast.makeText(getApplicationContext(),
                "Пароль успешно обновлён!",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();

        this.deleteDatabase("app.db");
        toAuthorization(view);


    }
    public void toAuthorization(View view){
        startActivity(new Intent(this, authorizationPage.class));
    }
}
