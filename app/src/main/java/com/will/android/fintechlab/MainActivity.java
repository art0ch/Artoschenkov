package com.will.android.fintechlab;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private BaseFragment baseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        baseFragment = new BaseFragment(this, "https://developerslife.ru/random?json=true");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, baseFragment)
                .commit();

    }


}