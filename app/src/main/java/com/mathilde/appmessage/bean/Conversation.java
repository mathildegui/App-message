package com.mathilde.appmessage.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mathilde on 22/02/16.
 */
@ModelContainer
@Table(database = Database.class)
public class Conversation extends BaseModel implements Parcelable {

    private static int increment = 0;

    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    @Unique(unique = true)
    @ForeignKey(saveForeignKeyModel = false)
    User user;
   /* @Column
    @ForeignKey(saveForeignKeyModel = true)
    ForeignKeyContainer<Message> messages;*/

    public Conversation() {

    }

    public Conversation(User u, Message m) {
        this.id       = ++increment;
        this.user     = u;
        this.messages = new ArrayList<>();
        this.messages.add(m);
    }

    List<Message> messages;

    protected Conversation(Parcel in) {
        id       = in.readLong();
        user     = in.readParcelable(User.class.getClassLoader());
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

    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "messages")
    public List<Message> getMyAnts() {
        /*if (messages == null || messages.isEmpty()) {
            messages = SQLite.select()
                    .from(Message.class)
                    .where(Message.conversationForeignKeyContainer_id.eq(id))
                    .queryList();
        }*/
        return messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(user, flags);
        dest.writeTypedList(messages);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", user=" + user +
                ", messages=" + messages +
                '}';
    }
}
