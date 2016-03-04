package com.mathilde.appmessage.utils;

import com.mathilde.appmessage.bean.Conversation;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.Message_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * @author mathilde on 04/03/16.
 */
public class DataManager {

    public static void deleteAllMessages(Conversation c) {
        List<Message> messages = SQLite.select()
                .from(Message.class)
                .where(Message_Table.receiver_id.eq(c.getUser().getId()))
                .or(Message_Table.sender_id.eq(c.getUser().getId()))
                .orderBy(Message_Table.date, true)
                .queryList();
        for(Message m : messages) {
            m.delete();
        }
        c.delete();
    }
}
