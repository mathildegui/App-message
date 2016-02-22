package com.mathilde.appmessage.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.Message;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author mathilde on 17/02/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private long oldId = -2;
    private List<Message> mMessageList;

    public MessageAdapter(List<Message> messages) {
        mMessageList = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_list_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message m = mMessageList.get(position);

        if(m.getSender() == null) {
            holder.messageTvR.setText(m.getMessage());
            holder.messageTv.setVisibility(View.GONE);
            holder.messageTvR.setVisibility(View.VISIBLE);
            holder.messageTvR.setBackgroundColor(Color.BLUE);

            //Because getSender == null
            long currentId = -1;
            if(oldId != currentId) {
                holder.messageUserIvR.setVisibility(View.VISIBLE);
                holder.messageUserIvR.setImageResource(R.drawable.default_user);
            } else {
                holder.messageUserIvR.setVisibility(View.GONE);
            }
            holder.messageUserIv.setVisibility(View.GONE);
            oldId = -1;
        } else {
            holder.messageTv.setText(m.getMessage());
            holder.messageTvR.setVisibility(View.GONE);
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageTv.setBackgroundColor(Color.RED);

            if(oldId != m.getSender().getContactId()) {
                holder.messageUserIv.setVisibility(View.VISIBLE);
                holder.messageUserIv.setImageBitmap(m.getSender().getPicture());
            } else {
                holder.messageUserIv.setVisibility(View.GONE);
            }
            holder.messageUserIvR.setVisibility(View.GONE);
            oldId = m.getSender().getContactId();
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTv;
        public TextView messageTvR;
        public CircleImageView messageUserIv;
        public CircleImageView messageUserIvR;

        public ViewHolder(View v) {
            super(v);
            messageTv     = (TextView)v.findViewById(R.id.message_tv);
            messageTvR     = (TextView)v.findViewById(R.id.message_tv_r);
            messageUserIv = (CircleImageView)v.findViewById(R.id.message_iv);
            messageUserIvR = (CircleImageView)v.findViewById(R.id.message_iv_r);
        }
    }
}
