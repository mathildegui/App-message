package com.mathilde.appmessage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathilde.appmessage.R;

import java.util.List;

/**
 * @author mathilde on 17/02/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    List<String> mMessageList;

    public MessageAdapter(List<String> messages) {
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
        holder.messageTv.setText(mMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTv;
        public ViewHolder(View v) {
            super(v);
            messageTv = (TextView)v.findViewById(R.id.message_tv);
        }

    }
}
