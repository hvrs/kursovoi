package com.example.praktikamobile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class mainPage extends AppCompatActivity {
    private String email = null;

    private GridView mGridView;
    private CustomGridAdapter mAdapter;
    private Boolean Upload = false;
    ArrayList<String> idList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        try {
            SQLiteDatabase db = getBaseContext().openOrCreateDatabase("users.db", MODE_PRIVATE, null);
            Cursor query = db.rawQuery("SELECT email FROM (SELECT * FROM users ORDER BY id DESC LIMIT 1) t ORDER BY id;", null);
            while(query.moveToNext()) {
                email  = query.getString(0);}

            GridView gridView = findViewById(R.id.gridView);
            gridView.setAdapter(null);
            idList.clear();
            OkHttpClient client = new OkHttpClient();
            String url = "https://smtpservers.ru/projects/praktikaMobile/selectUserPhotos?email="+email;//+isFav
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = client.newCall(request);

            mGridView = findViewById(R.id.gridView);

            Context context = this;
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    if (json.equals("500")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Gson gson = new Gson();
                        Photos[] adv = gson.fromJson(json, Photos[].class);
                        String[][] array = new String[adv.length][1];
                        for(int i = 0; i < adv.length; i++){
                            idList.add(String.valueOf(adv[i].Id));
                            array[i][0] = adv[i].photoPath;

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new CustomGridAdapter(context, array);
                                mGridView.setAdapter(mAdapter);
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


    public void toFavspage(View v)
    {
        Intent i = new Intent(getApplicationContext(),favoritePage.class);
        startActivity(i);
    }
    public void toSettingsPage(View v)
    {
        Intent i = new Intent(getApplicationContext(),settingPage.class);
        startActivity(i);
    }

    public class Photos{
        private int Id;
        private String photoPath;
        private String date;
        private String isFav;

    }
}
