package edu.rosehulman.fenogljc.mtgbazaar.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Deck implements Parcelable {
    private String name;
    private ArrayList<UserCard> userCards;
    private String key;

    public Deck() {

    }

    protected Deck(Parcel in) {
        name = in.readString();
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<UserCard> getUserCards() {
        return this.userCards;
    }

    public void addCards(ArrayList<UserCard> userCards) {
        this.userCards.addAll(userCards);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Deck newDeck) {
        this.userCards = newDeck.getUserCards();
        this.name = newDeck.getName();
    }
}
