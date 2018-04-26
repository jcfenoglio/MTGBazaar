package edu.rosehulman.fenogljc.mtgbazaar;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

@IgnoreExtraProperties
public class Binder implements Parcelable {

    private String name;
    private HashMap<String, Card> cards;

    @SuppressWarnings("unused")
    public Binder() {}

    public Binder(String name){
        this.name = name;
        this.cards = new HashMap<>();
    }

    protected Binder(Parcel in) {
        name = in.readString();
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

    public HashMap<String, Card> getCards() {
        return this.cards;
    }

    public void addCards(HashMap<String, Card> cards) { this.cards.putAll(cards); }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
