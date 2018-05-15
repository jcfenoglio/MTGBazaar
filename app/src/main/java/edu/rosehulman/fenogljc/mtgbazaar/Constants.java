package edu.rosehulman.fenogljc.mtgbazaar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final String TAG = "MTGBAZAAR";
    public static final String DB_BINDERS_REF = "binders";
    public static final String DB_DECKS_REF = "decks";
    public static final String DB_USERS_REF = "users";
    public static final String DB_CARDS_REF = "cards";

    public static final String LANG_EN = "English";

    public static final String[] COND_ARRAY = {"Near Mint", "Lightly Played", "Moderately Played", "Played", "Damaged"};
    private static String[] format_array = {"Standard", "Modern", "Legacy", "Vintage", "Pauper", "Commander"};
    public static final ArrayList<String> FORMAT_ARRAY = new ArrayList<>(Arrays.asList(format_array));

    private static List<String> CARD_NAMES;

    public static List<String> getCardNames() {
        return CARD_NAMES;
    }

    public static void setCardNames(List<String> cardNames) {
        CARD_NAMES = cardNames;
    }
}
