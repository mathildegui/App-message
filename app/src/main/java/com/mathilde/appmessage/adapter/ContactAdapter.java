package com.mathilde.appmessage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.User;

import java.util.List;

/**
 * @author mathilde on 09/02/16.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<User> mContactsList;

    public ContactAdapter(List<User> contacts) {
        mContactsList = contacts;
    }

    public User getItem(int position) {
        return mContactsList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_list_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextViewName.setText(mContactsList.get(position).getName());
        holder.mTextViewNumber.setText(mContactsList.get(position).getNumber());
        if(mContactsList.get(position).getPicture() != null) {
            holder.mImageViewPhoto.setImageBitmap(mContactsList.get(position).getPicture());
        } else {
            holder.mImageViewPhoto.setImageResource(R.drawable.default_user);
        }
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
    }

    public User removeItem(int position) {
        final User user = mContactsList.remove(position);
        notifyItemRemoved(position);
        return user;
    }

    public void addItem(int position, User user) {
        mContactsList.add(position, user);
        notifyItemInserted(position);
    }

    public void moveItem(int from, int to) {
        final User u = mContactsList.get(from);
        mContactsList.add(to, u);
        notifyItemMoved(from, to);
    }

    public void animateTo(List<User> list) {
        applyAndAnimateRemovals(list);
        applyAndAnimateAdditions(list);
        applyAndAnimateMovedItems(list);
    }

    private void applyAndAnimateRemovals(List<User> newList) {
        for (int i = mContactsList.size() - 1; i >= 0; i--) {
            final User model = mContactsList.get(i);
            if (!newList.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<User> newList) {
        for (int i = 0, count = newList.size(); i < count; i++) {
            final User model = newList.get(i);
            if (!mContactsList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<User> newList) {
        for (int toPosition = newList.size() - 1; toPosition >= 0; toPosition--) {
            final User model = newList.get(toPosition);
            final int fromPosition = mContactsList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewName;
        public TextView mTextViewNumber;
        public ImageView mImageViewPhoto;

        public ViewHolder(View v) {
            super(v);
            mTextViewName   = (TextView)v.findViewById(R.id.name);
            mTextViewNumber = (TextView)v.findViewById(R.id.number);
            mImageViewPhoto = (ImageView)v.findViewById(R.id.photo);
        }
    }

}
