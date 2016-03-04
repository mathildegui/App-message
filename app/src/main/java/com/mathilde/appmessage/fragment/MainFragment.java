package com.mathilde.appmessage.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.mathilde.appmessage.utils.DataManager;
import com.mathilde.appmessage.utils.RecyclerItemClickListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements ActionMode.Callback {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private ActionMode actionMode;
    private ConversationAdapter mAdapter;
    private List<Conversation> mConversationList;
    private OnListFragmentInteractionListener mListener;

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        getActivity().findViewById(R.id.fab).setVisibility(View.VISIBLE);

        // Register as a subscriber
        if(!EventBus.getDefault().isRegistered(this))EventBus.getDefault().register(this);

        mConversationList = SQLite.select().from(Conversation.class).groupBy(Conversation_Table.user_id).queryList();
        RecyclerView recyclerView = (RecyclerView)v.findViewById(R.id.conversations_rv);
        mAdapter = new ConversationAdapter(mConversationList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                mListener.onListFragmentInteraction(mAdapter.getItem(position).getUser());
            }

            @Override
            public void onItemLongPress(View childView, int position) {
                if (actionMode != null) {
                    return;
                }
                // Start the CAB using the ActionMode.Callback defined above
                actionMode = getActivity().startActionMode(MainFragment.this);
                mAdapter.toggleSelection(position);
            }
        }));
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_context_action, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(
            ActionMode actionMode,
            MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_delete:
                List<Integer> selectedItemPositions = mAdapter.getSelectedItems();
                for (int i = selectedItemPositions.size()-1; i >= 0; i--) {
                    DataManager.deleteAllMessages(mAdapter.getItem(i));
                    mAdapter.removeData(selectedItemPositions.get(i));
                }
                actionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        mAdapter.clearSelections();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(User user);
    }
}
