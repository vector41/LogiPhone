package com.example.pms_client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListContainer extends AppCompatActivity {

    private FavoriteListHelper listHelper;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_container);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS
            );
        }
        getContacts();

        JSONObject paramObj = new JSONObject();
        List<String> numbers = new ArrayList<String>();
        try {
            Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            );
            if(cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    numbers.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                }
            }
            paramObj.put("event", "VERIFY_PHONE_LIST");
            paramObj.put("data", numbers);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        ListView listView = findViewById(R.id.container_favorite_list);
        WebSocketManager socketManager = new WebSocketManager(this, listView);
        socketManager.getGlobalWebSocket().send(paramObj.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
//
//    public void navigateRecents(View view) {
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(CallLog.Calls.CONTENT_URI);
//        startActivity(intent);
//    }
//
//    public void navigateContacts(View view) {
//
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(ContactsContract.Contacts.CONTENT_URI);
//        startActivity(intent);
//    }


    @SuppressLint("Range")
    private void getContacts() {

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if(cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneticName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHONETIC_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String starred = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
                String icon = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                Log.d("Contact Detail", id + ", " + lookupKey + ", " + displayName + ", " + phoneticName + ", " + number + ", " + starred + ", " + icon);
            }
        }
    }

}
