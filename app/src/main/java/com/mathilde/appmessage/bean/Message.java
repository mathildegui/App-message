package com.mathilde.appmessage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.Date;

/**
 * @author mathilde on 16/02/16.
 */
@ModelContainer
@Table(database = Database.class)
public class Message extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    Date date;
    @Column
    @ForeignKey(saveForeignKeyModel = false)
    User sender;
    @Column
    @ForeignKey(saveForeignKeyModel = false)
    User receiver;
    @Column
    String message;
    @ForeignKey(saveForeignKeyModel = false)
    ForeignKeyContainer<Conversation> conversationForeignKeyContainer;

    /**
     * Example of setting the model for the queen.
     */
    public void associateConversation(Conversation c) {
        conversationForeignKeyContainer = FlowManager.getContainerAdapter(Conversation.class).toForeignKeyContainer(c);
    }

    public Message() {

    }

    public ForeignKeyContainer<Conversation> getConversationForeignKeyContainer() {
        return conversationForeignKeyContainer;
    }

    protected Message(Parcel in) {
        id       = in.readLong();
        date     = new Date (in.readLong());
        sender   = in.readParcelable(User.class.getClassLoader());
        receiver = in.readParcelable(User.class.getClassLoader());
        message  = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(date.getTime());
        dest.writeParcelable(sender, flags);
        dest.writeParcelable(receiver, flags);
        dest.writeString(message);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", message='" + message + '\'' +
                '}';
    }
}
