package com.example.pms_client;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListHelper {
    
    Context context = null;
    ListView favoriteContainer = null;
    FavoriteListItem[] favoriteItems ;
    List<String> baseNumberList = new ArrayList<>();
    ProgressDialog loadingDialog;
    Thread loadingThread;

    @SuppressLint("Range")
    public FavoriteListHelper(Context context, ListView listView) {

        this.context = context;
        this.favoriteContainer = listView;
//        loadingThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                loadingDialog = new ProgressDialog(context);
//                loadingDialog.setMessage("サーバーに接続中です。しばらくお待ちください。");
//                loadingDialog.show();
//            }
//        });
//        loadingThread.start();
    }

    @SuppressLint("Range")
    public void setupFavoriteList(List<String> numbers) {

        this.baseNumberList = numbers;

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                "starred = ?",
                new String[]{"1"},
                ContactsContract.CommonDataKinds.Phone.LAST_TIME_USED + " DESC"
        );
        int count = 0;
        if(cursor != null && cursor.getCount() > 0) {
            favoriteItems = new FavoriteListItem[cursor.getCount()];
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                int type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                boolean shared = baseNumberList.contains(number);

                favoriteItems[count] = new FavoriteListItem(id, lookupKey, name, number, getPhoneNumberType(type), R.drawable.green_button_background, shared);
                count ++;
            }
        }
        try {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    FavoriteAdapter adapter = new FavoriteAdapter(context, R.layout.favorite_list_item, favoriteItems);
                    favoriteContainer.setAdapter(adapter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        loadingThread.destroy();
    }

    private String getPhoneNumberType(int type) {

        switch (type) {
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                return "携帯";
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                return "自宅";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                return "勤務先";
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                return "FAX(勤務先)";
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                return "FAX(自宅)";
            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                return "ポケベル";
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                return "その他";
            case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK:
                return "コールバック";
            case ContactsContract.CommonDataKinds.Phone.TYPE_CAR:
                return "クルマ";
            case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN:
                return "会社代表番号";
            case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN:
                return "ISDN";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN:
                return "メイン";
            case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX:
                return "FAX (その他)";
            case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO:
                return "無線";
            case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX:
                return "テレックス";
            case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD:
                return "TTY TDDD";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                return "携帯(勤務先)";
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER:
                return "ポケベル (勤務先)";
            case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT:
                return "アシスタント";
            case ContactsContract.CommonDataKinds.Phone.TYPE_MMS:
                return "MMS";
            default:
                return "カスタム";
        }
    }

}
