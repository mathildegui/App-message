package com.mathilde.appmessage.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author mathilde on 09/02/16.
 */
@ModelContainer
@Table(database = Database.class)
public class User extends BaseModel implements Parcelable {

    private static int increment = 0;

    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    @Unique(unique = true)
    long contactId;
    @Column
    String name;
    @Column
    String number;
    @Column(typeConverter = BitmapConverter.class)
    Bitmap picture;

    public User() {

    }

    public long getIdByUserId(long theid) {
        if(theid == contactId) return id;
        else return -1;
    }

    public User(long contactId, String name, String number, Bitmap picture) {
        this.id        = ++increment;
        this.name      = name;
        this.number    = number;
        this.picture   = picture;
        this.contactId = contactId;
    }

    protected User(Parcel in) {
        id        = in.readLong();
        contactId = in.readLong();
        name      = in.readString();
        number    = in.readString();
        picture   = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(contactId);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeValue(picture);
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public long getContactId() {
        return contactId;
    }

    @Override
    public String toString() {
        return "User{" +
                "contactId=" + contactId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", picture=" + picture +
                '}';
    }

    public static User getUserByContactId(long id) {
        return SQLite.select().from(User.class).where(User_Table.contactId.eq(id)).querySingle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (getContactId() != user.getContactId()) return false;
        return getName().equals(user.getName());

    }

    @Override
    public int hashCode() {
        int result = (int) (getContactId() ^ (getContactId() >>> 32));
        result = 31 * result + getName().hashCode();
        return result;
    }
}
