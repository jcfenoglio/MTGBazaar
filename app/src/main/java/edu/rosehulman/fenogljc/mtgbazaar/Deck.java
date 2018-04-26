package edu.rosehulman.fenogljc.mtgbazaar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Deck implements Parcelable {
    private String name;
    private ArrayList<Card> cards;

    public Deck(String name) {
        this.name = name;
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

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
