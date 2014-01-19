package com.cesspoollife.rgrong;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class AsyncSaveImage extends AsyncTask<String, Void, InputStream> {
	private String url;
	@Override
	protected InputStream doInBackground(String... urls) {
		url = urls[0];
		InputStream in = null;
        try {
            in = new java.net.URL(url).openStream();
            String root = Environment.getExternalStorageDirectory().toString();
    		File myDir = new File(root + "/RGR");  
    		String[] names = url.split("/");
    		String name = names[names.length-1];
    		File filePath = new File(myDir+"/"+name);
    		OutputStream f = new BufferedOutputStream(new FileOutputStream(filePath));;
            byte[] buffer = new byte[4096];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
            	f.write(buffer, 0, len1);
            }
            f.close();
            in.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return in;
	}
}
