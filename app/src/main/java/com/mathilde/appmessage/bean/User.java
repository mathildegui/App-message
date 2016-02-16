package com.mathilde.appmessage.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author mathilde on 09/02/16.
 */
@ModelContainer
@Table(database = Database.class)
public class User extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    long id;
    @Column
    String name;
    @Column
    String number;
    Bitmap picture;

    public User() {
    }

    public User(String name, String number, Bitmap picture) {
        this.name    = name;
        this.number  = number;
        this.picture = picture;
    }

    protected User(Parcel in) {
        name    = in.readString();
        number  = in.readString();
        picture = in.readParcelable(Bitmap.class.getClassLoader());
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
}
