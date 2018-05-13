package edu.rosehulman.fenogljc.mtgbazaar.models;

import com.google.firebase.database.Exclude;

import java.util.List;

/**
 * Created by hamiltjc on 5/7/18.
 */

public class Trade {
    private List<UserCard> ownUserCards;
    private List<UserCard> theirUserCards;
    private String tradeName;
    private String key;

    public Trade() {
        // Empty constructor for Firebase
    }

    public Trade(String tradeName, List<UserCard> offering, List<UserCard> offered) {
        this.tradeName = tradeName;
        ownUserCards = offering;
        theirUserCards = offered;
    }

    public List<UserCard> getOwnUserCards() {
        return ownUserCards;
    }

    public void setOwnUserCards(List<UserCard> ownUserCards) {
        this.ownUserCards = ownUserCards;
    }

    public List<UserCard> getTheirUserCards() {
        return theirUserCards;
    }

    public void setTheirUserCards(List<UserCard> theirUserCards) {
        this.theirUserCards = theirUserCards;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String name) {
        this.tradeName = name;
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
        tradeName = updatedTrade.getTradeName();
    }
}
