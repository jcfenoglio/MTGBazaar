package edu.rosehulman.fenogljc.mtgbazaar.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Binder implements Parcelable {

    private String name;
    private String key;
    private HashMap<String, UserCard> cards;

    @SuppressWarnings("unused")
    public Binder() {}

    public Binder(String name){
        this.name = name;
        this.cards = new HashMap<>();
    }

    protected Binder(Parcel in) {
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<Binder> CREATOR = new Creator<Binder>() {
        @Override
        public Binder createFromParcel(Parcel in) {
            return new Binder(in);
        }

        @Override
        public Binder[] newArray(int size) {
            return new Binder[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashMap<String, UserCard> getCards() {
        return this.cards;
    }

    public void addCards(HashMap<String, UserCard> cards) { this.cards.putAll(cards); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(key);
    }

    public void setValues(Binder updatedBinder) {
        setName(updatedBinder.getName());
        setKey(updatedBinder.getKey());
    }
}
