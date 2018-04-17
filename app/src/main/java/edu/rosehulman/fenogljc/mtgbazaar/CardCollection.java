package edu.rosehulman.fenogljc.mtgbazaar;

import java.util.ArrayList;

public interface CardCollection {
    public String getName();
    public void setName(String name);
    public ArrayList<Card> getCards();
    public void addCards(ArrayList<Card> cards);
}
