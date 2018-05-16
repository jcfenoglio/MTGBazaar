package edu.rosehulman.fenogljc.mtgbazaar.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hamiltjc on 5/7/18.
 */

public class Trade implements Parcelable {
    private Map<String, UserCard> ownUserCards;
    private Map<String, UserCard> theirUserCards;
    private String name;
    private String key;

    public Trade() {
        // Empty constructor for Firebase
    }

    public Trade(String name) {
        this.name = name;
        ownUserCards = new HashMap<>();
        theirUserCards = new HashMap<>();
    }

    protected Trade(Parcel in) {
        in.readMap(ownUserCards, UserCard.class.getClassLoader());
        in.readMap(theirUserCards, UserCard.class.getClassLoader());
        name = in.readString();
        key = in.readString();
    }

    public static final Creator<Trade> CREATOR = new Creator<Trade>() {
        @Override
        public Trade createFromParcel(Parcel in) {
            return new Trade(in);
        }

        @Override
        public Trade[] newArray(int size) {
            return new Trade[size];
        }
    };

    public Map<String, UserCard> getOwnUserCards() {
        return ownUserCards;
    }

    public void setOwnUserCards(Map<String, UserCard> ownUserCards) {
        this.ownUserCards = ownUserCards;
    }

    public Map<String, UserCard> getTheirUserCards() {
        return theirUserCards;
    }

    public void setTheirUserCards(Map<String, UserCard> theirUserCards) {
        this.theirUserCards = theirUserCards;
    }

    public String getName() {
        return name;
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

    public void setValues(Trade updatedTrade) {
        ownUserCards = updatedTrade.getOwnUserCards();
        theirUserCards = updatedTrade.getTheirUserCards();
        name = updatedTrade.getName();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(ownUserCards);
        dest.writeMap(theirUserCards);
        dest.writeString(name);
        dest.writeString(key);
    }
}
