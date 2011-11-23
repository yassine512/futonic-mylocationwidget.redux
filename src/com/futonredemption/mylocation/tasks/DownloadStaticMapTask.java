package com.futonredemption.mylocation.tasks;

import java.io.File;
import java.util.concurrent.Future;

import org.beryl.diagnostics.Logger;
import org.beryl.io.DirectoryUtils;
import org.beryl.io.FileUtils;

import com.futonredemption.mylocation.MyLocationRetrievalState;
import com.futonredemption.mylocation.StaticMap;
import com.futonredemption.mylocation.google.maps.Parameters;
import com.futonredemption.mylocation.google.maps.Parameters.Center;
import com.futonredemption.mylocation.google.maps.Parameters.Dimension;
import com.futonredemption.mylocation.google.maps.StaticMapsClient;

import android.content.Context;

public class DownloadStaticMapTask extends AbstractMyLocationTask {

	public DownloadStaticMapTask(Context context, MyLocationRetrievalState state) {
		super(context, state);
	}
	
	public DownloadStaticMapTask(Context context, Future<MyLocationRetrievalState> state) {
		super(context, state);
	}

	@Override
	protected void loadData(MyLocationRetrievalState state) {

		if(state.hasLocation()) {
			Parameters params = new Parameters();
			params.center = new Center(state.getLocation());
			params.size = new Dimension(128,128);
			params.scale = 2;
			params.zoom = 9;
			try {
				StaticMap map = new StaticMap();
				File targetDirectory = DirectoryUtils.getApplicationExternalStorageDirectory(context, "static-maps");
				FileUtils.createDirectory(targetDirectory);
				File targetFile = File.createTempFile("static-map", ".png", targetDirectory);
				StaticMapsClient client = new StaticMapsClient();
				map = client.downloadMap(targetFile, params);
				state.setStaticMap(map);
			} catch(Exception e) {
				Logger.e(e);
			}
		}
	}
}
