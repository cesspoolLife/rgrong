package com.cesspoollife.rgrong;

import java.util.Date;

public class ListObject {
	public int index;
	public String comment_num;
	public String title;
	public String nickname;
	public boolean isimg;
	public String time;
	public String url;
	public Date date;
	
	ListObject(int i, String c, String t, String n, boolean img, String w, String u ){
		index = i;
		comment_num = c;
		title = t;
		nickname = n;
		isimg = img;
		time = w;
		url = u;
		date = new Date();
		if(time.indexOf("now")!=-1){
		}else if(time.indexOf("초전")!=-1){
			Long sec = Long.parseLong(time.substring(0, time.indexOf("초전")));
			date.setTime(date.getTime()-sec*1000);
		}else if(time.indexOf("분전")!=-1){
			Long min = Long.parseLong(time.substring(0, time.indexOf("분전")));
			date.setTime(date.getTime()-min*60*1000);
		}else{
			date = null;
		}
	}
}
