package com.spud.locationpingtester;

import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Spud on 2019-12-11 for the project: Locaion Ping tester.
 * <p>
 * For the license, view the file titled LICENSE at the root of the project
 */
public class Location implements LocationListener {

	public static Double[] location = new Double[2];

	@Override
	public void onLocationChanged(android.location.Location location) {
		Location.location[0] = location.getLatitude();
		Location.location[1] = location.getLongitude();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
