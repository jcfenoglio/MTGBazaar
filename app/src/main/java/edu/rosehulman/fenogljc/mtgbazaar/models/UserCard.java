package edu.rosehulman.fenogljc.mtgbazaar.models;

import com.google.firebase.database.Exclude;

public class UserCard extends Card {

    private String set;
    private int qty;
    private float price;
    private String key;

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public int getQty() { return qty; }

    public void setQty(int qty) { this.qty = qty; }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(UserCard newUserCard) {
        this.set = newUserCard.getSet();
        this.qty = newUserCard.getQty();
        this.price = newUserCard.getPrice();
    }
}
