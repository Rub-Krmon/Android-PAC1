package com.uoc.pac2.utils;

import android.content.Context;

import com.uoc.pac2.R;

/**
 * @author Ruben Carmona
 * @project TFM - PAC3
 * @date 10/2016
 */

// Enumeración que nos permite detectar fácilmente si
// el dispositivo en el que estamos ejecutando la aplicación
// es un teléfono o una tablet
public enum DeviceType {
    TABLET, PHONE;

    public static boolean isTablet(Context context) {
        return TABLET == DeviceType.valueOf(context.getString(R.string.deviceType).toUpperCase());
    }

    public static boolean isPhone(Context context) {
        return PHONE == DeviceType.valueOf(context.getString(R.string.deviceType).toUpperCase());
    }
}
