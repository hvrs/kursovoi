package com.example.praktikamobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

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

        try{
            String email = null;
            String FirstName = null;
            String lastName = null;
            String rememberme = null;

            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("users.db", MODE_PRIVATE, null);
            Cursor query = db.rawQuery("SELECT id, email, firstName, lastName, remberMe FROM (SELECT * FROM users ORDER BY id DESC LIMIT 1) t ORDER BY id;", null);
            while(query.moveToNext()) {
                email  = query.getString(1);
                FirstName = query.getString(2);
                lastName = query.getString(3);
                rememberme = query.getString(4);
            }
            if(rememberme.equals("1")){
                toProfile();
            }
            else{
                this.deleteDatabase("users.db");
            }
        }
        catch (Exception e){

        }

    }
    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public void toRegistrationPage (View v){
        Intent i = new Intent(getApplicationContext(), registrationPage.class);
        startActivity(i);
    }
    public void toResetPasswordPage (View v){
        startActivity(new Intent(this, resetPasswordPage.class));

    }
    public void authorization (View v){
        EditText nameEditText = (EditText) findViewById(R.id.tb_login);
        String login = nameEditText.getText().toString();

        EditText passwordEditText = (EditText) findViewById(R.id.tb_password);
        String password = passwordEditText.getText().toString();

        OkHttpClient client = new OkHttpClient();
        String url = "https://smtpservers.ru/projects/praktikaMobile/authUsers.php?login="+login+"&password="+password;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                if (json.equals("004")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Gson gson = new Gson();
                    User[] users = gson.fromJson(json, User[].class);



                    SQLiteDatabase db = getBaseContext().openOrCreateDatabase("users.db",MODE_PRIVATE,null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER, email TEXT, firstName TEXT, lastName TEXT, remberMe BOOL, mainPhoto TEXT)");
                    String exception = String.format("INSERT INTO users (id,email,firstName,lastName,remberMe, mainPhoto) VALUES (%s, '%s', '%s', '%s', %s, ' ')", users[0].id, users[0].email, users[0].firstName, users[0].lastName, true);
                    db.execSQL(exception);
                    db.close();
                    toProfile();

                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                String error = e.toString();
            }
        });

    }
    public void toProfile (){
        startActivity(new Intent(this, mainPage.class));

    }

    public class User {
        private int id;
        private String firstName;
        private String lastName;
        private String bday;
        private String mainPhoto;
        private  String login;
        private  String email;
    }
}
