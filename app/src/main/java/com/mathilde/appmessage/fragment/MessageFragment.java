package com.mathilde.appmessage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.adapter.MessageAdapter;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.Message_Table;
import com.mathilde.appmessage.bean.User;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {
    public static final String RECEIVER = "receiver";

    private User receiver;
    private List<Message> mMessageList;
    private RecyclerView.Adapter mAdapter;

    public static MessageFragment newInstance(User receiver) {
        Bundle b          = new Bundle();
        MessageFragment f = new MessageFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getbundle();
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        init(view);
        initList();

        return view;
    }

    private void initList() {
        mMessageList.addAll(SQLite.select()
                .from(Message.class)
                .where(Message_Table.receiver_id.eq(receiver.getId()))
                .or(Message_Table.sender_id.eq(receiver.getId()))
                .orderBy(Message_Table.date, true)
                .queryList());

        mAdapter.notifyDataSetChanged();
    }

    private void init(View v) {
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        v.findViewById(R.id.send_ib).setOnClickListener(this);

        mMessageEt   = (EditText)v.findViewById(R.id.message_et);
        mMessageList = new ArrayList<>();
        mAdapter     = new MessageAdapter(mMessageList);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.messages_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private EditText mMessageEt ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_ib:
                String message = mMessageEt.getText().toString();

                Message m = new Message();
                m.setDate(new Date());
                m.setReceiver(receiver);
                m.setMessage(message);

                mMessageList.add(m);
                mMessageEt.setText("");
                mAdapter.notifyDataSetChanged();

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(receiver.getNumber(), null, message, null, null);

                m.save();
                break;
        }
    }
}
