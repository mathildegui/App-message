package com.mathilde.appmessage.broadcast;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.mathilde.appmessage.bean.Conversation;
import com.mathilde.appmessage.bean.Conversation_Table;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.User;
import com.mathilde.appmessage.utils.Numbers;
import com.mathilde.appmessage.utils.QueryContact;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.Date;

public class MessageReceiver extends BroadcastReceiver {
    //final SmsManager sms = SmsManager.getDefault();
    private EventBus bus = EventBus.getDefault();

    public MessageReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        try {
            if (b != null) {
                final Object[] objs = (Object[]) b.get("pdus");

                for (int i = 0; i < objs.length; i++) {
                    SmsMessage currentMessage;

                    if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                        currentMessage = msgs[i];
                    } else {
                        //Object pdus[] = (Object[]) b.get("pdus");
                        currentMessage = SmsMessage.createFromPdu((byte[]) objs[i]);
                    }
                    String message = currentMessage.getDisplayMessageBody();
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();


                    //get the user _ID from contact list
                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                    Cursor c = context.getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup._ID,
                            ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);
                    long contactId = -1;
                    String contactName = null;
                    if (c != null) {
                        if (c.moveToFirst()) {
                            contactId = c.getLong(c.getColumnIndex(ContactsContract.PhoneLookup._ID));
                            contactName = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        }
                    }

                    Message m = new Message();
                    //Create the message in DB if the user exist
                    //else create the message and the user
                    User u = User.getUserByContactId(contactId);
                    if (u == null) {
                        Cursor cursorPhones = QueryContact.getInstance(context).getPhones(String.valueOf(contactId));
                        if (cursorPhones != null) {
                            while (cursorPhones.moveToNext()) {
                                int index = cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                int type = cursorPhones.getInt(cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                                    if (index != -1) {
                                        phoneNumber = cursorPhones.getString(index);
                                        u = new User(contactId, contactName, phoneNumber, loadContactPhoto(context.getContentResolver(), String.valueOf(contactId)));
                                        u.save();
                                    }
                                }
                            }
                            Log.d("THE ID ___", contactId + "");
                            cursorPhones.close();
                        }
                    }
                    m.setDate(new Date());
                    m.setMessage(message);
                    m.setSender(u);
                    m.save();

                    Conversation localC = SQLite.select().from(Conversation.class).where(Conversation_Table.user_id.eq(User.getUserByContactId(u.getContactId()).getId())).querySingle();
                    if(localC == null) {
                        Conversation conversation = new Conversation(u, m);
                        conversation.save();
                        m.associateConversation(conversation);
                    } else {
                        m.associateConversation(localC);
                        localC.setLastMessage(m);
                        localC.update();
                    }


                    bus.post(m);
                    Log.i("SmsReceiver", "senderNum: " + m.getSender().getNumber() + "; message: " + message);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }



    public Bitmap loadContactPhoto(ContentResolver cr, String id) {
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(id));
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        if (input == null) {
            return null;
        }
        return BitmapFactory.decodeStream(input);
    }
}
