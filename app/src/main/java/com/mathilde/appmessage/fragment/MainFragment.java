package com.mathilde.appmessage.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Message m1 = new Message();
        Message m2 = new Message();

        m1.setMessage("mon message 1");
        m2.setMessage("mon message 2");

        m1.setReceiver(new User());
        m2.setReceiver(new User());
        m1.setSender(new User());
        m2.setSender(new User());
        m1.save();
        m2.save();
        return inflater.inflate(R.layout.fragment_main, container, false);


    }
}
