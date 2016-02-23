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

import java.util.ArrayList;
import java.util.List;

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

        /**
         * FIXME :: CLEAN THIS SHIT
         */
        List<Conversation> listC = SQLite.select().from(Conversation.class).queryList();
        //List<Message> list = SQLite.select().from(Message.class).where(Message_Table.sender_id.isNotNull()).groupBy(Message_Table.sender_id).queryList();
        /*List<String> ls = new ArrayList<>();
        for(Message m : list){
            ls.add(m.getMessage());
        }*/

        Log.d("My list", listC.toString());
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.conversations_rv);
        RecyclerView.Adapter a = new ConversationAdapter(listC);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(a);
        return v;
    }
}
