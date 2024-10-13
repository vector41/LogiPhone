package com.example.pms_client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

public class SearchContactAdapter extends BaseAdapter {


    private final Context context;
    private final int layoutId;
    private final SearchContactsItem[] contactsList;

    public SearchContactAdapter(Context context, int layoutId, SearchContactsItem[] contactsList) {

        this.context = context;
        this.layoutId = layoutId;
        this.contactsList = contactsList;
    }

    @Override
    public int getCount() {
        return contactsList.length;
    }

    @Override
    public Object getItem(int i) {
        return contactsList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View lineItem;
        LayoutInflater layout = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        lineItem = layout.inflate(layoutId, null);

        TextView nickName = (TextView)lineItem.findViewById(R.id.contact_avatar_nickname);
        TextView name = (TextView)lineItem.findViewById(R.id.search_contact_name);
        LinearLayout avatar = (LinearLayout)lineItem.findViewById(R.id.contact_avatar_container);

        SearchContactsItem searchContactsItem = contactsList[i];
        nickName.setText(searchContactsItem.getNickName());
        name.setText(searchContactsItem.getName());

        Drawable drawableAvatar = ContextCompat.getDrawable(this.context, searchContactsItem.getIconColor());
        avatar.setBackground(drawableAvatar);

        return lineItem;
    }
}
