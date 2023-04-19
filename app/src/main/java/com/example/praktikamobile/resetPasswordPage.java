package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
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

public class resetPasswordPage extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_page);
    }
    public void toResetPasswordPage (View v){
        startActivity(new Intent(this, resetCodePage.class));

    }
    public void resetPassword (View v){
        EditText emailEditText = findViewById(R.id.tb_email);
        String email = emailEditText.getText().toString();
        if(isValidEmail(email)){
            SendMessage(email, v);


        }
        else{
            SendErrorNotification();
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
    int finalRandomNumber = 0;
    protected void SendMessage(String email, View v){
        OkHttpClient client = new OkHttpClient();
        Random random = new Random();
        int randomNumber = random.nextInt(8999) + 1000;
        finalRandomNumber = randomNumber;
        String url = "https://smtpservers.ru/projects/praktikaMobile/sendMessage?to="+email+"&code="+randomNumber+"&app=photochka&helpemail=help@smtpservers.ru";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                if(responseBody.equals("005")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Аккаунт с таким email не зарегистрирован!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db",MODE_PRIVATE,null);
                            db.execSQL("CREATE TABLE IF NOT EXISTS RecoveryPassword (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, code INTEGER)");
                            String exception = String.format("INSERT into RecoveryPassword(email, code) VALUES('%s', %s)", email, finalRandomNumber);
                            db.execSQL(exception);
                            db.close();
                            Toast.makeText(getApplicationContext(), "Код отправлен!", Toast.LENGTH_SHORT).show();
                            toResetPasswordPage(v);
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.toString();
            }
        });
    }
    private void SendErrorNotification() {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Неверно введен email адрес!",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
}
