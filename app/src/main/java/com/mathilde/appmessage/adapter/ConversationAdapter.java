package com.mathilde.appmessage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.Conversation;

import java.util.List;

/**
 * @author mathilde on 23/02/16.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>{

    private List<Conversation> mConversationList;

    public ConversationAdapter(List<Conversation> conversationList) {
        this.mConversationList = conversationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_list_conversation, parent, false);
        return new ViewHolder(v);
    }

    public void updateList(List<Conversation> newList) {
        mConversationList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mConversationList.get(position).getLastMessage() != null) {
            holder.conversationTv.setText(mConversationList.get(position).getLastMessage().getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mConversationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView conversationTv;

        public ViewHolder(View itemView) {
            super(itemView);
            conversationTv = (TextView)itemView.findViewById(R.id.conversation_tv);
        }
    }
}
