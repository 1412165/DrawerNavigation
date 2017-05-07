package com.nthieuitus.findplaces.GPS;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by Nguyen Trung Hieu on 06-May-17.
 */

public interface GPSTrackerListener {
    void onGPSTrackerLocationChanged(Location location);
    void onGPSTrackerStatusChanged(String provider, int status, Bundle extras);
}
