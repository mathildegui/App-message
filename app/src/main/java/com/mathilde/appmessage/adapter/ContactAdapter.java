package com.mathilde.appmessage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mathilde on 09/02/16.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<User> mContactsList;
    private final LayoutInflater mInflater;

    public ContactAdapter(List<User> contacts, Context c) {
        mInflater     = LayoutInflater.from(c);
        mContactsList = contacts;
    }

    public User getItem(int position) {
        return mContactsList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = mInflater.inflate(R.layout.fragment_item_list_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User u = mContactsList.get(position);
        holder.mTextViewName.setText(u.getName());
        holder.mTextViewNumber.setText(u.getNumber());
        if (u.getPicture() != null) {
            holder.mImageViewPhoto.setImageBitmap(u.getPicture());
        } else {
            holder.mImageViewPhoto.setImageResource(R.drawable.default_user);
        }
    }

    @Override
    public int getItemCount() {
        return mContactsList.size();
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

    public void setFilter(List<User> users) {
        mContactsList = new ArrayList<>();
        mContactsList.addAll(users);
        notifyDataSetChanged();
    }

}
