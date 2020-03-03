package com.example.debtapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.debtapp.Contracts.DebtFactory;

public class MainActivity extends AppCompatActivity {
    final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
