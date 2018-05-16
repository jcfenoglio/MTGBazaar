package edu.rosehulman.fenogljc.mtgbazaar.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.Map;

public class Binder implements Parcelable {

    private Map<String, UserCard> cards;
    private String name;
    private String key;

    @SuppressWarnings("unused")
    public Binder() {}

    public Binder(String name){
        this.name = name;
    }

    protected Binder(Parcel in) {
        in.readMap(cards, UserCard.class.getClassLoader());
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

    public Map<String, UserCard> getCards() {
        return cards;
    }

    public void setCards(Map<String, UserCard> cards) {
        this.cards = cards;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(cards);
        dest.writeString(name);
        dest.writeString(key);
    }

    public void setValues(Binder updatedBinder) {
        setName(updatedBinder.getName());
        setKey(updatedBinder.getKey());
    }
}
