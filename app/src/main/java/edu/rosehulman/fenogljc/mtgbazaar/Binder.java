package edu.rosehulman.fenogljc.mtgbazaar;

import java.util.ArrayList;
import java.util.List;

public class Binder implements CardCollection {

    private String name;
    private ArrayList<Card> cards;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
