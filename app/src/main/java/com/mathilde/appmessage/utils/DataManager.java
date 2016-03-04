package com.mathilde.appmessage.utils;

import com.mathilde.appmessage.bean.Conversation;
import com.mathilde.appmessage.bean.Conversation_Table;
import com.mathilde.appmessage.bean.Message;
import com.mathilde.appmessage.bean.Message_Table;
import com.mathilde.appmessage.bean.User;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * @author mathilde on 04/03/16.
 */
public class DataManager {

    /**
     *
     * @param c
     */
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

    /**
     *
     * @param u
     * @param m
     */
    public static void updateOrCreateConversation(User u, Message m) {
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
    }
}
