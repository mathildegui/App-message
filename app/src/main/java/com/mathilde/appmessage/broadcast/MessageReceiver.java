package com.mathilde.appmessage.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.mathilde.appmessage.R;
import com.mathilde.appmessage.activity.MainActivity;
import com.mathilde.appmessage.bean.Conversation;
import com.mathilde.appmessage.bean.Conversation_Table;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.User;
import com.mathilde.appmessage.utils.DataManager;
import com.mathilde.appmessage.utils.QueryContact;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;

import java.io.InputStream;
import java.util.Date;

public class MessageReceiver extends BroadcastReceiver {

    private static final int SMS_ID = 0;

    private EventBus bus = EventBus.getDefault();

    public MessageReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        try {
            if (b != null) {
                final Object[] objs = (Object[]) b.get("pdus");

                if (objs != null) {
                    for (int i = 0; i < objs.length; i++) {
                        SmsMessage currentMessage;

                        if (Build.VERSION.SDK_INT >= 19) { //KITKAT
                            SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                            currentMessage = msgs[i];
                        } else {
                            currentMessage = SmsMessage.createFromPdu((byte[]) objs[i]);
                        }
                        String message = currentMessage.getDisplayMessageBody();
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();


                        //get the user _ID from contact list
                        Uri uri  = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
                        Cursor c = context
                                .getContentResolver()
                                .query(uri,
                                        new String[]{
                                                ContactsContract.PhoneLookup._ID,
                                                ContactsContract.Contacts.DISPLAY_NAME},
                                        null,
                                        null,
                                        null);

                        long contactId     = -1;
                        String contactName = null;
                        if (c != null) {
                            if (c.moveToFirst()) {
                                contactId   = c.getLong(c.getColumnIndex(ContactsContract.PhoneLookup._ID));
                                contactName = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                            }
                            c.close();
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
                                    int type  = cursorPhones.getInt(cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                    if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                                        if (index != -1) {
                                            phoneNumber = cursorPhones.getString(index);
                                            u = new User(contactId, contactName, phoneNumber, loadContactPhoto(context.getContentResolver(), String.valueOf(contactId)));
                                            u.save();
                                        }
                                    }
                                }
                                cursorPhones.close();
                            }
                        }
                        m.setDate(new Date());
                        m.setMessage(message);
                        m.setSender(u);
                        m.save();

                        DataManager.updateOrCreateConversation(u, m);

                        bus.post(m);

                        /* If this app is the default message one, then display the notification */
                        if(context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context))) {
                            if (u != null) {
                                sendNotification(m.getMessage(), u.getName(), context);
                            } else {
                                sendNotification(m.getMessage(), context.getString(R.string.new_message), context);
                            }
                        }
                        Log.i("SmsReceiver", "senderNum: " + m.getSender().getNumber() + "; message: " + message);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    private void sendNotification(String message, String from, Context c) {
        Intent intent = new Intent(c, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(c)
                .setSmallIcon(R.drawable.ic_add_white)
                .setContentTitle(from)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(SMS_ID, notificationBuilder.build());
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
