package com.mathilde.appmessage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * @author mathilde on 22/02/16.
 */
public class Conversation extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    long id;
    @PrimaryKey
    User user1;
    @PrimaryKey
    User user2;
    @Column
    @ForeignKey(saveForeignKeyModel = false)
    List<Message> messages;


    protected Conversation(Parcel in) {
        id       = in.readLong();
        user1    = in.readParcelable(User.class.getClassLoader());
        user2    = in.readParcelable(User.class.getClassLoader());
        messages = in.createTypedArrayList(Message.CREATOR);
    }

    public static final Creator<Conversation> CREATOR = new Creator<Conversation>() {
        @Override
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        @Override
        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(user1, flags);
        dest.writeParcelable(user2, flags);
        dest.writeTypedList(messages);
    }
}
