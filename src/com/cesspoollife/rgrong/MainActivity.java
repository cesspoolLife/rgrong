package com.cesspoollife.rgrong;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements AsyncResponse{
	
	private ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		HttpRgrong httprgrong = new HttpRgrong();
		httprgrong.setAsyncResponse(this, true);
		String url = "http://rgrong.kr/m/mlist.php?id=rgr201311";
		httprgrong.execute(url);
		
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
	}

	@Override
	public void onBackPressed() {
		if(mPager.getCurrentItem()==1)
			mPager.setCurrentItem(0);
		else
			super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void processFinish(String result){
		LinearLayout sv = (LinearLayout)findViewById(R.id.listLayout);
		try{
			Document doc = Jsoup.parse(result);
			 
			// get page title
			Elements elements = doc.select("span");
			Iterator<Element> it = elements.iterator();
			Element e = null;
			while((e = it.next())!=null){
				LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.list_view, null);
				if(e.getElementsByTag("b").first()!=null){
					e = e.getElementsByTag("b").first().getElementsByTag("font").first();
				}
				TextView tv = (TextView)ll.findViewById(R.id.countcomment);
				tv.setText(e.html());
				e = it.next();
				Element e1 = e.getElementsByTag("a").first();
				String url = e1.attr("href");
				tv = (TextView)ll.findViewById(R.id.url);
				tv.setText(url);
				tv = (TextView)ll.findViewById(R.id.title);
				tv.setText(Html.fromHtml(e1.html()));
				if(e1.nextElementSibling()!=null){
					ll.findViewById(R.id.is_img).setVisibility(View.VISIBLE);
				}
				e = it.next();
				tv = (TextView)ll.findViewById(R.id.ownerid);
				String id = e.html();
				tv.setText(id.substring(0, id.indexOf("<")));
				e = it.next();
				e = it.next();
				e = e.getElementsByTag("font").first().getElementsByTag("b").first();
				tv = (TextView)ll.findViewById(R.id.writedtime);
				tv.setText(e.html());
				sv.addView(ll);
				ll.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						try{
							mPager.setCurrentItem(1);
							TextView tv = (TextView)v.findViewById(R.id.url);
							String url = tv.getText().toString(); 
							HttpRgrong httprgrong = new HttpRgrong();
							httprgrong.setAsyncResponse(MainActivity.this, false);
							url = "http://rgrong.kr/m/"+url;
							httprgrong.execute(url);
						}catch(Exception e){
							Log.e("click-error", e.toString());
						}
					}
				});
			}
		}catch(Exception e){
			Log.e("jsoup",e.toString());
		}
	}
	
	public void contentsFinish(String result){
		try{
			Document doc = Jsoup.parse(result);
			Element e = doc.getElementById("ArticleVTitle");
			TextView tv = (TextView)findViewById(R.id.contentstitle);
			tv.setText(Html.fromHtml(e.getElementsByTag("font").first().html()));
			Elements elements = doc.getElementsByClass("box");
			tv = (TextView)findViewById(R.id.contentsuserid);
			tv.setText(elements.select("a").first().html());
			elements = doc.getElementsByTag("table");
			e = elements.get(5).getElementsByTag("td").first();
			tv = (TextView)findViewById(R.id.contents);
			tv.setText(Html.fromHtml(e.html()));
			Iterator<Element> it = doc.getElementsByAttributeValue("name", "zb_target_resize").iterator();
			LinearLayout cl = (LinearLayout)findViewById(R.id.contents_layout);
			int clnum = cl.getChildCount();
			if(clnum>1)
				cl.removeViews(1, cl.getChildCount()-1);
			while(it.hasNext()){
				e = it.next();
				LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.linear_img, null);
				ImageView i = (ImageView)ll.findViewById(R.id.contents_img);
				HttpImage hi = new HttpImage(i);
				hi.execute(e.attr("src"));
				cl.addView(ll);
			}
			int numtable = elements.size()-5;
			for(int i=7;i<numtable;i++){
				Element e1 = elements.get(i++);
				LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.linear_commnet, null);
				TextView tv1 = (TextView)ll.findViewById(R.id.comment_userid);
				tv1.setText(e1.select("b").get(1).html());
				e1 = elements.get(i);
				tv1 = (TextView)ll.findViewById(R.id.comment_text);
				tv1.setText(e1.select(".Apple-style-span").first().child(0).html());
				cl.addView(ll);
			}
		}catch(Exception e){
			Log.e("error", e.toString());
		}
	}
	
	/**
	 * PagerAdapter 
	 */
	private class PagerAdapterClass extends PagerAdapter{
		
		private LayoutInflater mInflater;

		public PagerAdapterClass(Context c){
			super();
			mInflater = LayoutInflater.from(c);
		}
		
		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object instantiateItem(View pager, int position) {
			View v = null;
    		if(position==0){
    			v = mInflater.inflate(R.layout.linear_list, null);
    		}else {
    			v = mInflater.inflate(R.layout.linear_contents, null);
    		}
    		
    		((ViewPager)pager).addView(v, 0);
    		
    		return v; 
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {	
			((ViewPager)pager).removeView((View)view);
		}
		
		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj; 
		}

		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
		@Override public void finishUpdate(View arg0) {}
	}
}
