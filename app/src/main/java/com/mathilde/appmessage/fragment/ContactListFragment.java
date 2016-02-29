package com.mathilde.appmessage.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.adapter.ContactAdapter;
import com.mathilde.appmessage.bean.Conversation;
import com.mathilde.appmessage.bean.Conversation_Table;
import com.mathilde.appmessage.bean.User;
import com.mathilde.appmessage.bean.User_Table;
import com.mathilde.appmessage.utils.QueryContact;
import com.mathilde.appmessage.utils.RecyclerItemClickListener;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment implements SearchView.OnQueryTextListener{

    private List<User> mList;
    private ContactAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private OnListFragmentInteractionListener mListener;

    private static final int LIMIT                = 20;
    private static final String SELECTION         = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
    private static final String SORT_ORDER_FIRST  = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC LIMIT " + LIMIT;
    private static final String SORT_ORDER_OFFSET = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC LIMIT -1 OFFSET " + LIMIT;
    private static final String[] PROJECTION      = new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
    };

    public ContactListFragment() {

    }

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    public Bitmap loadContactPhoto(ContentResolver cr, String id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_contact, container, false);
        setHasOptionsMenu(true);
        init(view);
        getContacts(SORT_ORDER_FIRST, true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<User> filter(List<User> users, String query) {
        query = query.toLowerCase();

        final List<User> filteredUserList = new ArrayList<>();
        for (User u : users) {
            final String text = u.getName().toLowerCase();
            if (text.contains(query)) {
                filteredUserList.add(u);
            }
        }
        return filteredUserList;
    }


    @Override
    public boolean onQueryTextChange(String query) {
        final List<User> filteredModelList = filter(mList, query);
        mAdapter.setFilter(filteredModelList);
        return true;
    }

    private class GetContactsAsync extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            return getContacts(SORT_ORDER_OFFSET, false);
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            mList.addAll(users);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void init(View v) {
        getActivity().findViewById(R.id.fab).setVisibility(View.GONE);

        mList         = new ArrayList<>();
        mAdapter      = new ContactAdapter(mList, getActivity());
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Create user only if it is not already in the DB - else use the local one
                if (SQLite.select().from(User.class).where(User_Table.contactId.eq(((ContactAdapter) mRecyclerView.getAdapter()).getItem(position).getContactId())).querySingle() != null) {
                    mListener.onListFragmentInteraction(SQLite.select().from(User.class).where(User_Table.contactId.eq(((ContactAdapter) mRecyclerView.getAdapter()).getItem(position).getContactId())).querySingle());
                } else {
                    mListener.onListFragmentInteraction(((ContactAdapter) mRecyclerView.getAdapter()).getItem(position));
                }
                try {
                    ((ContactAdapter) mRecyclerView.getAdapter()).getItem(position).save();
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    private List<User> getContacts(String args, boolean isFirst) {
        Cursor cursorContact = QueryContact.getInstance(getActivity()).getContacts(PROJECTION, SELECTION, null, args);
        if(cursorContact != null) {
            while (cursorContact.moveToNext()) {
                long contactId     = cursorContact.getLong(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //  Get all phone numbers.
                Cursor cursorPhones = QueryContact.getInstance(getActivity()).getPhones(String.valueOf(contactId));

                String number;
                if (cursorPhones != null) {
                    while (cursorPhones.moveToNext()) {
                        int index = cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int type  = cursorPhones.getInt(cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        if(type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            if (index != -1) {
                                number = cursorPhones.getString(index);
                                mList.add(new User(contactId, contactName, number, loadContactPhoto(getActivity().getContentResolver(), String.valueOf(contactId))));
                            }
                        }
                    }
                    cursorPhones.close();
                }
            }
            cursorContact.close();
        }
        if(isFirst) {
            new GetContactsAsync().execute();
        }
        return mList;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(User user);
    }
}
