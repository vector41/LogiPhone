package com.example.pms_client;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RecentsListContainer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.recents_container);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_recent);
        bottomNavigationView.findViewById(R.id.nav_history).setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
