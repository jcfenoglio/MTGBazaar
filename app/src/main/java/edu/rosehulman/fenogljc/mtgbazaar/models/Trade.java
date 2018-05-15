package edu.rosehulman.fenogljc.mtgbazaar.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamiltjc on 5/7/18.
 */

public class Trade {
    private List<String> ownUserCards;
    private List<String> theirUserCards;
    private String name;
    private String key;

    public Trade() {
        // Empty constructor for Firebase
    }

    public Trade(String name) {
        this.name = name;
        ownUserCards = new ArrayList<>();
        theirUserCards = new ArrayList<>();
    }

    public List<String> getOwnUserCards() {
        return ownUserCards;
    }

    public void setOwnUserCards(List<String> ownUserCards) {
        this.ownUserCards = ownUserCards;
    }

    public List<String> getTheirUserCards() {
        return theirUserCards;
    }

    public void setTheirUserCards(List<String> theirUserCards) {
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
}
