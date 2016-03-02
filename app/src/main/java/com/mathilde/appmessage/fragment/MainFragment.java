package com.mathilde.appmessage.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.adapter.ConversationAdapter;
import com.mathilde.appmessage.adapter.MessageAdapter;
import com.mathilde.appmessage.bean.Conversation;
import com.mathilde.appmessage.bean.Conversation_Table;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.Message_Table;
import com.mathilde.appmessage.bean.User;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private ConversationAdapter mAdapter;
    private List<Conversation> mConversationList;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);

        // Register as a subscriber
        if(!EventBus.getDefault().isRegistered(this))EventBus.getDefault().register(this);

        /**
         * FIXME :: CLEAN THIS SHIT
         */
        mConversationList = SQLite.select().from(Conversation.class).queryList();


        RecyclerView rv = (RecyclerView)v.findViewById(R.id.conversations_rv);
        mAdapter = new ConversationAdapter(mConversationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(mAdapter);
        return v;
    }

    @Subscribe
    public void onEvent(Message message) {
        Conversation conversation = message.getConversationForeignKeyContainer().load();

        if(!mConversationList.contains(conversation)) {
            mConversationList.add(conversation);
        } else {
            mConversationList.set(mConversationList.indexOf(conversation), conversation);
        }

        mAdapter.notifyDataSetChanged();
    }
}
