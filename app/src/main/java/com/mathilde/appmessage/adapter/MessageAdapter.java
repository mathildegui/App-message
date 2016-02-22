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

    List<Message> mMessageList;

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
        holder.messageTv.setText(m.getMessage());
        if(m.getSender() == null) {
            holder.messageTv.setBackgroundColor(Color.BLUE);
            holder.messageUserIv.setImageResource(R.drawable.default_user);
        } else {
            holder.messageTv.setBackgroundColor(Color.RED);
            holder.messageUserIv.setImageBitmap(m.getSender().getPicture());
            if(m.getSender().getPicture() != null)Log.d("Picture", m.getSender().getPicture().toString());
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTv;
        public CircleImageView messageUserIv;

        public ViewHolder(View v) {
            super(v);
            messageTv = (TextView)v.findViewById(R.id.message_tv);
            messageUserIv = (CircleImageView)v.findViewById(R.id.message_iv);
        }

    }
}
