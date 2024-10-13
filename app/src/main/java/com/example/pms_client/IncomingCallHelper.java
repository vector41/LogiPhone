package com.example.pms_client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class IncomingCallHelper {

    private Context context;
    private View view;

    public IncomingCallHelper(Context context, View view) {
        this.context = context;
        this.view = view;
    }

    public void displayIncomingDetail(JSONObject obj) {

        JSONObject paramObj = obj;
        try {
            TextView companyText = (TextView) view.findViewById(R.id.incoming_company);
            TextView nameText = (TextView) view.findViewById(R.id.incoming_name);
            TextView numberText = (TextView) view.findViewById(R.id.incoming_number);

            String companyName = paramObj.getString("company_name");
            String name = paramObj.getString("person_name");
            String number = "";


            companyText.setText(companyName);
            nameText.setText(name);
            numberText.setText(number);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
