package com.mathilde.appmessage.adapter;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author mathilde on 17/02/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    //private long oldId = -2;
    private List<Message> mMessageList;

    public MessageAdapter(List<Message> messages) {
        mMessageList = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_item_list_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message m = mMessageList.get(position);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)holder.messageTv.getLayoutParams();
        LinearLayout.LayoutParams lpIV = (LinearLayout.LayoutParams)holder.messageIv.getLayoutParams();

        if (m.getSender() == null) {
            holder.messageTv.setText(m.getMessage());
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageTv.setBackgroundColor(Color.BLUE);
            lp.gravity   = Gravity.RIGHT;
            lpIV.gravity = Gravity.RIGHT;
            holder.messageIv.setImageResource(R.drawable.default_user);

            //Because getSender == null
            /*long currentId = -1;
            if (oldId != currentId) {
                holder.messageUserIv.setVisibility(View.VISIBLE);
                if (m.getReceiver().getPicture() != null) {
                    holder.messageUserIv.setImageBitmap((m.getReceiver().getPicture()));
                } else {
                    holder.messageUserIv.setImageResource(R.drawable.default_user);
                }
                Log.d("Display for me", "OLD: " + oldId + " - " + " NEW:" + currentId + " - POSITION: " + position);
            } else {
                holder.messageUserIv.setVisibility(View.GONE);
            }
            oldId = -1;*/
        } else {
            holder.messageTv.setText(m.getMessage());
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageTv.setBackgroundColor(Color.RED);
            lp.gravity   = Gravity.LEFT;
            lpIV.gravity = Gravity.LEFT;
            if (m.getSender().getPicture() != null) {
                holder.messageIv.setImageBitmap((m.getSender().getPicture()));
            } else {
                holder.messageIv.setImageResource(R.drawable.default_user);
            }
            /*if (m.getSender().getPicture() != null) {
                holder.messageIv.setImageBitmap(m.getSender().getPicture());
            } else {
                holder.messageIv.setImageResource(R.drawable.default_user);
            }*/
           /* if (oldId != m.getSender().getContactId()) {
                holder.messageUserIv.setVisibility(View.VISIBLE);
                if (m.getSender().getPicture() != null) {
                    holder.messageUserIv.setImageBitmap(m.getSender().getPicture());
                } else {
                    holder.messageUserIv.setImageResource(R.drawable.default_user);
                }
                Log.d("Display for me", "OLD: " + oldId + " - " + " NEW:" + m.getSender().getContactId() + " - POSITION: " + position);
            }
            else {
                holder.messageUserIv.setVisibility(View.GONE);
            }
            oldId = m.getSender().getContactId();*/
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTv;
        public CircleImageView messageIv;

        public ViewHolder(View v) {
            super(v);
            messageTv = (TextView)v.findViewById(R.id.message_tv);
            messageIv = (CircleImageView)v.findViewById(R.id.message_iv);
        }
    }
}
