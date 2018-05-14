package edu.rosehulman.fenogljc.mtgbazaar.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.models.Card;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment {


    public CardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layoutView = inflater.inflate(R.layout.fragment_card, container, false);

        final MainActivity context = (MainActivity) getContext();

        context.setTitle(getString(R.string.nav_item_search));

        layoutView.findViewById(R.id.pricing_layout).setVisibility(View.INVISIBLE);
        layoutView.findViewById(R.id.card_info_layout).setVisibility(View.INVISIBLE);
        layoutView.findViewById(R.id.card_text).setVisibility(View.INVISIBLE);

        final AutoCompleteTextView autoComplete = layoutView.findViewById(R.id.card_search);

        ArrayAdapter myAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, Constants.getCardNames());
        autoComplete.setAdapter(myAdapter);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputManager = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(autoComplete.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String cardName = autoComplete.getText().toString();
                FirebaseDatabase.getInstance().getReference().child(Constants.DB_CARDS_REF).child(cardName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Card incomingCard = dataSnapshot.getValue(Card.class);
                        incomingCard.setKey(dataSnapshot.getKey());
                        populateCardInfo(layoutView, incomingCard);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.hide();

        return layoutView;
    }

    private void populateCardInfo(View layoutView, Card incomingCard) {
        TextView cardName = layoutView.findViewById(R.id.card_name);
        TextView cardType = layoutView.findViewById(R.id.card_type);
        TextView cardManaCost = layoutView.findViewById(R.id.card_mana_cost);
        TextView cardText = layoutView.findViewById(R.id.card_text);
        Spinner setSelector = layoutView.findViewById(R.id.pricing_set);
        TextView regPrice = layoutView.findViewById(R.id.regular_price);
        TextView foilPrice = layoutView.findViewById(R.id.foil_price);

        cardName.setText(incomingCard.getKey());
        cardType.setText(incomingCard.getType().toString() + " - " + incomingCard.getSubtype().toString());
        cardManaCost.setText(incomingCard.getManaCost());
        cardText.setText(incomingCard.getText());

        //TODO: FILL SELECTOR AND PRICES
        ArrayAdapter<String> setAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, incomingCard.getSets());
        setSelector.setAdapter(setAdapter);

        regPrice.setText(String.format(Locale.getDefault(), "%.2f", 0.00f));
        foilPrice.setText(String.format(Locale.getDefault(), "%.2f", 0.00f));

        layoutView.findViewById(R.id.pricing_layout).setVisibility(View.VISIBLE);
        layoutView.findViewById(R.id.card_info_layout).setVisibility(View.VISIBLE);
        layoutView.findViewById(R.id.card_text).setVisibility(View.VISIBLE);
    }
}
