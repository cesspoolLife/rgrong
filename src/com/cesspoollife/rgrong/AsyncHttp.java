package com.cesspoollife.rgrong;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.AsyncTask;
import android.util.Log;


@SuppressWarnings({ "deprecation", "resource" })
public class AsyncHttp extends AsyncTask<ArrayList<NameValuePair>, Void, Document> {
	private AsyncResponse delegate;
	private boolean isList;
	private boolean login=false;
	private static String cookie="";
	private String type = "";

	@Override
	protected Document doInBackground(ArrayList<NameValuePair>... data) {
		Document doc = null;
		try{
			String url = data[0].get(0).getValue();
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(data[0], "EUC-KR");
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(entityRequest);
			if(type.equals("write")){
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE); 
				for(int index=0; index < data[0].size(); index++) {
					if(data[0].get(index).getName().indexOf("file")!=-1) {
			                // If the key equals to "image", we use FileBody to transfer the data
						entity.addPart(data[0].get(index).getName(), new FileBody(new File (data[0].get(index).getValue())));
					}else{
			                // Normal string data
						entity.addPart(data[0].get(index).getName(), new StringBody(data[0].get(index).getValue(), Charset.forName("EUC-KR")));
					}
				}
			    httpPost.setEntity(entity);
				httpPost.setHeader("Cookie",cookie);
				httpPost.addHeader("Referer", data[0].get(1).getValue());
			}
			else if(login){
				httpPost.setHeader("Cookie",cookie);
				httpPost.addHeader("Referer", data[0].get(1).getValue());
			}
			else{
				httpPost.setHeader("Cookie",cookie);
			}
			HttpClient http = new DefaultHttpClient();
			HttpResponse responsePost = http.execute(httpPost);
			if(type.equals("write")){
			}else if(login){
				Header[] headers = responsePost.getHeaders("Set-Cookie");
                for (Header header : headers)
                	cookie = header.getValue();
                HttpPost httpMain = new HttpPost("http://rgr.kr/rgr/main.php");
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
		Log.e("t", doc.toString());
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
	
	public void setAsyncResponse(AsyncResponse a, String type){
		this.delegate = a;
		this.type = type;
	}
	
	public void seCookie(String cookie){
		AsyncHttp.cookie=cookie;
	}
}
