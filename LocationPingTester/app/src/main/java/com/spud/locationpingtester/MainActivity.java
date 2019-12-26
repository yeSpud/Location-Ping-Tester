package com.spud.locationpingtester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

	private final String FILE_NAME = "out.txt";
	private TextView textView;
	private Ping pingerThread;

	private Location location = new Location();

	private boolean run = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		this.run = false;

		final Button button = this.findViewById(R.id.button);

		this.textView = this.findViewById(R.id.text);

		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			button.setEnabled(false);
			this.textView.setText(R.string.no_location);
		} else {
			button.setEnabled(true);
			((LocationManager) this.getSystemService(Context.LOCATION_SERVICE))
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this.location);
			this.textView.setText(R.string.process_stopped);
		}

		button.setOnClickListener((click) -> {
			this.run = !this.run;
			if (this.run) {
				// Start the thread
				try {
					this.recreateFile();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				this.pingerThread = new Ping(this);
				this.pingerThread.execute();
				button.setText(R.string.stop);

			} else {
				// Stop the thread
				this.pingerThread.cancel(true);
				button.setText(R.string.start);
				this.textView.setText(R.string.process_stopped);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
	}

	public void setStatus(String status) {
		this.runOnUiThread(() -> this.textView.setText(status));
	}

	public String getLocation() {
		return String.format(Locale.ENGLISH, "Latitude: %f, Longitude: %f", Location.location[0], Location.location[1]);
	}

	public void appendToFile(String string) {
		try {
			FileOutputStream fos = this.openFileOutput(this.FILE_NAME, MODE_APPEND);
			fos.write(string.getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void recreateFile() throws IOException {
		this.deleteFile(this.FILE_NAME);
		File file = new File(this.getFilesDir() + "/" + this.FILE_NAME);
		if (!file.createNewFile()) {
			throw new IOException();
		}
	}
}
