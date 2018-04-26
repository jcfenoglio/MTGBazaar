package edu.rosehulman.fenogljc.mtgbazaar;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class User {
    public String username;
    public Map<String, Binder> binders;
    public Map<String, Deck> decks;
//    public Map<String, Trade> trades;
    public Map<String, Card> cards;

    @SuppressWarnings("unused")
    public User () {}

    public User(String name) {
        this.username = name;
        binders = new HashMap<>();
        decks = new HashMap<>();
//        trades = new HashMap<>();
        cards = new HashMap<>();
    }
}
