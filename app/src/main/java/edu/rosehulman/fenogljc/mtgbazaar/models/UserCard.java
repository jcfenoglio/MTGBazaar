package edu.rosehulman.fenogljc.mtgbazaar.models;

import com.google.firebase.database.Exclude;

import edu.rosehulman.fenogljc.mtgbazaar.Constants;

public class UserCard extends Card {

    private String set;
    private int qty = 1;
    private float price = 0.0f;
    private String language = Constants.LANG_EN;
    private boolean foil = false;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isFoil() {
        return foil;
    }

    public void setFoil(boolean foil) {
        this.foil = foil;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(UserCard newUserCard) {
        setSet(newUserCard.getSet());
        setQty(newUserCard.getQty());
        setPrice(newUserCard.getPrice());
        setLanguage(newUserCard.getLanguage());
        setFoil(newUserCard.isFoil());
    }
}
