package edu.rosehulman.fenogljc.mtgbazaar;

import java.util.ArrayList;

public class Binder implements CardCollection {

    private String name;
    private ArrayList<Card> cards;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public ArrayList<Card> getCards() {
        return this.cards;
    }

    @Override
    public void addCards(ArrayList<Card> cards) {
        this.cards.addAll(cards);
    }
}
