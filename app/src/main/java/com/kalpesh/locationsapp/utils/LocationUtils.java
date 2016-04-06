package com.kalpesh.locationsapp.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by kalpeshpatel on 06/04/16.
 */
public class LocationUtils {

    public static boolean isLocationSettingEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled, network_enabled;
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps_enabled || network_enabled;
    }

}
