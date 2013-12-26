package com.cesspoollife.rgrong;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class HttpRgrong extends AsyncTask<String, Void, String>{
	
	private AsyncResponse delegate=null;
	private boolean isList = true;
	BasicResponseHandler myHandler;
	HttpPost method;
	HttpClient httpclient;
	
	
	HttpRgrong(){
		this.myHandler = new BasicResponseHandler();
		this.method = new HttpPost();
		this.httpclient = new DefaultHttpClient();
	}
	
	@Override
	protected String doInBackground(String... params) {
		String endResult = null;
		try {
			URI uri = new URI(params[0]);
			method.setURI(uri);
			HttpResponse response = httpclient.execute(method);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(),"EUC-KR"));
			endResult ="";
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				endResult += line;
			}
			Header[] headers = response.getHeaders("Set-Cookie");
			for (Header header : headers)
				method.setHeader("cookie",header.getValue());
		} catch (Exception e) {
			Log.e("http-error", e.toString());
			return endResult;
		}
		return endResult;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(this.isList)
			delegate.processFinish(result);
		else
			delegate.contentsFinish(result);
	}
	
	public void setAsyncResponse(AsyncResponse a, boolean isList){
		this.isList = isList;
		this.delegate = a;
	}
}
