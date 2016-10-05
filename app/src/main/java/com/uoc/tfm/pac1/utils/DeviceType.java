package com.uoc.tfm.pac1.utils;

import android.content.Context;

import com.uoc.tfm.pac1.R;

/**
 * @author Ruben Carmona
 * @project TFM - PAC1
 * @date 10/2016
 */

public enum DeviceType {
    TABLET, PHONE;

    public static boolean isTablet(Context context) {
        return TABLET == DeviceType.valueOf(context.getString(R.string.deviceType).toUpperCase());
    }

    public static boolean isPhone(Context context) {
        return PHONE == DeviceType.valueOf(context.getString(R.string.deviceType).toUpperCase());
    }
}
