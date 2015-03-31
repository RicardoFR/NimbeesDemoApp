package com.nimbees.newdemo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimbees.newdemo.R;
import com.nimbees.newdemo.activity.NavigationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardo on 25/03/15.
 */



public class DrawerAdapter extends ArrayAdapter<DrawerObject>
{
    private final Context context;
    private final int layoutResourceId;
    private List<DrawerObject> drawerList;

    public DrawerAdapter(Context context, int layoutResourceId, List<DrawerObject> drawerList)
    {
        super(context, layoutResourceId, drawerList);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.drawerList = drawerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = ((NavigationActivity) context).getLayoutInflater();

        View v = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageView = (ImageView) v.findViewById(R.id.navDrawerImageView);
        TextView textView = (TextView) v.findViewById(R.id.navDrawerTextView);

        imageView.setImageResource(drawerList.get(position).getIcon());
        textView.setText(drawerList.get(position).getName());

        return v;
    }
}
