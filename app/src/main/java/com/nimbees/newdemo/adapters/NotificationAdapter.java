package com.nimbees.newdemo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimbees.newdemo.R;
import com.nimbees.platform.beans.NimbeesMessage;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * This adapter works with the RecyclerView of notifications to show each element in the UI
 */


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NimbeesMessage> mMessageList;

    public NotificationAdapter(List<NimbeesMessage> mMessageList) {
        this.mMessageList = mMessageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder NotificationViewHolder, int i) {

        NimbeesMessage nimbeesMessage = mMessageList.get(i);
        String date = new SimpleDateFormat("dd-MM-yyyy   HH:mm").format(nimbeesMessage.getTimestamp());

        if (nimbeesMessage.getIdBeacon() == null) {
            NotificationViewHolder.vImageType.setImageResource(R.drawable.ic_sms_white_36dp);
        } else {
            NotificationViewHolder.vImageType.setImageResource(R.drawable.ic_track_changes_white_36dp);
        }

        if (nimbeesMessage.getDisplayed()) {
            NotificationViewHolder.vDisplayed.setImageResource(R.drawable.ic_visibility_white_36dp);
        } else {
            NotificationViewHolder.vDisplayed.setImageResource(R.drawable.ic_visibility_off_white_36dp);
        }
        NotificationViewHolder.vDate.setText(date);
        NotificationViewHolder.vContent.setText(nimbeesMessage.getContent().toString());
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);

        return new NotificationViewHolder(itemView);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView vDate;
        TextView vContent;
        ImageView vImageType;
        ImageView vDisplayed;

        public NotificationViewHolder(View v) {
            super(v);

            vDate = (TextView) v.findViewById(R.id.message_date);
            vContent = (TextView) v.findViewById(R.id.message_content);
            vImageType = (ImageView) v.findViewById(R.id.notification_type);
            vDisplayed = (ImageView) v.findViewById(R.id.message_displayed);

        }
    }
}
