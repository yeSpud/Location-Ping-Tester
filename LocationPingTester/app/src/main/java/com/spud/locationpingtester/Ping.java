package com.spud.locationpingtester;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by Spud on 2019-12-11 for the project: Locaion Ping tester.
 * <p>
 * For the license, view the file titled LICENSE at the root of the project
 */
public class Ping extends AsyncTask<Void, Void, Void> {

	private WeakReference<MainActivity> activity;

	Ping(MainActivity activity) {
		this.activity = new WeakReference<>(activity);
	}

	@Override
	protected Void doInBackground(Void... voids) {
		Log.i("Ping thread", "Starting...");
		MainActivity a = this.activity.get();
		while (!this.isCancelled() && !Thread.interrupted()) {
			a.setStatus("Trying to make a connection...");
			if (this.ping8888()) {
				Log.i("Ping thread", "Ping successful!");
				String location = a.getLocation();
				Log.i("Ping thread", "Location: " + location);
				a.setStatus(location);
				a.appendToFile(location + "\n");
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
			Log.i("Ping thread", "Looping...");
		}
		Log.i("Ping thread", "Stopping...");
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	private boolean ping8888() {
		Runtime runtime = Runtime.getRuntime();
		try {
			return runtime.exec("/system/bin/ping -c 1 8.8.8.8").waitFor() == 0;
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
