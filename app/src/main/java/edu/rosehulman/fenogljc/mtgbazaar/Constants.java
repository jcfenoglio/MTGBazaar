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
    public static final String DB_TRADES_REF = "trades";
    public static final String DB_TRADE_LEFT = "ownUserCards";
    public static final String DB_TRADE_RIGHT = "theirUserCards";
    public static final String DB_NAME_REF = "name";
    
    private static boolean persistanceEnabled = false;


    public static final String LANG_EN = "English";

    public static final String[] COND_ARRAY = {"Near Mint", "Lightly Played", "Moderately Played", "Played", "Damaged"};

    private static String[] format_array = {"Standard", "Modern", "Legacy", "Vintage", "Pauper", "Commander"};
    public static final ArrayList<String> FORMAT_ARRAY = new ArrayList<>(Arrays.asList(format_array));

    public static final String TCGPLAYER_BEARER_TOKEN = "H_mieexY5sEtZh70XUm-VwQNLqwNe2RShoAQvInduyKPx4zhAopEdlpCObd2wFZCe7OSsGxN0Wzllo1DSNJR1JPEVegi0h94u_5wXIFCt6-xEQT4tR-QwVAhl0GrFrHN09AHmp3b93gIwvSTYQ2bx_j3a-WSHNzT5JvWjhSBUCPQ1lqUOARmq9d_Dr3pjtmLE6hfa6wBTPV_e3tpcZxGN4JK9iAsu1HBLHX_0zHC8XLKQI9GYUmaE0GZVEWU4_H4mutxgp4563Ktp16_7jPlk45HVmtfxxYn6uwC2Rt9EAajNo-k7TTwhrGyrdrS_akbGVbZtXY0ouZw-KbQ3nlE3VWY814";

    public static final String PREFS = "PREFS";
    public static final String TRADE_BINDER_STORAGE_ID = "TRADE_BINDER_STORAGE";

    private static List<String> CARD_NAMES;

    public static List<String> getCardNames() {
        return CARD_NAMES;
    }

    public static void setCardNames(List<String> cardNames) {
        CARD_NAMES = cardNames;
    }

    public static boolean getPersistanceEnabled() {
        return persistanceEnabled;
    }

    public static void setPersistanceEnabled(boolean persistanceEnabled) {
        Constants.persistanceEnabled = persistanceEnabled;
    }
}
