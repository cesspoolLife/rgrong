package com.cesspoollife.rgrong;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements AsyncResponse{
	
	private static final int LOGIN_ACTIVITY = 1;
	private ViewPager mPager;
	private String menu = "mlist.php?id=rgr201311";
	private String commentUrl = "http://rgrong.kr/bbs/vote_ex.php";
	private String menuText = "호기심해결";
	private boolean init = true;
	private int firstNo = 0;
	private int lastNo = 99999999;
	private boolean addPre = false;
	private boolean addPost = false;
	private int page = 1;
	private Typeface fontawesome=null;
	private List<ListObject> list = null;
	private PullAndLoadListView listViewItems = null;
	private ArrayAdapterList adapter = null;
	private boolean exit = false;
	private int login = 0;
	private boolean isSync = false;
	private ArrayAdapterItem menuAdapter;
	private List<ObjectItem> ObjectMenuData;
	private String URL;
	
	private String[] menuName = {"로그인", "호기심해결", "모바일게임", "엽기/유머/레어", "명예의전당", "황당뉴스", "야구",
			"스포츠", "연애특강", "큰마을", "연예인갤러리", "게임", "중고장터", "캥거루몰", "브랜디드", 
			"컴퓨터", "사진/카메라", "패션/유행", "노래/음악", "영화/드라마", "만화/애니", "자전거",
			"스마트폰", "LOL", "WOW", "디아블로3", "블소", "던파", "마영전", "스타크래프트", "마구마구"};
	private String[] menuUrl= {"login", "mlist.php?id=rgr201311", "mlist.php?id=arthur", "mlist.php?id=rare",
			"mlist.php?id=100", "mlist.php?id=18news", "mlist.php?id=sports", "mlist.php?id=sports2",
			"mlist.php?id=loveqa1", "mlist.php?id=plaza", "yonye.html", "mlist.php?id=sgm1",
			"mlist.php?id=econo", "mlist.php?id=hideholic", "mlist.php?id=lux", "mlist.php?id=pcclub",
			"mlist.php?id=nkin", "mlist.php?id=ganzi", "mlist.php?id=music", "mlist.php?id=movie",
			"mlist.php?id=animation", "mlist.php?id=vehicle", "mlist.php?id=iphone", "mlist.php?id=lol",
			"mlist.php?id=multi2", "mlist.php?id=diablo", "mlist.php?id=blso", "mlist.php?id=df1",
			"mlist.php?id=ma0", "mlist.php?id=star2", "mlist.php?id=ma9"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		fontawesome = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
		list = new ArrayList<ListObject>();
		adapter = new ArrayAdapterList(this, R.layout.list_view, list);
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
		mPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				if(arg0==1&&login==1){
					TextView tv = (TextView)findViewById(R.id.title_bar_left_first_btn);
	    			tv.setText(R.string.fa_pencil);
	    			tv.setTextSize((float)(getResources().getDimension(R.dimen.titlebar_btn_size)));
	    			tv.setTypeface(fontawesome);
	    			tv.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							SlidingDrawer sl = (SlidingDrawer)findViewById(R.id.slide_comment);
							if(!sl.isOpened()){
								TextView tv = (TextView)findViewById(R.id.title_bar_left_sec_btn);
				    			tv.setText(R.string.fa_times);
				    			tv.setTextSize((float)(getResources().getDimension(R.dimen.titlebar_btn_size)));
				    			tv.setTypeface(fontawesome);
				    			tv.setOnClickListener(new OnClickListener(){

									@Override
									public void onClick(View v) {
										((TextView)v).setText("");
										((TextView)v).setTextSize((float)(getResources().getDimension(R.dimen.titlebar_btn_size)));
										((TextView)v).setTypeface(fontawesome);
										EditText et = (EditText)findViewById(R.id.slide_memo);
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
										SlidingDrawer sl = (SlidingDrawer)findViewById(R.id.slide_comment);
										sl.animateOpen();
									}
				    				
				    			});
								EditText et = (EditText)findViewById(R.id.slide_memo);
								et.requestFocus();
							}else{
								TextView tv = (TextView)findViewById(R.id.title_bar_left_sec_btn);
				    			tv.setText("");
				    			tv.setTextSize((float)(getResources().getDimension(R.dimen.titlebar_btn_size)));
				    			tv.setTypeface(fontawesome);
				    			tv.setOnClickListener(null);
								EditText et = (EditText)findViewById(R.id.slide_memo);
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
								ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
								data.add(new BasicNameValuePair("url", commentUrl));
								data.add(new BasicNameValuePair("Referer", "http://rgrong.kr/m/"+URL));
								data.add(new BasicNameValuePair("memo",et.getText().toString()));
							    List<NameValuePair> parameters;
								try {
									parameters = URLEncodedUtils.parse(new URI(URL), "EUC-KR");
									for (NameValuePair p : parameters) {
										String name = p.getName();
										String value = p.getValue();
										if(value==null)
											value="";
								        data.add(new BasicNameValuePair(name, value));
								    }
								} catch (URISyntaxException e) {
									
								}
								getHttpAsync(data);
								et.setText("");
							}
							sl.animateOpen();
						}
					});
				}else{
					TextView tv = (TextView)findViewById(R.id.title_bar_left_first_btn);
	    			tv.setText(R.string.fa_cog);
	    			tv.setTextSize((float)(getResources().getDimension(R.dimen.titlebar_btn_size)));
	    			tv.setTypeface(fontawesome);
	    			tv.setOnClickListener(new OnClickListener(){

	    				@Override
	    				public void onClick(View v) {
	    					Intent intent = new Intent(v.getContext(),LoginActivity.class);
	    					intent.putExtra("login", login);
	    					startActivityForResult(intent, LOGIN_ACTIVITY);
	    					mPager.setCurrentItem(0);
	    				}
	    				
	    			});
				}
			}
			
		});
		
		setSideMenu();
		
		//자동 로그인
		DatabaseHelper mDbHelper = new DatabaseHelper(this, "RGRONG", null, 1);
		SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
		Cursor cursor = mDb.query("login", new String[] { "id", "password" }, null, null, null, null, null);
		if(login==0&&cursor!=null&&cursor.getCount()>0){
			cursor.moveToFirst();
			Login login = new Login(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("password")));
			login.login(this);
		}
		
		TextView saveimg = (TextView)findViewById(R.id.save_img);
		saveimg.setTypeface(fontawesome);
		saveimg.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ImageView iv = (ImageView) findViewById(R.id.photoview);
				String url = (String) iv.getTag();
				AsyncSaveImage asi = new AsyncSaveImage();
				asi.execute(url);
				Toast.makeText(v.getContext(), "저장했습니다.", Toast.LENGTH_SHORT).show();
				v.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+Environment.getExternalStorageDirectory().toString()+"/RGR")));
			}
			
		});
		
		TextView shareimg = (TextView)findViewById(R.id.share_img);
		shareimg.setTypeface(fontawesome);
		
		Typeface heummpost = Typeface.createFromAsset( getAssets(), "heummpost-10th.ttf" );
		TextView tv = (TextView)findViewById(R.id.title_bar_text_menu);
		tv.setTextSize(getResources().getDimension(R.dimen.titlebar_text_size));
		tv.setText(menuText);
		tv.setTypeface(heummpost);
		tv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!isSync){
					firstNo = 0;
					lastNo = 99999999;
					list.clear();
					getHttpAsync(page=1);
				}
			}
			
		});
		tv = (TextView)findViewById(R.id.title_bar_menu_btn);
		tv.setTextSize((float)(getResources().getDimension(R.dimen.titlebar_btn_size)));
		tv.setTypeface(fontawesome);
		tv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
				mDrawerLayout.openDrawer(Gravity.LEFT);
			}
			
		});
		tv = (TextView)findViewById(R.id.title_bar_left_first_btn);
		tv.setTextSize((float)(getResources().getDimension(R.dimen.titlebar_btn_size)));
		tv.setTypeface(fontawesome);
		tv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),LoginActivity.class);
				intent.putExtra("login", login);
				startActivityForResult(intent, LOGIN_ACTIVITY);
				mPager.setCurrentItem(0);
			}
			
		});
	}
	
	@Override
	public void onBackPressed() {
		if(findViewById(R.id.linear_photo).getVisibility()==View.VISIBLE)
			findViewById(R.id.linear_photo).setVisibility(View.INVISIBLE);
		else if(mPager.getCurrentItem()==1)
			mPager.setCurrentItem(0);
		else if(mPager.getCurrentItem()==2){
			mPager.setCurrentItem(1);
		}else{
			if(exit){
				new AsyncHttp().seCookie("");
				finish();
				super.onBackPressed();
			}
			else{
				exit = true;
				Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
				Handler handler = new Handler();
				Runnable runnalbe = new Runnable(){

					@Override
					public void run() {
						exit = false;
					}
					
				};
				handler.postDelayed(runnalbe, 2000);
			}
		}
	}
	
	public void setSideMenu(){
		ObjectMenuData = new ArrayList<ObjectItem>();
        for(int i=0;i<menuName.length;i++)
        	ObjectMenuData.add(new ObjectItem(i,menuName[i],menuUrl[i]));

        // our adapter instance
        menuAdapter = new ArrayAdapterItem(this, R.layout.menu, ObjectMenuData);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView)findViewById(R.id.left_drawer);
        listViewItems.setAdapter(menuAdapter);
        listViewItems.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mPager.setCurrentItem(0);
				DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
				mDrawerLayout.closeDrawers();
				if(position==0&&login==0){
					Intent intent = new Intent(view.getContext(),LoginActivity.class);
					intent.putExtra("login", login);
					startActivityForResult(intent, LOGIN_ACTIVITY);
				}else if(position==0&&login==1){
					Intent intent = new Intent(view.getContext(),LoginActivity.class);
					intent.putExtra("login", login);
					startActivityForResult(intent, LOGIN_ACTIVITY);
				}else{
					TextView tv = (TextView)view.findViewById(R.id.url_menu);
					menu = tv.getText().toString();
					tv = (TextView)view.findViewById(R.id.menu_name);
					menuText = tv.getText().toString();
					tv = (TextView)findViewById(R.id.title_bar_text_menu);
					tv.setText(menuText);
					firstNo = 0;
					lastNo = 99999999;
					list.clear();
					getHttpAsync(page=1);
				}
			}
        });
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==LOGIN_ACTIVITY){
			if(resultCode==RESULT_OK){
				login = data.getIntExtra("login",0);
				String nickName = data.getStringExtra("nickName");
				menuName[0] =nickName;
				menuUrl[0] = "main";
				ObjectMenuData.remove(0);
				ObjectMenuData.add(0, new ObjectItem(0,menuName[0],menuUrl[0]));
				menuAdapter.notifyDataSetChanged();
			}
		}else{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@SuppressWarnings("unchecked")
	public void getHttpAsync(int page){
		if(!isSync){
			isSync = true;
			AsyncHttp http= new AsyncHttp();
			http.setAsyncResponse(this, true);
			String url = "http://rgrong.kr/m/"+menu+"&page="+String.valueOf(page);
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("url", url));
			http.execute(data);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getHttpAsync(String url){
		if(!isSync){
			isSync = true;
			AsyncHttp http= new AsyncHttp();
			http.setAsyncResponse(this, false);
			ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
			data.add(new BasicNameValuePair("url", url));
			http.execute(data);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void getHttpAsync(ArrayList<NameValuePair> data){
		if(!isSync){
			isSync = true;
			AsyncHttp http= new AsyncHttp();
			http.setAsyncResponse(this);
			http.execute(data);
		}
	}
	
	public void processFinish(Document doc){
		int firstTmp = 0;
		int lastTmp = 9999999;
		if(addPre){
			list.clear();
		}
		try{			 
			Elements elements = doc.select("span");
			Iterator<Element> it = elements.iterator();
			Element e = null;
			//int i=0;
			while(it.hasNext()){
				int index =0;
				String comment_num="";
				String title="";
				String nickname="";
				boolean isimg=false;
				String time="";
				String url="";
				e = it.next();
				if(e.getElementsByTag("b").first()!=null){
					e = e.getElementsByTag("b").first().getElementsByTag("font").first();
				}
				comment_num = (Html.fromHtml(e.html()).toString());
				e = it.next();
				Element e1 = e.getElementsByTag("a").first();
				url = e1.attr("href");
				index = url.indexOf("no=");
				index = Integer.parseInt(url.substring(index+3));
				title = Html.fromHtml(e1.html()).toString();
				if(e1.nextElementSibling()!=null){
					isimg = true;
				}
				e = it.next();
				String id = e.html();
				nickname = id.substring(0, id.indexOf("<"));
				e = it.next();
				e = it.next();
				e = e.getElementsByTag("font").first().getElementsByTag("b").first();
				time = e.html();
				if(index>firstTmp)
					firstTmp = index;
				if(index<lastTmp)
					lastTmp = index;
				if(addPre){
					list.add(new ListObject(index, comment_num, title, nickname, isimg, time, url));
					if(index>firstNo){
				//		list.add(i++, new ListObject(index, comment_num, title, nickname, isimg, time, url));
				//	}else{
				//		break;
					}
				}else if(addPost){
					if(index<lastNo){
						list.add(new ListObject(index, comment_num, title, nickname, isimg, time, url));
					}
				}else{
					list.add(new ListObject(index, comment_num, title, nickname, isimg, time, url));
				}
			}
		}catch(Exception e){
			Log.e("jsoup",e.toString());
		}
		
		adapter.notifyDataSetChanged(); 
		listViewItems.refreshDrawableState(); 
		listViewItems.invalidate();
		if(addPost)
			listViewItems.onLoadMoreComplete();
		else
			listViewItems.onRefreshComplete();
		
        listViewItems.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mPager.setCurrentItem(1);
				URL = list.get(position-1).url;
				getHttpAsync("http://rgrong.kr/m/"+URL);
			}
        });
        if(!addPost)
        	firstNo = firstTmp;
        if(!addPre)
        	lastNo = lastTmp;
		addPre = false;
		addPost = false;
		isSync = false;
	}
	
	public void contentsFinish(Document doc){
		try{
			Element e = doc.getElementById("ArticleVTitle");
			TextView tv = (TextView)findViewById(R.id.contentstitle);
			tv.setText(Html.fromHtml(e.getElementsByTag("font").first().html()));
			Elements elements = doc.getElementsByClass("box");
			tv = (TextView)findViewById(R.id.contentstime);
			//tv.setTypeface(font);
			tv = (TextView)findViewById(R.id.contentsuserid);
			if(elements.select("a").size()!=0)
				tv.setText(elements.select("a").first().html());
			else
				tv.setText("비공개");
			elements = doc.getElementsByTag("table");
			e = elements.get(5).getElementsByTag("td").first();
			tv = (TextView)findViewById(R.id.contents);
			tv.setText(Html.fromHtml(e.html()).toString().trim());
			Pattern p = Pattern.compile("\\d{4}\\.\\d{2}\\.\\d{2} (\\d{2}\\:\\d{2}\\:\\d{2})");
			Matcher m = p.matcher(doc.toString());
			if(m.find()){
				tv = (TextView)findViewById(R.id.contentstime);
				tv.setText(m.group(1));
			}
			Iterator<Element> it = doc.getElementsByAttributeValue("name", "zb_target_resize").iterator();
			LinearLayout cl = (LinearLayout)findViewById(R.id.contents_layout);
			int clnum = cl.getChildCount();
			if(clnum>0)
				cl.removeViews(0, cl.getChildCount()-1);
			int j=0;
			while(it.hasNext()){
				e = it.next();
				LinearLayout ll = (LinearLayout)getLayoutInflater().inflate(R.layout.linear_img, null);
				ImageView i = (ImageView)ll.findViewById(R.id.contents_img);
				i.setTag(e.attr("src"));
				if(e.attr("src").indexOf(".gif")==-1){
					HttpImage hi = new HttpImage(i, (ProgressBar)ll.findViewById(R.id.progressBar_img));
					hi.execute(e.attr("src"));
				}else{
					Ion.with(i).load(e.attr("src")).setCallback(new FutureCallback<ImageView>(){

						@Override
						public void onCompleted(Exception e, ImageView result) {
							LinearLayout ll = (LinearLayout)result.getParent();
							ll.findViewById(R.id.progressBar_img).setVisibility(View.GONE);
						}
						
					});
		    		i.setAdjustViewBounds(true);
				}
				i.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						FrameLayout ll = (FrameLayout) findViewById(R.id.linear_photo);
						ImageView iv = (ImageView) findViewById(R.id.photoview);
						iv.destroyDrawingCache();
						iv.refreshDrawableState();
						iv.setImageDrawable(((ImageView)v).getDrawable());
						iv.setTag(v.getTag());
						ll.setVisibility(View.VISIBLE);
					}
					
				});
				i.setOnLongClickListener(new OnLongClickListener(){

					@Override
					public boolean onLongClick(View v) {
						openContextMenu(v);
						return true;
					}});
				cl.addView(ll, j++);
				registerForContextMenu(cl);
			}
			int numtable = elements.size()-5-login;
			cl = (LinearLayout)findViewById(R.id.commentlist);
			clnum = cl.getChildCount();
			if(clnum>0)
				cl.removeViews(0, cl.getChildCount());
			int k=1;
			for(int i=7;i<numtable;i++){
				FrameLayout ll = (FrameLayout)getLayoutInflater().inflate(R.layout.linear_comment, null);
				TextView tv1 = (TextView)ll.findViewById(R.id.comment_num);
				tv1.setText(String.valueOf(k++));
				Element e1 = elements.get(i++);
				tv1 = (TextView)ll.findViewById(R.id.comment_userid);
				tv1.setText(e1.select("b").get(1).html());
				e1 = elements.get(i);
				tv1 = (TextView)ll.findViewById(R.id.comment_text);
				tv1.setText(Html.fromHtml(e1.select(".Apple-style-span").first().child(0).html()));
				cl.addView(ll);
			}
		}catch(Exception e){
			Log.e("error", e.toString());
		}
		findViewById(R.id.scrollcontents).setScrollY(0);
		findViewById(R.id.Frame_no_contents).setVisibility(View.GONE);
		isSync = false;
	}
	
	@Override
	public void loginFinish(Document doc) {
		isSync = false;
		if(login!=1){
			Pattern p = Pattern.compile("\\<b\\>(.+)\\<\\/b\\>\\<\\/a\\>\\<font color\\=\\\"eeeeee\\\"");
			Matcher m = p.matcher(doc.toString());
			if(m.find()){
				Toast.makeText(this, m.group(1)+" 님 오늘도 즐지롱~♡", Toast.LENGTH_SHORT).show();
				login = 1;
				menuName[0] = m.group(1)+" 님";
				menuUrl[0] = "main";
				ObjectMenuData.remove(0);
				ObjectMenuData.add(0, new ObjectItem(0,menuName[0],menuUrl[0]));
				menuAdapter.notifyDataSetChanged();
			}else{
				Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
			}
		}else{
			getHttpAsync("http://rgrong.kr/m/"+URL);
		}
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	String[] menuItems = {"저장","보기"};
		for (int i = 0; i<menuItems.length; i++)
    		menu.add(Menu.NONE, i, i, menuItems[i]);
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item){
		
		return true;
	}
	
	/**
	 * PagerAdapter 
	 */
	private class PagerAdapterClass extends PagerAdapter{
		private View initView = null;
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
    			if(init){
    				v = mInflater.inflate(R.layout.linear_list, null);
    				initView = v;
    				init =  false;
    				listViewItems = (PullAndLoadListView)v.findViewById(R.id.list_view);
    				listViewItems.setAdapter(adapter);
    				listViewItems.setOnRefreshListener(new OnRefreshListener() {

    					public void onRefresh() {
    						addPre = true;
    						getHttpAsync(page=1);
    					}
    				});
    				listViewItems.setOnLoadMoreListener(new OnLoadMoreListener() {
    					
    					public void onLoadMore() {
    						addPost = true;
							getHttpAsync(++page);
    					}
    				});

    				getHttpAsync(1);
    			}else{
    				v = initView;
    			}
    			
    		}else if(position==1) {
    			v = mInflater.inflate(R.layout.linear_content, null);
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
