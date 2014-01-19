package com.cesspoollife.rgrong;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements AsyncResponse{
	
	private String id;
	private String password;
	private int RESULT_CANSELED;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Intent intent = getIntent();
		int login = intent.getIntExtra("login", 0);
		
		Button btn = (Button)findViewById(R.id.login_btn);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setLogin();
			}
			
		});
		
		btn = (Button)findViewById(R.id.logout_btn);
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setLogout();
			}
			
		});
		
		if(login==1){
			RelativeLayout rl = (RelativeLayout)findViewById(R.id.login_layout);
			rl.setVisibility(View.GONE);
		}else{
			RelativeLayout rl = (RelativeLayout)findViewById(R.id.logout_layout);
			rl.setVisibility(View.GONE);
		}
	}
	
	private void setLogout(){
		Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
		DatabaseHelper mDbHelper = new DatabaseHelper(this, "RGRONG", null, 1);
		SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
		Cursor cursor = mDb.query("login", new String[] { "id", "password" }, null, null, null, null, null);
		if(cursor!=null&&cursor.getCount()>0){
			Log.e("count", String.valueOf(cursor.getCount()));
			mDb.delete("login", null, null);
		}
		Login login = new Login();
		login.logout();
		Intent intent = new Intent();
		intent.putExtra("login", 0);
		intent.putExtra("nickName", "로그인");
		setResult(RESULT_OK, intent);
		finish();
	}
	
	private void setLogin(){
		TextView idTv = (TextView)findViewById(R.id.id);
		id = idTv.getText().toString().trim();
		TextView pwTv = (TextView)findViewById(R.id.password);
		password = pwTv.getText().toString().trim();
		Login login = new Login(id, password);
		login.login(this);
	}
	
	@Override
	public void loginFinish(Document doc) {
		Pattern p = Pattern.compile("\\<b\\>(.+)\\<\\/b\\>\\<\\/a\\>\\<font color\\=\\\"eeeeee\\\"");
		Matcher m = p.matcher(doc.toString());
		if(m.find()){
			Toast.makeText(this, m.group(1)+" 님 오늘도 즐지롱~♡", Toast.LENGTH_SHORT).show();
			CheckBox auto = (CheckBox)findViewById(R.id.auto_login);
			if(auto.isChecked()){
				DatabaseHelper mDbHelper = new DatabaseHelper(this, "RGRONG", null, 1);
				SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
				Cursor cursor = mDb.query("login", new String[] { "id", "password" }, null, null, null, null, null);
				if(cursor!=null&&cursor.getCount()>0){
					mDb.delete("login", null, null);
				}
				ContentValues initialValues = new ContentValues();
		        initialValues.put("id", id);
		        initialValues.put("password", password);
				mDb.insert("login", null, initialValues);
			}
			Intent intent = new Intent();
			intent.putExtra("login", 1);
			intent.putExtra("nickName", m.group(1)+" 님");
			setResult(RESULT_OK, intent);
			finish();
		}else{
			Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setResult(RESULT_CANSELED);
	}

	@Override
	public void processFinish(Document doc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contentsFinish(Document doc) {
		// TODO Auto-generated method stub
		
	}
}
