package com.cesspoollife.rgrong;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Login {
	private String id;
	private String pw;
	private ArrayList<NameValuePair> data;
	private final String LOGIN_URL = "http://rgrong.kr/bbs/login_check.php";
	private final String LOGOUT_URL = "http://rgrong.kr/bbs/logout.php";
	
	public Login(){
		data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("url", LOGOUT_URL));
	}
	
	public Login(String id, String pw){
		data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("url", LOGIN_URL));
		data.add(new BasicNameValuePair("user_id", id));
		data.add(new BasicNameValuePair("password", pw));
	}
	
	@SuppressWarnings("unchecked")
	public void logout(){
		AsyncHttp http= new AsyncHttp();
		http.setAsyncResponse();
		http.execute(data);
	}
	
	@SuppressWarnings("unchecked")
	public void login(){
		AsyncHttp http= new AsyncHttp();
		http.setAsyncResponse();
		http.execute(data);
	}
	
	@SuppressWarnings("unchecked")
	public void login(AsyncResponse a){
		AsyncHttp http= new AsyncHttp();
		http.setAsyncResponse(a);
		http.execute(data);
	}
	
	public void setData(){
		data.add(new BasicNameValuePair("user_id", id));
		data.add(new BasicNameValuePair("password", pw));
	}
	
	public void setData(String id, String pw){
		data.add(new BasicNameValuePair("user_id", id));
		data.add(new BasicNameValuePair("password", pw));
	}
	
	public void setUserId(String id){
		this.id = id;
	}
	
	public void setPassWord(String pw){
		this.pw = pw;
	}
}
