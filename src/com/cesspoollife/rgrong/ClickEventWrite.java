package com.cesspoollife.rgrong;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class ClickEventWrite implements OnClickListener {

	@Override
	public void onClick(View v) {
		View r = v.getRootView();
		Context c = (MainActivity)r.getContext();
		final Typeface fontawesome = Typeface.createFromAsset( c.getAssets(), "fontawesome-webfont.ttf" );
		SlidingDrawer sl = (SlidingDrawer)r.findViewById(R.id.slide_write);
		if(!sl.isOpened()){
			TextView tv = (TextView)r.findViewById(R.id.title_bar_left_sec_btn);
			tv.setText(R.string.fa_times);
			tv.setTextSize((float)(r.getResources().getDimension(R.dimen.titlebar_btn_size)));
			tv.setTypeface(fontawesome);
			tv.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					((TextView)v).setText("");
					((TextView)v).setTextSize((float)(v.getRootView().getResources().getDimension(R.dimen.titlebar_btn_size)));
					((TextView)v).setTypeface(fontawesome);
					EditText subject = (EditText)v.getRootView().findViewById(R.id.slide_write_subject);
					EditText memo = (EditText)v.getRootView().findViewById(R.id.slide_write_memo);
					InputMethodManager imm = (InputMethodManager) v.getRootView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(memo.getWindowToken(), 0);
					SlidingDrawer sl = (SlidingDrawer)v.getRootView().findViewById(R.id.slide_write);
					sl.animateOpen();
					subject.setText("");
					memo.setText("");
					((MainActivity)v.getRootView().getContext()).getUploadImagePath().clear();
					LinearLayout ll = (LinearLayout)v.getRootView().findViewById(R.id.slide_write_image_layout);
					ll.removeAllViews();
				}
				
			});
			EditText et = (EditText)r.findViewById(R.id.slide_write_subject);
			et.requestFocus();
			Button filebtn = (Button)r.findViewById(R.id.slide_write_file_btn);
			filebtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    ((MainActivity)v.getRootView().getContext()).startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), MainActivity.SELECT_PICTURE);
				}
				
			});
		}else{
			TextView tv = (TextView)r.findViewById(R.id.title_bar_left_sec_btn);
			tv.setText("");
			tv.setTextSize((float)(r.getResources().getDimension(R.dimen.titlebar_btn_size)));
			tv.setTypeface(fontawesome);
			tv.setOnClickListener(null);
			EditText subject = (EditText)r.findViewById(R.id.slide_write_subject);
			EditText memo = (EditText)r.findViewById(R.id.slide_write_memo);
			InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(subject.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(memo.getWindowToken(), 0);
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("url", ((MainActivity)c).getWriteURL()));
			data.add(new BasicNameValuePair("Referer", "http://rgrong.kr/m/"+((MainActivity)c).getURL()));
			data.add(new BasicNameValuePair("subject", subject.getText().toString()));
			data.add(new BasicNameValuePair("memo", memo.getText().toString()));
			for(int i=0;i<((MainActivity)c).getUploadImagePath().size();i++){
				data.add(new BasicNameValuePair("file"+String.valueOf(i+1), ((MainActivity)c).getUploadImagePath().get(i)));
			}
		    List<NameValuePair> parameters;
			try {
				parameters = URLEncodedUtils.parse(new URI(((MainActivity)c).getWritePage()), "EUC-KR");
				for (NameValuePair p : parameters) {
					String name = p.getName();
					String value = p.getValue();
					if(value==null)
						value="";
			        data.add(new BasicNameValuePair(name, value));
			    }
			} catch (URISyntaxException e) {
				
			}
			((MainActivity)c).getHttpWrite(data);
			subject.setText("");
			memo.setText("");
			((MainActivity)c).getUploadImagePath().clear();
			LinearLayout ll = (LinearLayout)v.getRootView().findViewById(R.id.slide_write_image_layout);
			ll.removeAllViews();
		}
		sl.animateOpen();
	}

}
