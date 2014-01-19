package com.cesspoollife.rgrong;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterList extends ArrayAdapter<ListObject>{

    Context mContext;
    int layoutResourceId;
    List<ListObject> data = null;

    public ArrayAdapterList(Context mContext, int layoutResourceId, List<ListObject> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout. 
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        ListObject object = data.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(object.title);
        TextView list_url = (TextView) convertView.findViewById(R.id.list_url);
        list_url.setText(object.url);
        TextView ownerid = (TextView) convertView.findViewById(R.id.ownerid);
        ownerid.setText(object.nickname);
        TextView writedtime = (TextView) convertView.findViewById(R.id.writedtime);
        if(object.date!=null){
	        Date date = new Date();
	        Long diff =date.getTime()-object.date.getTime();
	        if(diff/1000<10){
	        	writedtime.setTextColor(parent.getResources().getColor(R.color.list_now));
	        	writedtime.setTextSize(parent.getResources().getDimension(R.dimen.list_now_text_size));
	        	writedtime.setText("now");
	        }
	        else if(diff/1000<60){
	        	writedtime.setTextColor(parent.getResources().getColor(R.color.list_sec));
	        	writedtime.setTextSize(parent.getResources().getDimension(R.dimen.list_gen_text_size));
	        	writedtime.setText(String.valueOf(diff/1000)+"초전");
	        }
	        else if(diff/60000<30){
	        	writedtime.setTextColor(parent.getResources().getColor(R.color.list_min));
	        	writedtime.setTextSize(parent.getResources().getDimension(R.dimen.list_gen_text_size));
	        	writedtime.setText(String.valueOf(diff/60000)+"분전");
	        }else{
	        	writedtime.setText(object.time);
	        }
        }
        else
        	writedtime.setText(object.time);
        if(!object.isimg){
        	convertView.findViewById(R.id.is_img).setVisibility(View.INVISIBLE);
        }else{
        	convertView.findViewById(R.id.is_img).setVisibility(View.VISIBLE);
        }
        
        TextView comment = (TextView) convertView.findViewById(R.id.countcomment);
        comment.setText(object.comment_num);
        comment.setGravity(Gravity.CENTER);
        int comment_num= Integer.parseInt(object.comment_num.length()!=0?object.comment_num.trim():"0");
        if(comment_num>=100){
        	comment.setTextSize(parent.getResources().getDimension(R.dimen.list_count_text_size_small));
        	comment.setTextColor(parent.getResources().getColor(R.color.comment_hundred));
        }else if(comment_num>=30){
        	comment.setTextSize(parent.getResources().getDimension(R.dimen.list_count_text_size));
        	comment.setTextColor(parent.getResources().getColor(R.color.comment_thirty));
        }
        else if(comment_num>=10){
        	comment.setTextSize(parent.getResources().getDimension(R.dimen.list_count_text_size));
        	comment.setTextColor(parent.getResources().getColor(R.color.comment_ten));
        }
        else if(comment_num>=5){
        	comment.setTextSize(parent.getResources().getDimension(R.dimen.list_count_text_size));
        	comment.setTextColor(parent.getResources().getColor(R.color.commnet_five));
        }
        else{
        	comment.setTextSize(parent.getResources().getDimension(R.dimen.list_count_text_size));
        	comment.setTextColor(parent.getResources().getColor(R.color.commnet_gen));
        }
        return convertView;

    }

}