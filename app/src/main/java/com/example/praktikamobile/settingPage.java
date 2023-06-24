package com.example.praktikamobile;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class settingPage extends AppCompatActivity{
    ImageView userPhoto;
    private static final int REQUEST_SELECT_PHOTOS = 1;
    private static final int MAX_PHOTOS = 1;
    private List<String> selectedPhotos = new ArrayList<>();
    public static String LoginSett;
    private String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
 try {

     SQLiteDatabase db = getBaseContext().openOrCreateDatabase("users.db", MODE_PRIVATE, null);
     Cursor query = db.rawQuery("SELECT email FROM (SELECT * FROM users ORDER BY id DESC LIMIT 1) t ORDER BY id;", null);
     while(query.moveToNext()) {
         email  = query.getString(0);
     }


     userPhoto = (ImageView) findViewById(R.id.tb_photo_user);

     EditText tb_name = (EditText) findViewById(R.id.tb_nameE);
     EditText tb_surname = (EditText) findViewById(R.id.tb_surnameE);
     EditText tb_bday = (EditText) findViewById(R.id.tb_bdayY);

     OkHttpClient client = new OkHttpClient();
     String url = "https://smtpservers.ru/projects/praktikaMobile/selectUserInfo?login="+LoginSett+"&email="+email;
     Request request = new Request.Builder()
             .url(url)
             .build();
     Call call = client.newCall(request);
     call.enqueue(new Callback() {
         @Override
         public void onResponse(Call call, Response response) throws IOException {
             String json = response.body().string();
             if (json.equals("500")) {
                 Handler handler = new Handler(Looper.getMainLooper());
                 handler.post(new Runnable() {
                     @Override
                     public void run() {
                         Toast.makeText(getApplicationContext(), "Ошибкеам!", Toast.LENGTH_SHORT).show();
                     }
                 });
             } else {
                 Gson gson = new Gson();

                 User[] users = gson.fromJson(json, User[].class);
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() { tb_name.setText(users[0].firstName);
                         tb_surname.setText(users[0].lastName);
                         tb_bday.setText(users[0].bday);
                         String photoUri = "https://smtpservers.ru/projects/praktikaMobile/uploads/"+users[0].mainPhoto;
                         Picasso.get().load(photoUri).into(userPhoto);
                     }

                 });


             }
         }
         @Override
         public void onFailure(Call call, IOException e) {
             String error = e.toString();
         }
     });


 }catch(Exception e){

 }


    }
    @Override
    public void onBackPressed() {

    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    public void exitFromApp(View v){
        try{
            this.deleteDatabase("users.db");
            finish();
            startActivity(new Intent(this, authorizationPage.class));
            finish();
        }
        catch (Exception e){
        }
    }
    public void toMainPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),mainPage.class);
        startActivity(i);
    }
    public void saveNewSettings(View v){
        EditText tb_name = (EditText) findViewById(R.id.tb_nameE);
        EditText tb_surname = (EditText) findViewById(R.id.tb_surnameE);
        EditText tb_bday = (EditText) findViewById(R.id.tb_bdayY);
        String name = tb_name.getText().toString();
        String surname = tb_surname.getText().toString();
        String bday = tb_bday.getText().toString();
        if (!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(surname) &&!TextUtils.isEmpty(bday)){

            OkHttpClient client = new OkHttpClient();
            String url = "https://smtpservers.ru/projects/praktikaMobile/editSettings?name="+name+"&surname="+surname+"&bday="+bday+"&email="+email+"&login="+LoginSett;
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

                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    String error = e.toString();
                }
            });

        }




    }
    public void toFavspage(View v)
    {
        Intent i = new Intent(getApplicationContext(),favoritePage.class);
        startActivity(i);
    }
    public void uploadImage(View v){
        if(selectedPhotos.size() < 1) {
            // Запуск выбора фотографий из галереи
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent, "Выберите фотографии"), REQUEST_SELECT_PHOTOS);
        }else{
            Toast.makeText(this, "Выбрано максимальное количество фотографий", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_PHOTOS && resultCode == RESULT_OK) {
            if (data != null) {
                // Получение выбранных фотографий
                if (data.getClipData() != null) {
                    int count = Math.min(data.getClipData().getItemCount(), MAX_PHOTOS);
                    if(selectedPhotos.size() + count > 1){
                        Toast.makeText(this, "Выбрано максимальное количество фотографий", Toast.LENGTH_SHORT).show();
                    }else{
                        for (int i = 0; i < count; i++) {
                            selectedPhotos.add(data.getClipData().getItemAt(i).getUri().toString());
                        }
                    }
                } else if (data.getData() != null) {
                    if(selectedPhotos.size() + 1 > 1){
                        Toast.makeText(this, "Выбрано максимальное количество фотографий", Toast.LENGTH_SHORT).show();
                    }else{
                        selectedPhotos.add(data.getData().toString());
                    }
                }

                int maxImages = Math.min(selectedPhotos.size(), 1);
                // Загрузка и отображение выбранных фотографий в каждом ImageView
                for (int i = 0; i < maxImages; i++) {
                    String photoUri = selectedPhotos.get(i);
                    Picasso.get().load(photoUri).into(userPhoto);
                }
                    // Загрузка фотографии в ImageView с помощью Picasso

//                    if (imageView != null) {
                       // Picasso.get().load(photoUri).into(imageView);
//                    }
                }
                // Вывод информации о выбранных фотографиях
              //  Toast.makeText(this, "Выбрано " + selectedPhotos.size() + " фотографий", Toast.LENGTH_SHORT).show();
            }
        }

    public class User {
        private String firstName;
        private String lastName;
        private String bday;
        private String mainPhoto;
    }
}

