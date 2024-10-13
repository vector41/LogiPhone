package com.example.pms_client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class OverlayDialog extends DialogFragment {

//    public OverlayDialog(Context context) {
//
//        super(context);
//        init();
//    }
//
//    @Override
//    public void setContentView(@NonNull View view) {
//        super.setContentView(view);
//    }
//
//    @Override
//    public void setContentView(int layoutResID) {
//        super.setContentView(layoutResID);
//    }
//
//    @Override
//    public void setContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
//        super.setContentView(view, params);
//    }
//
//    @Override
//    public void addContentView(@NonNull View view, @Nullable ViewGroup.LayoutParams params) {
//        super.addContentView(view, params);
//    }
//
//    @Override
//    public void show() {
//        super.show();
//    }
//
//    @Override
//    public void setOnDismissListener(@Nullable OnDismissListener listener) {
//
//        super.show();
//    }
//
//    private void init() {
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Window window = getWindow();
//        if (window != null) {
//
//            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.gravity = Gravity.BOTTOM | Gravity.CENTER;
//        }
//    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("None Dissdlkf").setMessage("sdfsdfdsfds");

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        return dialog;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    public void closeDialog() {
        dismiss();
    }
}
