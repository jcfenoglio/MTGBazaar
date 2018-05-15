package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import java.util.List;
import java.util.Locale;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.DeckAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.models.Deck;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class DeckFragment extends Fragment implements Callback {

    private AlertDialog editCardDialog;
    private static final String ARG_DECK = "deck";
    private Deck mDeck;
    private DeckAdapter mAdapter;
    private List<String> mCardNameArray;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeckFragment() {
    }

    @SuppressWarnings("unused")
    public static DeckFragment newInstance(Deck deck) {
        DeckFragment fragment = new DeckFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DECK, deck);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDeck = getArguments().getParcelable(ARG_DECK);
        }

        mCardNameArray = Constants.getCardNames();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity context = (MainActivity) getContext();

        context.setTitle(mDeck.getName());

        View view = inflater.inflate(R.layout.fragment_deck, container, false);

        final AutoCompleteTextView autoComplete = view.findViewById(R.id.deck_card_search);
        ArrayAdapter myAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, mCardNameArray);
        autoComplete.setAdapter(myAdapter);

//        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });


        final RecyclerView recyclerView = view.findViewById(R.id.deck_card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference mUserData = context.getmUserData().child(Constants.DB_DECKS_REF).child(mDeck.getKey());

        mAdapter = new DeckAdapter(this, mUserData);
        recyclerView.setAdapter(mAdapter);

        Button addButton = view.findViewById(R.id.add_card_button);
        final Callback callback = this;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = autoComplete.getText().toString();
                UserCard uCard = new UserCard(cardName);
                uCard.setCardFromName(callback);

            }
        });

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.hide();

        return view;
    }

    @Override
    public void onEdit(UserCard userCard) {
        showEditCardDialog(userCard);
    }

    private void showEditCardDialog(final UserCard userCard) {
        Log.d(Constants.TAG, "showEditCardDialog: called");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(userCard.getName());

        View view = getLayoutInflater().inflate(R.layout.card_edit_popup, null, false);
        builder.setView(view);

        ImageButton deleteCardButton = view.findViewById(R.id.edit_delete_card_button);
        deleteCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(userCard);
            }
        });

        final EditText cardPriceText = view.findViewById(R.id.edit_card_price);
        cardPriceText.setText(String.format(Locale.getDefault(), "%.2f", userCard.getPrice()));

        final EditText cardQtyText = view.findViewById(R.id.edit_card_quantity);
        cardQtyText.setText(String.valueOf(userCard.getQty()));

        final CheckBox foilCheckBox = view.findViewById(R.id.edit_card_foil);
        foilCheckBox.setChecked(userCard.isFoil());

        final Spinner setSpinner = view.findViewById(R.id.edit_card_set);
        ArrayAdapter<String> setAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, userCard.getCard().getSets());
        setSpinner.setAdapter(setAdapter);

        final Spinner langSpinner = view.findViewById(R.id.edit_card_language);
        ArrayAdapter<String> langAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, userCard.getCard().getLanguages());
        langSpinner.setAdapter(langAdapter);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // make a new card
                UserCard newCard = new UserCard();

                // set all the info for that card
                newCard.setFoil(foilCheckBox.isChecked());
                newCard.setPrice(Float.parseFloat(cardPriceText.getText().toString()));
                newCard.setQty(Integer.parseInt(cardQtyText.getText().toString()));
                newCard.setSet(setSpinner.getSelectedItem().toString());
                newCard.setLanguage(langSpinner.getSelectedItem().toString());

                // update original card using that card
                mAdapter.update(userCard, newCard);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        editCardDialog = builder.create();
        editCardDialog.show();
    }

    private void showDeleteConfirmationDialog(final UserCard userCard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.delete_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.delete_confirmation_popup, null, false);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.remove(userCard);
                editCardDialog.cancel();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    @Override
    public void onCardFound(UserCard card) {
        Log.d(Constants.TAG, "onClick: " + card.getName());
        if(card.getName() != null) {
            mAdapter.add(card);
        }
    }
}