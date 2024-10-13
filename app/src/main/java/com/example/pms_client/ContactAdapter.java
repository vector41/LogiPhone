package com.example.pms_client;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private static final int REQUEST_CALL_PERMISSION = 1;
    private List<Contact> contactList;
    private boolean isFavoriteView = true; // Add a boolean flag to determine which layout to use

    private Context context;

    public ContactAdapter(List<Contact> contactList, boolean isFavoriteView) {
        this.contactList = contactList;
        this.isFavoriteView = isFavoriteView;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.getName());

        // Set mobile and office phone text views
        holder.mobileTextView.setText(contact.getMobilePhone().isEmpty() ? "" : "勤務先 " + contact.getMobilePhone());
        holder.officeTextView.setText(contact.getOfficePhone().isEmpty() ? "" : "携帯  " + contact.getOfficePhone());

        holder.iconImageView.setImageResource(contact.getIconResourceId());

        // Handle contact_main_info click to show/hide details
        holder.contactMainInfo.setOnClickListener(v -> {
            if (holder.contactDetails.getVisibility() == View.GONE) {
                holder.contactDetails.setVisibility(View.VISIBLE);
            } else {
                holder.contactDetails.setVisibility(View.GONE);
            }
        });

        // Set click listener for SMS send action
        holder.sms_send.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SMSSend.class);
            intent.putExtra("contact_name", contact.getName());
            intent.putExtra("mobile_phone", contact.getMobilePhone());
            v.getContext().startActivity(intent);
        });

        // Set click listener for call history action
        holder.callHistory.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CallHistory.class);
            intent.putExtra("contact_name", contact.getName());
            intent.putExtra("mobile_phone", contact.getMobilePhone());
            v.getContext().startActivity(intent);
        });

        // Handle phone icon visibility (only if using Favorite.xml)
        if (!isFavoriteView && holder.phoneIcon != null) {
            holder.phoneIcon.setVisibility(View.GONE);
        }

        holder.phoneIcon.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request permission through the Activity
                if (v.getContext() instanceof Activity) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(),
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                }
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + contact.getMobilePhone()));
                v.getContext().startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView mobileTextView;
        TextView officeTextView;
        ImageView iconImageView;
        LinearLayout contactInfo;
        LinearLayout contactMainInfo;
        LinearLayout contactDetails;
        LinearLayout sms_send;
        LinearLayout callHistory;
        ImageView phoneIcon; // Reference to the phone icon (for Favorite.xml)

        ContactViewHolder(View itemView) {
            super(itemView);

            nameTextView    = itemView.findViewById(R.id.contact_name);
            mobileTextView  = itemView.findViewById(R.id.mobile_phone);
            officeTextView  = itemView.findViewById(R.id.office_phone);
            iconImageView   = itemView.findViewById(R.id.contact_icon);
            contactInfo     = itemView.findViewById(R.id.contact_info);
            contactMainInfo = itemView.findViewById(R.id.contact_main_info);
            contactDetails  = itemView.findViewById(R.id.contact_details);
            sms_send        = itemView.findViewById(R.id.sms_send);
            phoneIcon       = itemView.findViewById(R.id.favorite_ic_call); // Initialize phone icon
            callHistory     = itemView.findViewById(R.id.call_history);
        }
    }
}
