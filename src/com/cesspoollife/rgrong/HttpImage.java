package com.cesspoollife.rgrong;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class HttpImage extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    ProgressBar mProgressBar;
    String url = "";

    public HttpImage(ImageView bmImage, ProgressBar pv) {
        this.bmImage = bmImage;
        this.mProgressBar = pv;
    }

    protected Bitmap doInBackground(String... urls) {
        url = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
    	try{
    		int[] glInt = new int[1];
			GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, glInt, 0);
			if(result.getWidth()>glInt[0]&&result.getHeight()>glInt[0]){
    			result = Bitmap.createScaledBitmap(result, glInt[0], glInt[0], true);
			}
    		else{
	    		if(result.getWidth()>glInt[0]){
	    			result = Bitmap.createScaledBitmap(result, glInt[0], result.getHeight(), true);
	    		} 
	    		if(result.getHeight()>glInt[0]){
	    			result = Bitmap.createScaledBitmap(result, result.getWidth(), glInt[0], true);
	    		}
    		}
			mProgressBar.setVisibility(View.GONE);
    		bmImage.setImageBitmap(result);
    		bmImage.setAdjustViewBounds(true);
    	}catch(Exception e){
  //  		Ion.with(bmImage).load(url);
    	}
    }
}
