package org.projects.shoppinglist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by heunex on 2016. 05. 10..
 */
public class Product implements Parcelable {

    String name;
    int quantity;
    String volume;

    public Product(int quantity, String volume, String name)
    {
        this.name = name;
        this.quantity = quantity;
        this.volume = volume;
    }

    @Override
    public String toString() {
        return quantity+" "+volume+" "+name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeString(volume);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    // "De-parcel object
    public Product(Parcel in) {
        name = in.readString();
        quantity = in.readInt();
        volume = in.readString();
    }
}
