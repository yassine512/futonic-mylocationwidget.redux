package com.futonredemption.mylocation.tasks;

import java.util.List;
import java.util.concurrent.Future;

import org.beryl.diagnostics.Logger;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.futonredemption.mylocation.MyLocationBundle;

public class RetrieveAddressTask extends ContextAwareCallable<MyLocationBundle> {

	Future<Location> futureLocation = null;
	public RetrieveAddressTask(Context context) {
		super(context);
	}

	public MyLocationBundle call() throws Exception {
		MyLocationBundle bundle = null;
		try {
			Logger.w("Starting address finding.");
			final Location location = futureLocation.get();
			bundle = new MyLocationBundle(location);
			Geocoder coder = new Geocoder(context);
			final List<Address> addresses = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			bundle.setAddress(addresses.get(0));
		} finally {
			Logger.w("Address Finishing: ");
			Logger.w(bundle.getAddress().toString());
		}
		
		return bundle;
	}

	public void setLocation(Future<Location> futureLocation) {
		this.futureLocation = futureLocation;
	}

}