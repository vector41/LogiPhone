package com.example.pms_client;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.ContactsContract;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CallStateManager extends BroadcastReceiver {

    private static String lastState = TelephonyManager.EXTRA_STATE_IDLE;
    private static boolean isIncoming = false;
    private static String savedNumber = null;
    PhoneAppAccessibilityService service = new PhoneAppAccessibilityService();

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {

        WebSocketManager webSocketManager = new WebSocketManager(context, null);
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String target = getSimNumber(context) == null ? "0" : getSimNumber(context);

        Log.d("Incoming", "Number" + state + number + target);

        if (state != null && number != null) {

            if (state.equals("RINGING")) {

                isIncoming = true;
                savedNumber = number;
                JSONObject obj = new JSONObject();
                try {
                    obj.put("event", "GET_USER_DETAIL");
                    obj.put("data", savedNumber);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                webSocketManager.getGlobalWebSocket().send(obj.toString());
            } else if (state.equals("OFFHOOK")) {

                if (lastState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    JSONObject obj = new JSONObject();
                    try {
                        JSONObject paramObj = new JSONObject();
                        paramObj.put("sender", number);
                        paramObj.put("receiver", target);

                        obj.put("event", "START_CALL_LOG");
                        obj.put("data", paramObj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    webSocketManager.getGlobalWebSocket().send(obj.toString());
                    webSocketManager.getGlobalWebSocket().close(1000, null);
                    service.hideOverlayIncomingDetail();
                } else {
                    Log.e("Outgoing Accepted : ", number);
                }
            } else if (state.equals("IDLE")) {

                if (isIncoming) {
                    JSONObject obj = new JSONObject();
                    try {
                        JSONObject paramObj = new JSONObject();
                        paramObj.put("sender", number);
                        paramObj.put("receiver", target);

                        obj.put("event", "SAVE_CALL_LOG");
                        obj.put("data", paramObj);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    webSocketManager.getGlobalWebSocket().send(obj.toString());
                    webSocketManager.getGlobalWebSocket().close(1000, null);
                    service.hideOverlayIncomingDetail();
                    isIncoming = false;
                } else {
                    Log.e("Call Rejected : ", number);
                }
            }
            lastState = state;
        }
    }

    private String getSimNumber(Context context) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager manager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                List<SubscriptionInfo> subscriptionInfos = manager.getActiveSubscriptionInfoList();

                if (subscriptionInfos != null && !subscriptionInfos.isEmpty()) {
                    SubscriptionInfo simInfo = subscriptionInfos.get(0);
                    return simInfo.getNumber();
                }
            }
        }
        return null;
    }

}
