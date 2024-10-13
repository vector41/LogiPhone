package com.example.pms_client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class FavoriteAdapter extends BaseAdapter {

    private final Context context;
    private final int layoutId;
    private final FavoriteListItem[] favoriteList;

    public FavoriteAdapter(Context context, int layoutId, FavoriteListItem[] favoriteList) {

        this.context = context;
        this.layoutId = layoutId;
        this.favoriteList = favoriteList;
    }

    @Override
    public int getCount() {
        return favoriteList.length;
    }

    @Override
    public Object getItem(int i) {
        return favoriteList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View lineItem;
        LayoutInflater layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lineItem = layout.inflate(layoutId, null);

        TextView nickName = (TextView) lineItem.findViewById(R.id.favorite_avatar_nickname);
        TextView name = (TextView) lineItem.findViewById(R.id.favorite_item_name);
        TextView phone = (TextView) lineItem.findViewById(R.id.favorite_item_phone);
        LinearLayout avatar = (LinearLayout) lineItem.findViewById(R.id.favorite_avatar_container);
        LinearLayout shared = (LinearLayout) lineItem.findViewById(R.id.favorite_shared);

        FavoriteListItem favoriteItem = favoriteList[i];
        nickName.setText(favoriteItem.getNickName());
        name.setText(favoriteItem.getName());
        phone.setText(favoriteItem.getPhoneTitle() + " " + favoriteItem.getPhone());
        shared.setVisibility(favoriteItem.getShared() ? View.VISIBLE : View.GONE);

        Drawable drawableAvatar = ContextCompat.getDrawable(this.context, favoriteItem.getIconColor());
        avatar.setBackground(drawableAvatar);

        Drawable drawableNone = ContextCompat.getDrawable(this.context, R.drawable.none_background);
        Drawable drawableSelected = ContextCompat.getDrawable(this.context, R.drawable.favorite_active_background);

        lineItem.findViewById(R.id.favorite_item_info).setOnClickListener(v -> {

            if(lineItem.findViewById(R.id.favorite_contact_details).getVisibility() == View.GONE) {

                lineItem.findViewById(R.id.favorite_contact_details).setVisibility(View.VISIBLE);
                lineItem.findViewById(R.id.favorite_list_item).setBackground(drawableSelected);
            } else {

                lineItem.findViewById(R.id.favorite_contact_details).setVisibility(View.GONE);
                lineItem.findViewById(R.id.favorite_list_item).setBackground(drawableNone);
            }
        });

        lineItem.findViewById(R.id.favorite_sms).setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:+" + favoriteItem.getPhone()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        });

        lineItem.findViewById(R.id.favorite_log).setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(favoriteItem.getPhone()));
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        });

        lineItem.findViewById(R.id.favorite_profile).setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri contactUri = ContactsContract.Contacts.getLookupUri(favoriteItem.getId(), favoriteItem.getLookupKey());
            intent.setData(contactUri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        });

        lineItem.findViewById(R.id.favorite_ic_call).setOnClickListener(v -> {

//            Intent intent = new Intent(Intent.ACTION_CALL);
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + favoriteItem.getPhone()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        });

        return lineItem;
    }
}
