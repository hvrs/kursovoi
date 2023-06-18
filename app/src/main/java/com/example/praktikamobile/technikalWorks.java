package com.example.praktikamobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class technikalWorks extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.technikal_work);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        moveTaskToBack(true);
    }
}
