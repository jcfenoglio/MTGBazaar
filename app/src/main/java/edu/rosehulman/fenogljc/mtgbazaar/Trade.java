package edu.rosehulman.fenogljc.mtgbazaar;

import com.google.firebase.database.Exclude;

import java.util.List;

/**
 * Created by hamiltjc on 5/7/18.
 */

public class Trade {
    private List<Card> ownCards;
    private List<Card> theirCards;
    private String tradeName;
    private String key;

    public Trade() {
        // Empty constructor for Firebase
    }

    public Trade(String tradeName, List<Card> offering, List<Card> offered) {
        this.tradeName = tradeName;
        ownCards = offering;
        theirCards = offered;
    }

    public List<Card> getOwnCards() {
        return ownCards;
    }

    public void setOwnCards(List<Card> ownCards) {
        this.ownCards = ownCards;
    }

    public List<Card> getTheirCards() {
        return theirCards;
    }

    public void setTheirCards(List<Card> theirCards) {
        this.theirCards = theirCards;
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
        ownCards = updatedTrade.getOwnCards();
        theirCards = updatedTrade.getTheirCards();
        tradeName = updatedTrade.getTradeName();
    }
}
