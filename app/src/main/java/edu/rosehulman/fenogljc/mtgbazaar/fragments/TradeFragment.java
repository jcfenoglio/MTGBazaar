package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.TradeAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.models.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.models.Trade;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

public class TradeFragment extends Fragment implements Callback{

    private AlertDialog editCardDialog;
    private static final String ARG_TRADE = "trade";
    private Trade mTrade;
    private TradeAdapter mLeftAdapter;
    private TradeAdapter mRightAdapter;
    private List<String> mCardNameArray;

    public TradeFragment() {}

    @SuppressWarnings("unused")
    public static TradeFragment newInstance(Trade trade) {
        TradeFragment fragment = new TradeFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TRADE, trade);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTrade = getArguments().getParcelable(ARG_TRADE);
        }

        mCardNameArray = Constants.getCardNames();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity context = (MainActivity) getContext();

        context.setTitle(mTrade.getName());

        

        View view = inflater.inflate(R.layout.fragment_binder, container, false);

        final AutoCompleteTextView autoComplete = view.findViewById(R.id.binder_card_search);
        ArrayAdapter myAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, mCardNameArray);
        autoComplete.setAdapter(myAdapter);

//        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        final RecyclerView recyclerView = view.findViewById(R.id.binder_card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference mTradeData = context.getmUserData().child(Constants.DB_TRADES_REF).child(mTrade.getKey());

        mLeftAdapter = new TradeAdapter(this, mTradeData.child(Constants.DB_TRADE_LEFT), mTrade, Constants.DB_TRADE_LEFT);
        recyclerView.setAdapter(mLeftAdapter);
        mRightAdapter = new TradeAdapter(this, mTradeData.child(Constants.DB_TRADE_RIGHT), mTrade, Constants.DB_TRADE_RIGHT);
        recyclerView.setAdapter(mRightAdapter);

        Button addLeftButton = view.findViewById(R.id.add_card_left_button);
        final Callback callback = this;
        addLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = autoComplete.getText().toString();

                // if the card exists in the binder
                if () {

                }
                UserCard uCard = new UserCard(cardName);
                uCard.setCardFromName(callback);
                autoComplete.setText("");
            }
        });

        Button addRightButton = view.findViewById(R.id.add_card_right_button);
        addRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = autoComplete.getText().toString();
                UserCard uCard = new UserCard(cardName);
                uCard.setCardFromName(callback);
                autoComplete.setText("");
            }
        });

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.hide();

        return view;
    }

    @Override
    public void onEdit(UserCard card) {

    }

    @Override
    public void onCardFound(UserCard card) {

    }
}
