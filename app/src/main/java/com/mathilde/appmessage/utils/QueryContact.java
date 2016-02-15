package com.mathilde.appmessage.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

/**
 * @author mathilde on 15/02/16.
 */
public class QueryContact {

    private static ContentResolver cr     = null;
    private static QueryContact mInstance = null;

    public QueryContact(Context c) {
        cr = c.getContentResolver();
    }

    public static QueryContact getInstance(Context c) {
        if(mInstance == null)
        {
            mInstance = new QueryContact(c);
        }
        return mInstance;
    }

    public Cursor getPhones(String contactId) {
        return cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                null,
                null);
    }

    public Cursor getContacts(@Nullable String[] projection,
                               @Nullable String selection, @Nullable String[] selectionArgs,
                               @Nullable String sortOrder) {
        return cr.query(ContactsContract.Contacts.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

}
