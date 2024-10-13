package com.example.pms_client;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.PopupMenu;
import android.view.View;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSSend extends AppCompatActivity {

    TextView textView;
    private static final int REQUEST_CALL_PERMISSION = 1;

    private static String mobile_number = "";
    private ActivityResultLauncher<Intent> filePickerLauncher;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_send);

        // Initialize ActivityResultLauncher
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            displaySelectedFile(uri);
                        }
                    }
                }
        );

        // Retrieve the contact details passed from the ContactAdapter
        String contactName = getIntent().getStringExtra("contact_name");
        mobile_number = getIntent().getStringExtra("mobile_phone");

        // Set the contact details to views if needed
        // For example, set the contact name on a TextView in sms_send.xml
        TextView titleTextView = findViewById(R.id.selected_username);
        titleTextView.setText(contactName);

        //bottom_nav_menu
        ImageView bottomNavMenu = findViewById(R.id.bottom_nav_menu);
        bottomNavMenu.setOnClickListener( v-> showPopupMenu(v));

        // Call
        ImageView callButton = findViewById(R.id.selected_user_call);
        callButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                // Permission already granted, make the call
                makePhoneCall(mobile_number);
            }
        });

        // Set up the back button
        ImageView backButton = findViewById(R.id.sms_send_back);
        backButton.setOnClickListener(v -> finish());

        // Upload function
        ImageView attachFile = findViewById(R.id.attach_file);
        ImageView attachImage = findViewById(R.id.attach_image);

        attachFile.setOnClickListener(v -> openFileChooser("file/*"));
        attachImage.setOnClickListener(v -> openFileChooser("image/*"));

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.sms_popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here
                int itemId = item.getItemId();
                if (itemId == R.id.menu_search) {
                    Toast.makeText(SMSSend.this, "Search clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.menu_show_input) {
                    Toast.makeText(SMSSend.this, "Show input clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popupMenu.show();
    }


    private void makePhoneCall(String mobile_number) {
        Log.d("MakePhoneCall", "makePhoneCall method called with phoneNumber: " + mobile_number);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mobile_number));
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the call
                makePhoneCall(mobile_number);
            } else {
                // Permission denied
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openFileChooser(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        filePickerLauncher.launch(intent);
    }

    private void displaySelectedFile(Uri uri) {
        // For example, if you are loading an image, use an ImageView:
        ImageView imageView = findViewById(R.id.attach_image);
        imageView.setImageURI(uri);

        // For other files, you might want to show the file name or path:
        String fileName = getFileNameFromUri(uri);
        if (textView != null) {
            textView.setText(fileName);
        }
    }

    public String getFileNameFromUri(Uri uri) {
        String displayName = null;

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (nameIndex != -1) {
                            displayName = cursor.getString(nameIndex);
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        }

        return displayName;
    }
}


