package com.appdevloop.bessem.hikerswatch;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by AppDevloop on 25/09/2018.
 */
public class PackageUtil {
    static boolean checkPermission(Context context, String accessFineLocation) {

        int res = context.checkCallingOrSelfPermission(accessFineLocation);
        return (res == PackageManager.PERMISSION_GRANTED);

    }
}
