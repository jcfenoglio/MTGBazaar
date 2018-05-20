package edu.rosehulman.fenogljc.mtgbazaar;

import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

public interface Callback {
    void onEdit(UserCard card);
    void onCardFound(UserCard card);
}
