package com.mathilde.appmessage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.bean.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {

    //public static final String SENDER   = "sender";
    public static final String RECEIVER = "receiver";

    private User receiver;

    public static MessageFragment newInstance(User receiver) {
        Bundle b          = new Bundle();
        MessageFragment f = new MessageFragment();

        //b.putParcelable(SENDER, sender);
        b.putParcelable(RECEIVER, receiver);
        f.setArguments(b);
        return f;
    }

    public MessageFragment() {
        // Required empty public constructor
    }

    private void getbundle() {
        if(getArguments() != null) {
            receiver = getArguments().getParcelable(RECEIVER);
            Log.d("USER", receiver.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getbundle();
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

}
