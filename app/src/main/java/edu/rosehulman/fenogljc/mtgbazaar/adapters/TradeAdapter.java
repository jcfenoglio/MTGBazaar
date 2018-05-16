package edu.rosehulman.fenogljc.mtgbazaar.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.models.Trade;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.ViewHolder> {

        private Trade mTrade;
        private String mSide;
        private List<UserCard> mCards;
        private DatabaseReference mRef;
        private TradeCallback mCallback;

        public TradeAdapter(TradeCallback callback, DatabaseReference ref, Trade trade, String side) {
            mTrade = trade;
            mSide = side;
            mCards = new ArrayList<>();
            mRef = ref.child(Constants.DB_CARDS_REF).child(side);
            mRef.addChildEventListener(new TradeChildEventListener());
            mCallback = callback;
        }

        public void remove(UserCard userCard) {
            mRef.child(userCard.getKey()).removeValue();
        }

        public void add(UserCard userCard) {
            mRef.push().setValue(userCard);
        }

        public void update(UserCard userCard, UserCard newCard) {
            userCard.setValues(newCard);
            mRef.child(userCard.getKey()).setValue(userCard);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trade_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            UserCard userCard = mCards.get(position);
            holder.mCardNameView.setText(userCard.getName());
            holder.mCardAmountView.setText(String.valueOf(userCard.getQty()));
            holder.mCardSetView.setText(userCard.getSet());
            holder.mCardPriceView.setText(String.format(Locale.getDefault(), "%.2f", userCard.getPrice()));
        }

        @Override
        public int getItemCount() {
            return mCards.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View mView;
        public TextView mCardNameView;
        public TextView mCardAmountView;
        public TextView mCardPriceView;
        public TextView mCardSetView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCardNameView = view.findViewById(R.id.trade_card_name);
            mCardAmountView = view.findViewById(R.id.trade_card_amount);
            mCardPriceView = view.findViewById(R.id.trade_price);
            mCardSetView = view.findViewById(R.id.trade_set_name);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserCard userCard = mCards.get(getAdapterPosition());
            mCallback.onEdit(userCard, mSide);
        }
    }

    public interface TradeCallback extends Callback{
        void onEdit(UserCard card, String side);
        void onCardFound(UserCard card, String side);
    }

    private class TradeChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            UserCard userCard = dataSnapshot.getValue(UserCard.class);
            userCard.setKey(dataSnapshot.getKey());
            userCard.setCardFromName(new Callback() {
                @Override
                public void onEdit(UserCard card) {

                }

                @Override
                public void onCardFound(UserCard card) {
                    if (mSide == Constants.DB_TRADE_LEFT) {
                        mTrade.getOwnUserCards().put(card.getKey(), card);
                    }else if (mSide == Constants.DB_TRADE_RIGHT) {
                        mTrade.getTheirUserCards().put(card.getKey(), card);
                    }
                    mCards.add(card);
                    notifyDataSetChanged();
                }
            });
        }


        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            UserCard updatedTrade = dataSnapshot.getValue(UserCard.class);
            for (UserCard c : mCards) {
                if (c.getKey().equals(key)) {
                    c.setValues(updatedTrade);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (UserCard c : mCards) {
                if (c.getKey().equals(key)) {
                    if (mSide == Constants.DB_TRADE_LEFT) {
                        mTrade.getOwnUserCards().put(c.getKey(), c);
                    }else if (mSide == Constants.DB_TRADE_RIGHT) {
                        mTrade.getTheirUserCards().put(c.getKey(), c);
                    }
                    mCards.remove(c);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(Constants.TAG, databaseError.getMessage());
        }
    }
}

