package com.mathilde.appmessage.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.Conversation;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author mathilde on 23/02/16.
 */
public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>{

    private SparseBooleanArray selectedItems;
    private List<Conversation> mConversationList;

    public ConversationAdapter(List<Conversation> conversationList) {
        this.selectedItems     = new SparseBooleanArray();
        this.mConversationList = conversationList;
    }

    public void removeData(int position) {
        mConversationList.remove(position);
        notifyItemRemoved(position);
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }


    public Conversation getItem(int position) {
        return mConversationList.get(position);
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
        Conversation c = mConversationList.get(position);
        if(c.getLastMessage() != null) {
            holder.lastMessageTv.setText(c.getLastMessage().getMessage());
        }
        holder.userTv.setText(c.getUser().getName());
        if (c.getUser().getPicture() != null) {
            holder.userIv.setImageBitmap((c.getUser().getPicture()));
        } else {
            holder.userIv.setImageResource(R.drawable.default_user);
        }
    }

    @Override
    public int getItemCount() {
        return mConversationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userTv;
        TextView lastMessageTv;
        CircleImageView userIv;

        public ViewHolder(View itemView) {
            super(itemView);
            userTv        = (TextView)itemView.findViewById(R.id.user_tv);
            userIv        = (CircleImageView)itemView.findViewById(R.id.user_iv);
            lastMessageTv = (TextView)itemView.findViewById(R.id.last_message_tv);
        }
    }
}
