package com.example.pms_client;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhoneAppAccessibilityService extends AccessibilityService {

    private WindowManager windowManager = null;
    private View overlayView = null;
    private View incomingView = null;
    private FavoriteListHelper favoriteListHelper;
    private int type = 0;
    private WindowManager.LayoutParams params;
    private AccessibilityEvent event = null;
    private static PhoneAppAccessibilityService instance;

    public static PhoneAppAccessibilityService getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        this.event = event;
        String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";
        String className = event.getClassName() != null ? event.getClassName().toString() : "";
        String content = !event.getText().isEmpty() && event.getText().get(0) != null ? event.getText().get(0).toString() : "";
        int eventType = event.getEventType();
        Log.d("AccessibilityEvent", event.toString());

        if (packageName.equals("com.google.android.dialer")) {
            if (content.contentEquals(getText(R.string.title_favorite))) {
                showOverlayFavoriteContainer();
            } else {
                hideOverlayFavoriteContainer();
            }
        }
        if ((eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && packageName.equals("com.google.android.apps.messaging")) || (eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED && packageName.equals("com.google.android.apps.messaging"))) {
            hideOverlayFavoriteContainer();
        }
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && packageName.equals("com.google.android.contacts")) {
            hideOverlayFavoriteContainer();
        }
        if (eventType == AccessibilityEvent.TYPE_ANNOUNCEMENT && packageName.equals("com.google.android.apps.nexuslauncher")) {
            hideOverlayFavoriteContainer();
        }
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && packageName.equals("com.google.android.apps.nexuslauncher")) {
            hideOverlayFavoriteContainer();
        }
        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && packageName.equals("com.android.systemui")) {
            hideOverlayFavoriteContainer();
        }
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED && packageName.equals("com.google.android.dialer") && className.equals("android.widget.Button")) {
            hideOverlayIncomingDetail();
        }
    }

    @Override
    public void onInterrupt() {
        Log.e("AccessibilityEvent", "Interrupted");
    }

    @Override
    protected void onServiceConnected() {

        super.onServiceConnected();

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED | AccessibilityEvent.TYPE_VIEW_FOCUSED | AccessibilityEvent.TYPE_ANNOUNCEMENT | AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        this.setServiceInfo(info);

        overlayView = null;
        windowManager = null;
    }

    @SuppressLint("Range")
    private void showOverlayFavoriteContainer() {

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        overlayView = inflater.inflate(R.layout.favorite_container, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int dynamicHeight = 0;
        int dynamicWidth = displayMetrics.widthPixels;
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                "starred = ?",
                new String[]{"1"},
                ContactsContract.CommonDataKinds.Phone.LAST_TIME_USED + " DESC"
        );
        if (cursor != null && cursor.getCount() > 0) {
            dynamicHeight = displayMetrics.heightPixels - convertDpToPx(210);

            JSONObject paramObj = new JSONObject();
            List<String> numbers = new ArrayList<String>();
            try {
                while (cursor.moveToNext()) {
                    numbers.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                }
                paramObj.put("event", "VERIFY_PHONE_LIST");
                paramObj.put("data", numbers);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            ListView listView = overlayView.findViewById(R.id.container_favorite_list);
            WebSocketManager socketManager = new WebSocketManager(this, listView);
            socketManager.getGlobalWebSocket().send(paramObj.toString());
        }

        type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
                dynamicWidth,
                dynamicHeight,
                0,
                convertDpToPx(130),
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP;
        overlayView.setBackgroundColor(Color.TRANSPARENT);

        windowManager.addView(overlayView, params);
    }

    private void hideOverlayFavoriteContainer() {

        if (overlayView != null) {
            windowManager.removeView(overlayView);
            overlayView = null;
        }
    }

    @SuppressLint("Range")
    public void showOverlayIncomingDetail() {

        String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";
        String className = event.getClassName() != null ? event.getClassName().toString() : "";
        int eventType = event.getEventType();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        incomingView = inflater.inflate(R.layout.incoming_call_detail, null);

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && packageName.equals("com.google.android.dialer") && className.equals("com.android.incallui.LegacyInCallActivity")) {

            type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;

            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    0,
                    convertDpToPx(200),
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                    PixelFormat.TRANSLUCENT
            );
            params.gravity = Gravity.TOP;
            incomingView.setBackgroundColor(Color.TRANSPARENT);

            windowManager.addView(incomingView, params);
        }
    }

    public void hideOverlayIncomingDetail() {

        if (incomingView != null) {
            windowManager.removeView(incomingView);
            incomingView = null;
        }
    }

    private int convertDpToPx(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    private void configureNavigateRecents() {

        LinearLayout recent = (LinearLayout) overlayView.findViewById(R.id.switch_recent);
        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("content://call_log/calls"));
                startActivity(intent);
            }
        });
    }

    private void configureNavigateContacts() {

        LinearLayout contacts = (LinearLayout) overlayView.findViewById(R.id.switch_contacts);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("content://call_log/calls"));
                startActivity(intent);
            }
        });
    }

}
