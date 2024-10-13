package com.example.pms_client;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SearchContactsContainer extends AppCompatActivity {

    ListView contactsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_contacts_container);

        contactsContainer = (ListView) findViewById(R.id.container_contacts_list);
        contactsContainer.setDivider(null);

        SearchContactsItem[] searchContactsItems = new SearchContactsItem[] {
                new SearchContactsItem("田崎 幸治", 1, R.drawable.avatar_background),
                new SearchContactsItem("谷口 日野自動車", 2, R.drawable.green_button_background),
                new SearchContactsItem("谷口 日野自動車", 3, R.drawable.avatar_background),
                new SearchContactsItem("田崎 幸治", 4, R.drawable.green_button_background),
                new SearchContactsItem("谷口 日野自動車", 5, R.drawable.avatar_background),
                new SearchContactsItem("田崎 幸治", 6, R.drawable.avatar_background),
                new SearchContactsItem("谷口 日野自動車", 7, R.drawable.green_button_background),
                new SearchContactsItem("谷口 日野自動車", 8, R.drawable.avatar_background),
                new SearchContactsItem("田崎 幸治", 9, R.drawable.green_button_background),
                new SearchContactsItem("谷口 日野自動車", 10, R.drawable.avatar_background),
                new SearchContactsItem("田崎 幸治", 11, R.drawable.green_button_background),
                new SearchContactsItem("谷口 日野自動車", 12, R.drawable.avatar_background),
        };

        SearchContactAdapter adapter = new SearchContactAdapter(this, R.layout.search_contacts_item, searchContactsItems);
        contactsContainer.setAdapter(adapter);
    }
}
