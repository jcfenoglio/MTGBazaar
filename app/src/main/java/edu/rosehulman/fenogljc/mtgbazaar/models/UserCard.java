package edu.rosehulman.fenogljc.mtgbazaar.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;

public class UserCard extends Card {

    private String set;
    private int qty = 1;
    private float price = 0.0f;
    private String language = Constants.LANG_EN;
    private boolean foil = false;
    private String key;
    private String name;

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

    public void setValues(Card newCard) {
        setSet(newCard.getSets().get(0));
        setQty(1);
        setPrice(0.0f);
        setLanguage(Constants.LANG_EN);
        setFoil(false);
        setName(newCard.getKey());
    }

    public static void findCardFromName(final String cardName, final Callback callback) {
        //TODO: find card from name
        final UserCard card = new UserCard();
        FirebaseDatabase.getInstance().getReference().child(Constants.DB_CARDS_REF).child(cardName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Card incomingCard = dataSnapshot.getValue(Card.class);
                incomingCard.setKey(dataSnapshot.getKey());
                card.setValues(incomingCard);
                Log.d(Constants.TAG, "onDataChange: " + card.getName());
                callback.onCardFound(card);
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
