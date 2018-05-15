package edu.rosehulman.fenogljc.mtgbazaar.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;

public class UserCard {

    private Card card;
    private String set;
    private int qty = 1;
    private float price = 0.0f;
    private String language = Constants.LANG_EN;
    private boolean foil = false;
    private String condition = Constants.COND_ARRAY[0];
    private String key;
    private String name;

    public UserCard () {}

    public UserCard(String cardName) {
        setName(cardName);
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

    @Exclude
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setValues(UserCard newUserCard) {
        setSet(newUserCard.getSet());
        setQty(newUserCard.getQty());
        setPrice(newUserCard.getPrice());
        setLanguage(newUserCard.getLanguage());
        setFoil(newUserCard.isFoil());
    }

    public void setCardFromName(final Callback callback) {
        //TODO: find card from name
        final UserCard userCard = this;
        FirebaseDatabase.getInstance().getReference().child(Constants.DB_CARDS_REF).child(userCard.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Card incomingCard = dataSnapshot.getValue(Card.class);
                incomingCard.setKey(dataSnapshot.getKey());
                userCard.setCard(incomingCard);
                if (userCard.getSet() == null) {
                    userCard.setSet(userCard.getCard().getSets().get(0));
                }
                Log.d(Constants.TAG, "onDataChange: " + getName());
                callback.onCardFound(userCard);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
