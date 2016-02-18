package com.mathilde.appmessage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.adapter.MessageAdapter;
import com.mathilde.appmessage.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    //public static final String SENDER   = "sender";
    public static final String RECEIVER = "receiver";

    private List<String> mMessageList;
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        init(view);
        return view;
    }
    RecyclerView mRecyclerView;
    private void init(View v) {
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);


        v.findViewById(R.id.send_ib).setOnClickListener(this);
        et = (EditText)v.findViewById(R.id.message_et);
        mMessageList = new ArrayList<>();

        mRecyclerView = (RecyclerView)v.findViewById(R.id.messages_rv);

        RecyclerView.Adapter mAdapter             = new MessageAdapter(mMessageList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    EditText et ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_ib:
                mMessageList.add(String.valueOf(et.getText()));

                mRecyclerView.getAdapter().notifyDataSetChanged();
                Log.d("Le message", et.getText().toString());
                SmsManager smsManager = SmsManager.getDefault();
// Send a text based SMS
                smsManager.sendTextMessage(receiver.getNumber(), null, String.valueOf(et.getText()), null, null);
                et.setText("");
                break;
        }
    }
}
