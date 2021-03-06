package com.mathilde.appmessage.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.adapter.MessageAdapter;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.Message_Table;
import com.mathilde.appmessage.bean.User;
import com.mathilde.appmessage.utils.DataManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements View.OnClickListener {
    public static final String RECEIVER = "receiver";

    private User receiver;
    private EditText mMessageEt;
    private RecyclerView recyclerView;
    private List<Message> mMessageList;
    private RecyclerView.Adapter mAdapter;

    private EventBus bus = EventBus.getDefault();

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

        setHasOptionsMenu(true);

        // Register as a subscriber
        bus.register(this);

        init(view);
        initList();

        return view;
    }

    @Subscribe
    public void onEvent(Message message) {
        mMessageList.add(message);
        mAdapter.notifyItemInserted(mMessageList.size() - 1);
        scrollBottom();
    }

    private void initList() {
        mMessageList.addAll(SQLite.select()
                .from(Message.class)
                .where(Message_Table.receiver_id.eq(receiver.getId()))
                .or(Message_Table.sender_id.eq(receiver.getId()))
                .orderBy(Message_Table.date, true)
                .queryList());

        mAdapter.notifyDataSetChanged();
        scrollBottom();
    }

    private void scrollBottom() {
        if(mMessageList.size() > 0) {
            recyclerView.smoothScrollToPosition(mMessageList.size() - 1);
        }
    }

    private void init(View v) {
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);
        v.findViewById(R.id.send_ib).setOnClickListener(this);

        mMessageList = new ArrayList<>();
        mMessageEt   = (EditText)v.findViewById(R.id.message_et);
        mAdapter     = new MessageAdapter(mMessageList, getContext());

        recyclerView = (RecyclerView) v.findViewById(R.id.messages_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(mLayoutManager);
    }

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
                mAdapter.notifyItemInserted(mMessageList.size() - 1);
                m.save();

                DataManager.updateOrCreateConversation(receiver, m);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(receiver.getNumber(), null, message, null, null);
                scrollBottom();

                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_message, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call:
                Log.d("ACTION", receiver.getNumber());
                break;
        }
        return true;
    }
}
