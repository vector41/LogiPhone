package com.example.pms_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdd extends AppCompatActivity {
    boolean isFavoriteView = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_add);

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view_for_all_contact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up adapter with dummy data
        ContactAdapter adapter = new ContactAdapter(getDummyContacts(), isFavoriteView);
        recyclerView.setAdapter(adapter);

        // Set up the back button
        ImageView backButton = findViewById(R.id.favorite_back);
        //
        backButton.setOnClickListener(v -> finish());
    }

    // Dummy contacts data
    private List<Contact> getDummyContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("田崎 幸治", "0761-55-3487", "090-4275-0206",  R.drawable.ic_contact_default)); // Use your own drawable
        contacts.add(new Contact("谷口 日野自動車", "", "090-4275-0206",  R.drawable.ic_contact_default));
        contacts.add(new Contact("田治", "0761-55-3487", "090-4275-0206",  R.drawable.ic_contact_default)); // Use your own drawable
        contacts.add(new Contact("野自", "0761-55-3483", "090-4275-0213",  R.drawable.ic_contact_default));
        contacts.add(new Contact("動治", "0761-55-3487", "090-4275-0276",  R.drawable.ic_contact_default)); // Use your own drawable
        contacts.add(new Contact("野幸", "0761-55-3483", "090-4275-0253",  R.drawable.ic_contact_default));
        contacts.add(new Contact("動自", "0761-55-3417", "090-4275-0226",  R.drawable.ic_contact_default)); // Use your own drawable
        contacts.add(new Contact("自幸", "0761-55-3423", "090-4275-0243",  R.drawable.ic_contact_default));
        return contacts;
    }
}
