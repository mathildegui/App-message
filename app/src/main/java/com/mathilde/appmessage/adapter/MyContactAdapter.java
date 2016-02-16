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
public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.ViewHolder>{

    private List<User> mContactsList;

    public MyContactAdapter (List<User> contacts) {
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
