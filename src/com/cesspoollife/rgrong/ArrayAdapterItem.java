package com.cesspoollife.rgrong;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterItem extends ArrayAdapter<ObjectItem> {

    Context mContext;
    int layoutResourceId;
    List<ObjectItem> data = null;

    public ArrayAdapterItem(Context mContext, int layoutResourceId, List<ObjectItem> data) {

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
        ObjectItem objectItem = data.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.menu_name);
        textViewItem.setText(objectItem.itemName);
        textViewItem.setTag(objectItem.itemId);
        textViewItem = (TextView) convertView.findViewById(R.id.url_menu);
        textViewItem.setText(objectItem.page);

        return convertView;

    }

}