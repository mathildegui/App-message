package com.mathilde.appmessage.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author mathilde on 09/02/16.
 */
public class User implements Parcelable {

    public String id;
    public String name;
    public String number;
    public Bitmap picture;

    public User(String name, String number, Bitmap picture) {
        this.name   = name;
        this.number = number;
        this.picture = picture;
    }

    public User(String name, String number) {
        this.name   = name;
        this.number = number;
    }

    protected User(Parcel in) {
        name   = in.readString();
        number = in.readString();
        //picture = Bitmap.CREATOR.createFromParcel(in);
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
}
