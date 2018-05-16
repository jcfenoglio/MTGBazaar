package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.SharedPreferencesUtils;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.TradeAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.TradeAdapter.TradeCallback;
import edu.rosehulman.fenogljc.mtgbazaar.models.Trade;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

public class TradeFragment extends Fragment implements TradeAdapter.TradeCallback {

    private AlertDialog editCardDialog;
    private static final String ARG_TRADE = "trade";
    private static final String ARG_CARDS_FROM_BINDER = "cfb";
    private Trade mTrade;
    private TradeAdapter mLeftAdapter;
    private TradeAdapter mRightAdapter;
    private List<UserCard> cardsFromBinder;
    private List<String> mCardNameArray;

    public TradeFragment() {}

    @SuppressWarnings("unused")
    public static TradeFragment newInstance(Trade trade) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TRADE, trade);
        ArrayList<UserCard> cardsFB = new ArrayList<>();
        args.putParcelableArrayList(ARG_CARDS_FROM_BINDER, cardsFB);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTrade = getArguments().getParcelable(ARG_TRADE);
            cardsFromBinder = getArguments().getParcelableArrayList(ARG_CARDS_FROM_BINDER);
        }

        mCardNameArray = Constants.getCardNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final MainActivity context = (MainActivity) getContext();

        context.setTitle(mTrade.getName());

        final String binderKey = SharedPreferencesUtils.getTradeBinder(context);

        View view = inflater.inflate(R.layout.fragment_trade, container, false);

        final AutoCompleteTextView autoComplete = view.findViewById(R.id.card_search);
        final ArrayAdapter myAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, mCardNameArray);
        autoComplete.setAdapter(myAdapter);

//        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        final RecyclerView leftRecyclerView = view.findViewById(R.id.left_card_list);
        leftRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        final RecyclerView rightRecyclerView = view.findViewById(R.id.right_card_list);
        rightRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference mTradeData = context.getmUserData().child(Constants.DB_TRADES_REF).child(mTrade.getKey());

        mLeftAdapter = new TradeAdapter(this, mTradeData.child(Constants.DB_TRADE_LEFT), mTrade, Constants.DB_TRADE_LEFT);
        leftRecyclerView.setAdapter(mLeftAdapter);
        mRightAdapter = new TradeAdapter(this, mTradeData.child(Constants.DB_TRADE_RIGHT), mTrade, Constants.DB_TRADE_RIGHT);
        rightRecyclerView.setAdapter(mRightAdapter);

        final TradeCallback callback = this;

        Button addLeftButton = view.findViewById(R.id.add_card_left_button);
        addLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = autoComplete.getText().toString();
                // if the card exists in the binder
//                if (binderKey != null) {
//                    DatabaseReference binderRef = context.getmUserData().child(Constants.DB_BINDERS_REF).child(binderKey);
//                    final ArrayList<UserCard> cards = new ArrayList<>();
//                    Query cardsInBinder = binderRef.child(Constants.DB_CARDS_REF).orderByChild(Constants.DB_NAME_REF).equalTo(cardName);
//                    if (cardsInBinder != null) {
//                        cardsInBinder.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    cards.add(snapshot.getValue(UserCard.class));
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Log.e(Constants.TAG, databaseError.getMessage());
//                            }
//                        });
//                        binderSelectionDialog(cards);
//                    } else {
//                        // if no cards of that name are found
//                        UserCard uCard = new UserCard(cardName);
//                        uCard.setCardFromName(callback, Constants.DB_TRADE_LEFT);
//                    }
//                } else {
//                    // if no binder is set
                    UserCard uCard = new UserCard(cardName);
                    uCard.setCardFromName(callback, Constants.DB_TRADE_LEFT);
//                }
                autoComplete.setText("");
            }
        });

        Button addRightButton = view.findViewById(R.id.add_card_right_button);
        addRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = autoComplete.getText().toString();
                UserCard uCard = new UserCard(cardName);
                uCard.setCardFromName(callback, Constants.DB_TRADE_RIGHT);
                autoComplete.setText("");
            }
        });

        Button finishTradeButton = view.findViewById(R.id.accept_trade_button);
        finishTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: make it actually function

                context.onBackPressed();
            }
        });

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.hide();

        return view;
    }

    private void binderSelectionDialog(final ArrayList<UserCard> cards) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.binder_selection_dialog_title);

        final TradeCallback callback = this;
        String[] cardSelections = {};
        for (UserCard card : cards) {
            String temp = card.getSet() + " / " + card.getCondition() + " / " + card.getLanguage();
            cardSelections[cards.indexOf(card)] = temp;
        }
        builder.setItems(cardSelections, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserCard selectedCard = cards.get(which);
                cardsFromBinder.add(selectedCard);
                mLeftAdapter.add(selectedCard);
            }
        });

        builder.setNeutralButton("Don't Use Binder", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserCard uCard = new UserCard(cards.get(0).getName());
                uCard.setCardFromName(callback, Constants.DB_TRADE_LEFT);
            }
        });
        builder.create().show();
    }

    private void showEditCardDialog(final UserCard userCard, final String side) {
        Log.d(Constants.TAG, "showEditCardDialog: called");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(userCard.getName());

        View view = getLayoutInflater().inflate(R.layout.card_edit_popup, null, false);
        builder.setView(view);

        ImageButton deleteCardButton = view.findViewById(R.id.edit_delete_card_button);
        deleteCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(userCard, side);
            }
        });

        //TODO: add number found in binder
        final EditText cardPriceText = view.findViewById(R.id.edit_card_price);
        cardPriceText.setText(String.format(Locale.getDefault(), "%.2f", userCard.getPrice()));

        final EditText cardQtyText = view.findViewById(R.id.edit_card_quantity);
        cardQtyText.setText(String.valueOf(userCard.getQty()));

        final CheckBox foilCheckBox = view.findViewById(R.id.edit_card_foil);
        foilCheckBox.setChecked(userCard.isFoil());

        final Spinner setSpinner = view.findViewById(R.id.edit_card_set);
        ArrayAdapter<String> setAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, userCard.getCard().getSets());
        setSpinner.setAdapter(setAdapter);
        setSpinner.setSelection(userCard.getCard().getSets().indexOf(userCard.getSet()));

        final Spinner langSpinner = view.findViewById(R.id.edit_card_language);
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, userCard.getCard().getLanguages());
        langSpinner.setAdapter(langAdapter);
        langSpinner.setSelection(userCard.getCard().getLanguages().indexOf(userCard.getLanguage()));

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // make a new card
                final UserCard newCard = new UserCard(userCard.getCard());

                // set all the info for that card
                newCard.setFoil(foilCheckBox.isChecked());
                //newCard.setPrice(Float.parseFloat(cardPriceText.getText().toString()));
                newCard.setQty(Integer.parseInt(cardQtyText.getText().toString()));
                newCard.setSet(setSpinner.getSelectedItem().toString());
                newCard.setLanguage(langSpinner.getSelectedItem().toString());

                newCard.setPriceFromInfo(new Callback() {
                    @Override
                    public void onEdit(UserCard card) {

                    }

                    @Override
                    public void onCardFound(UserCard card) {
                        // update original card using that card
                        if (side.equals(Constants.DB_TRADE_LEFT)) {
                            mLeftAdapter.update(userCard, newCard);
                        } else if (side.equals(Constants.DB_TRADE_RIGHT)){
                            mRightAdapter.update(userCard, newCard);
                        }
                    }
                });

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        editCardDialog = builder.create();
        editCardDialog.show();
    }

    private void showDeleteConfirmationDialog(final UserCard userCard, final String side) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.delete_card_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.delete_confirmation_popup, null, false);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (side.equals(Constants.DB_TRADE_LEFT)) {
                    mLeftAdapter.remove(userCard);
                } else if (side.equals(Constants.DB_TRADE_RIGHT)) {
                    mRightAdapter.remove(userCard);
                }
                editCardDialog.cancel();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    @Override
    public void onEdit(UserCard card, String side) {
        showEditCardDialog(card, side);
    }

    @Override
    public void onCardFound(final UserCard card, final String side) {
        Log.d(Constants.TAG, "onClick: " + card.getName());
        if(card.getName() != null) {
            card.setPriceFromInfo(new Callback() {
                @Override
                public void onEdit(UserCard card) {

                }

                @Override
                public void onCardFound(UserCard not) {
                    if (side == Constants.DB_TRADE_LEFT) {
                        mLeftAdapter.add(card);
                    } else if (side.equals(Constants.DB_TRADE_RIGHT)) {
                        mRightAdapter.add(card);
                    }
                }
            });
        }
    }

    @Override
    public void onEdit(UserCard card) {

    }

    @Override
    public void onCardFound(UserCard card) {

    }
}
