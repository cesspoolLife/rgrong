package com.cesspoollife.rgrong;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class ClickEventComment implements OnClickListener {

	@Override
	public void onClick(View v) {
		View r = v.getRootView();
		Context c = (MainActivity)r.getContext();
		final Typeface fontawesome = Typeface.createFromAsset( c.getAssets(), "fontawesome-webfont.ttf" );
		SlidingDrawer sl = (SlidingDrawer)r.findViewById(R.id.slide_comment);
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
					EditText et = (EditText)v.getRootView().findViewById(R.id.slide_memo);
					InputMethodManager imm = (InputMethodManager) v.getRootView().getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
					SlidingDrawer sl = (SlidingDrawer)v.getRootView().findViewById(R.id.slide_comment);
					sl.animateOpen();
				}
				
			});
			EditText et = (EditText)r.findViewById(R.id.slide_memo);
			et.requestFocus();
		}else{
			TextView tv = (TextView)r.findViewById(R.id.title_bar_left_sec_btn);
			tv.setText("");
			tv.setTextSize((float)(r.getResources().getDimension(R.dimen.titlebar_btn_size)));
			tv.setTypeface(fontawesome);
			tv.setOnClickListener(null);
			EditText et = (EditText)r.findViewById(R.id.slide_memo);
			InputMethodManager imm = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("url", ((MainActivity)c).getCommentURL()));
			data.add(new BasicNameValuePair("Referer", "http://rgrong.kr/m/"+((MainActivity)c).getURL()));
			data.add(new BasicNameValuePair("memo",et.getText().toString()));
		    List<NameValuePair> parameters;
			try {
				parameters = URLEncodedUtils.parse(new URI(((MainActivity)c).getURL()), "EUC-KR");
				for (NameValuePair p : parameters) {
					String name = p.getName();
					String value = p.getValue();
					if(value==null)
						value="";
			        data.add(new BasicNameValuePair(name, value));
			    }
			} catch (URISyntaxException e) {
				
			}
			((MainActivity)c).getHttpAsync(data);
			et.setText("");
		}
		sl.animateOpen();
	}	

}
