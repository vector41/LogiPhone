package com.example.pms_client;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.content.Intent;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private RadioButton radioSelectApp;
    private RadioButton radioSelectPhone;
    private TextView txtAppDescription;
    private Button positiveButton;
    private int REQUEST_CODE = 100;

    @SuppressLint("Range")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.RECEIVE_MMS) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[] {
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_CALL_LOG,
                                Manifest.permission.READ_CONTACTS,
                                Manifest.permission.CALL_PHONE,
                                Manifest.permission.RECEIVE_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.READ_PHONE_NUMBERS,
                        },
                        REQUEST_CODE
                );
            }
        }

        findViewById(R.id.main_start_button).setOnClickListener(v -> {

//            ProgressDialog dialog = new ProgressDialog(this);
//            dialog.setMessage("サーバーに接続中です。しばらくお待ちください。");
//            dialog.show();

            showSetDefaultDialog();

//            Cursor cursor = getContentResolver().query(
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    null,
//                    null,
//                    null,
//                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
//            );
//
//            if(cursor != null && cursor.getCount() > 0) {
//
//                while (cursor.moveToNext()) {
//
//                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                    String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
//                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                    String phoneticName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHONETIC_NAME));
//                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                    String starred = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED));
//                    String icon = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//                    Log.d("Contact Detail", id + ", " + lookupKey + ", " + displayName + ", " + phoneticName + ", " + number + ", " + starred + ", " + icon);
//                }
//            }
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(CallLog.Calls.CONTENT_URI);
//            startActivity(intent);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 100);
//            } else {
//                //Open call function
//                String phone = "7769942159";
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:+91" + phone));
//                startActivity(intent);
//            }
//            String phone = "7769942159";
//            Intent intent = new Intent(Intent.ACTION_CALL);
//            intent.setData(Uri.parse("tel:+91" + phone));
//            startActivity(intent);

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Uri callLogUri = Uri.withAppendedPath(CallLog.Calls.CONTENT_FILTER_URI, Uri.encode("+8508853398"));
//            intent.setData(callLogUri);
//            startActivity(intent);

//            Uri contactUri = ContactsContract.Contacts.getLookupUri(16, "0r8-914927301E");
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(contactUri);
//
//            startActivity(intent);

//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("sms:" + "+8508853398"));
//            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    startOverlayService();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showSetDefaultDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_set_default, null);

        radioSelectApp = dialogView.findViewById(R.id.radio_select_app);
        radioSelectPhone = dialogView.findViewById(R.id.radio_select_phone);
        txtAppDescription = dialogView.findViewById(R.id.content_select_app);

        txtAppDescription.setVisibility(View.GONE);
        radioSelectPhone.setChecked(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(dialogView);

        builder.setPositiveButton(R.string.confirm_set_default, null);
        builder.setNegativeButton(R.string.cancel_set_default, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        positiveButton.setEnabled(false);
        positiveButton.setTextColor(Color.GRAY);

        radioSelectApp.setOnClickListener(v -> {

            radioSelectApp.setChecked(true);
            radioSelectPhone.setChecked(false);
            positiveButton.setEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                positiveButton.setTextColor(getColor(R.color.primary));
            }
            txtAppDescription.setVisibility(View.VISIBLE);
        });

        radioSelectPhone.setOnClickListener(v -> {

            radioSelectApp.setChecked(false);
            radioSelectPhone.setChecked(true);
            positiveButton.setEnabled(false);
            positiveButton.setTextColor(Color.GRAY);

            txtAppDescription.setVisibility(View.GONE);
        });

        positiveButton.setOnClickListener(v -> {
            if (radioSelectApp.isChecked()) {

                if(checkDrawOverlayPermission()) {
                    startOverlayService();
                }
                if(!checkAccessibilityServicePermission()) {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                }
            }
            dialog.dismiss();
        });
    }

    private boolean checkAccessibilityServicePermission() {
        return isAccessibilityServiceEnabled(getBaseContext(), PhoneAppAccessibilityService.class);
    }

    private boolean checkDrawOverlayPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    private void startOverlayService() {

        Intent intent = new Intent(this, OverlayService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {

        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

        for (AccessibilityServiceInfo enabledService : enabledServices) {
            ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
            if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
                return true;
        }
        return false;
    }

}
