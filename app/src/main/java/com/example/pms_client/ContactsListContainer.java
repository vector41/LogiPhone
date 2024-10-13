package com.example.pms_client;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContactsListContainer extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_container);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_contact);
        bottomNavigationView.findViewById(R.id.nav_contacts).setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
