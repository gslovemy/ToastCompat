package com.example.gs.toast;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by gaosheng on 2016/7/29.
 * 单列toast
 */

public class ToastUtil {

    private static Toast toast;

    public static void setToast(Context context, String toastStr) {

        if (toast == null) {
            toast = Toast.makeText(context, toastStr, Toast.LENGTH_SHORT);
        } else {

            toast.setText(toastStr);
        }
        toast.show();
    }
}
