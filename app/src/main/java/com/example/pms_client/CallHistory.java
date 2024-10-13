package com.example.pms_client;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

public class CallHistory extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_history);
        ImageView backButton = findViewById(R.id.call_history_send_back);
        backButton.setOnClickListener(v -> finish());
        ImageView bottomNavMenu = findViewById(R.id.bottom_nav_menu);

        bottomNavMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.history_popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.copy_number) {
                    Toast.makeText(CallHistory.this, "電話番号をコピー", Toast.LENGTH_SHORT).show();
                    // Handle copy number action
                    return true;
                } else if (itemId == R.id.block) {
                    Toast.makeText(CallHistory.this, "ブロック", Toast.LENGTH_SHORT).show();
                    // Handle block action
                    return true;
                } else if (itemId == R.id.delete_history) {
                    Toast.makeText(CallHistory.this, "履歴を削除", Toast.LENGTH_SHORT).show();
                    // Handle delete history action
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }
}
