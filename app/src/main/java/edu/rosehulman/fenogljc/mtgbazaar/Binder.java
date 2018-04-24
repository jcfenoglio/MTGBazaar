package edu.rosehulman.fenogljc.mtgbazaar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Binder implements CardCollection, Parcelable {

    private String name;
    private ArrayList<Card> cards;

    public Binder(String name){
        this.name = name;
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

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ArrayList<Card> getCards() {
        return this.cards;
    }

    @Override
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
