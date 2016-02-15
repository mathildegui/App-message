package com.mathilde.appmessage.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.adapter.MyContactAdapter;
import com.mathilde.appmessage.bean.User;
import com.mathilde.appmessage.utils.QueryContact;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {

    private List<User> mList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private OnListFragmentInteractionListener mListener;

    private static final String SELECTION    = ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '1'";
    private static final String SORT_ORDER   = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
    private static final String[] PROJECTION = new String[] {
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


        init(view);
        new GetContactsAsync().execute();

        return view;
    }

    private class GetContactsAsync extends AsyncTask<Void, Void, List<User>> {


        @Override
        protected List<User> doInBackground(Void... params) {
            return getContacts();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            mList = users;

            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void init(View v) {
        Context context = v.getContext();

        mList           = new ArrayList<>();
        mProgressBar    = (ProgressBar)v.findViewById(R.id.progress_bar);
        mRecyclerView   = (RecyclerView)v.findViewById(R.id.recycler_view);

        RecyclerView.Adapter mAdapter             = new MyContactAdapter(mList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private List<User> getContacts() {
        Cursor cursorContact = QueryContact.getInstance(getActivity()).getContacts(PROJECTION, SELECTION, null, SORT_ORDER);
        if(cursorContact != null) {
            while (cursorContact.moveToNext()) {
                String contactId   = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursorContact.getString(cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                //  Get all phone numbers.
                Cursor cursorPhones = QueryContact.getInstance(getActivity()).getPhones(contactId);

                String number = null;
                if (cursorPhones != null) {
                    while (cursorPhones.moveToNext()) {
                        int index = cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int type  = cursorPhones.getInt(cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        if(type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            if (index != -1) {
                                number = cursorPhones.getString(index);
                            }
                        }
                    }
                    mList.add(new User(contactName, number, loadContactPhoto(getActivity().getContentResolver(), contactId)));
                    cursorPhones.close();
                }
            }
            cursorContact.close();
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
        void onListFragmentInteraction();
    }
}
