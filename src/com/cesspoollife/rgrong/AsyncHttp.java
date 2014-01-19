package com.cesspoollife.rgrong;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncHttp extends AsyncTask<ArrayList<NameValuePair>, Void, Document> {
	private AsyncResponse delegate;
	private boolean isList;
	private boolean login=false;
	private static String cookie="";

	@Override
	protected Document doInBackground(ArrayList<NameValuePair>... data) {
		Document doc = null;
		try{
			String url = data[0].get(0).getValue();
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(data[0], "EUC-KR");
			HttpPost httpPost = new HttpPost(url);
			HttpClient http = new DefaultHttpClient();
			httpPost.setEntity(entityRequest);
			if(login){
				httpPost.setHeader("Cookie",cookie);
				httpPost.addHeader("Referer", data[0].get(1).getValue());
			}
			else{
				httpPost.setHeader("Cookie",cookie);
			}
			HttpResponse responsePost = http.execute(httpPost);
			if(login){
				Header[] headers = responsePost.getHeaders("Set-Cookie");
                for (Header header : headers)
                	cookie = header.getValue();
                HttpPost httpMain = new HttpPost("http://rgrong.kr/bbs/main.php");
                httpPost.setHeader("Cookie",cookie);
                responsePost = http.execute(httpMain);
			}
			String endResult ="";
            String line = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responsePost.getEntity().getContent(),"EUC-KR"));
            while ((line = bufferedReader.readLine()) != null) {
                    endResult += line;
            }
            doc = Jsoup.parse(endResult);
		} catch (Exception e) {
			Log.e("http-error", e.toString());
			return null;
		}
	return doc;
	}
	
	@Override
	protected void onPostExecute(Document doc) {
		if(this.delegate==null){
			cookie="";
		}
		else if(login){
			delegate.loginFinish(doc);
		}
		else if(this.isList)
			delegate.processFinish(doc);
		else
			delegate.contentsFinish(doc);
	}
	
	public void setAsyncResponse(){
		this.delegate = null;
	}
	
	public void setAsyncResponse(AsyncResponse a){
		this.delegate = a;
		login = true;
	}
	
	public void setAsyncResponse(AsyncResponse a, boolean isList){
		this.isList = isList;
		this.delegate = a;
	}
	
	public void seCookie(String cookie){
		AsyncHttp.cookie=cookie;
	}
}
