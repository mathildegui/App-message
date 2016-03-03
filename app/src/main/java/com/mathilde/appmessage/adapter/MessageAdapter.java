package com.mathilde.appmessage.adapter;

import android.app.ActionBar;
import android.content.Context;
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
import android.widget.RelativeLayout;
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

    private Context mContext;
    private List<Message> mMessageList;

    public MessageAdapter(List<Message> messages, Context c) {
        mContext     = c;
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
        final Message m   = mMessageList.get(position);
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixelsSize    = (int) (30 * scale + 0.5f);
        int pixelsMargin  = (int) (5 * scale + 0.5f);

        RelativeLayout.LayoutParams paramsIV = new RelativeLayout.LayoutParams(
                pixelsSize, pixelsSize);
        RelativeLayout.LayoutParams paramsTV = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        holder.messageTv.setText(m.getMessage());
        if (m.getSender() == null) {
            holder.messageIv.setImageResource(R.drawable.default_user);

            paramsIV.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.messageIv.setLayoutParams(paramsIV);

            paramsTV.addRule(RelativeLayout.LEFT_OF, holder.messageIv.getId());
            paramsTV.rightMargin = pixelsMargin;
            holder.messageTv.setLayoutParams(paramsTV);
        } else {
            paramsIV.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.messageIv.setLayoutParams(paramsIV);

            paramsTV.addRule(RelativeLayout.RIGHT_OF, holder.messageIv.getId());
            paramsTV.leftMargin = pixelsMargin;
            holder.messageTv.setLayoutParams(paramsTV);

            if (m.getSender().getPicture() != null) {
                holder.messageIv.setImageBitmap((m.getSender().getPicture()));
            } else {
                holder.messageIv.setImageResource(R.drawable.default_user);
            }
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
