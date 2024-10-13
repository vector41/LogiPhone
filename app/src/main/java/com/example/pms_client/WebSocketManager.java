package com.example.pms_client;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager {

    public Context globalContext;
    public ListView globalListView;
    public OkHttpClient client = new OkHttpClient();
    public Request request = new Request.Builder().url("ws://163.43.30.119:6001/app/3aea7acffe7a50de88de?protocol=7&client=js&version=4.3.1&flash=false").build();
    public WebSocket globalWebSocket = null;

    public WebSocket getGlobalWebSocket() {

        if (globalWebSocket == null) {
            globalWebSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                    super.onClosed(webSocket, code, reason);
                }

                @Override
                public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                    try {
                        JSONObject obj = new JSONObject(text);
                        String event = obj.getString("event");
                        if (event.equals("VERIFIED_PHONE_LIST")) {
                            List<String> numbers = new ArrayList<String>();
                            JSONArray arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {
                                numbers.add(arr.getString(i));
                            }

                            FavoriteListHelper helper = new FavoriteListHelper(globalContext, globalListView);
                            helper.setupFavoriteList(numbers);
                            webSocket.close(1000, null);
                        } else if (event.equals("GOT_USER_DETAIL")) {
                            JSONObject resultObj = obj.getJSONObject("data");
                            LayoutInflater inflater = (LayoutInflater) globalContext.getSystemService(globalContext.LAYOUT_INFLATER_SERVICE);
                            View incomingView = inflater.inflate(R.layout.incoming_call_detail, null);
                            PhoneAppAccessibilityService service = PhoneAppAccessibilityService.getInstance();

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                try {
                                    IncomingCallHelper helper = new IncomingCallHelper(globalContext, incomingView);
                                    helper.displayIncomingDetail(resultObj);

                                    service.showOverlayIncomingDetail();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }, 500);
                            webSocket.close(1000, null);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                    super.onFailure(webSocket, t, response);
                }

                @Override
                public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                    super.onClosing(webSocket, code, reason);
                }

                @Override
                public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                    super.onMessage(webSocket, bytes);
                }

                @Override
                public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                    super.onOpen(webSocket, response);
                }
            });
        }
        return globalWebSocket;
    }

    public WebSocketManager(Context context, ListView listView) {

        this.globalContext = context;
        this.globalListView = listView;
    }

}
