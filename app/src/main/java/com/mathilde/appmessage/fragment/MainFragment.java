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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
