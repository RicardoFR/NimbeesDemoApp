package com.nimbees.newdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimbees.newdemo.R;

import java.util.List;

/**
 * Created by ricardo on 25/03/15.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private List<DrawerObject> drawerList;

    public NavDrawerListAdapter(Context context, List<DrawerObject> drawerList){
        this.context = context;
        this.drawerList = drawerList;
    }

    @Override
    public int getCount() {
        return drawerList.size();
    }

    @Override
    public Object getItem(int position) {
        return drawerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.navDrawerImageView);
        TextView textView = (TextView) convertView.findViewById(R.id.navDrawerTextView);

        imageView.setImageResource(drawerList.get(position).getIcon());
        textView.setText(drawerList.get(position).getName());



        return convertView;
    }

}