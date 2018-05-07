package edu.rosehulman.fenogljc.mtgbazaar;

import com.google.firebase.database.Exclude;

public class Card {

    private String name;
    private String set;
    private int qty;
    private float price;
    private String key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setValues(Card newCard) {
        this.name = newCard.getName();
        this.set = newCard.getSet();
        this.qty = newCard.getQty();
        this.price = newCard.getPrice();
    }
}
