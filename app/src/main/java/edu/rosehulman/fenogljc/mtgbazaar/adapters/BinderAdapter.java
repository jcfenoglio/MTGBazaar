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
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UserCard}
 * TODO: Replace the implementation with code for your data type.
 */
public class BinderAdapter extends RecyclerView.Adapter<BinderAdapter.ViewHolder> {

    private List<UserCard> mUserCards;
    private DatabaseReference mRef;
    private Callback mCallback;

    public BinderAdapter(Callback callback, DatabaseReference ref) {
        mUserCards = new ArrayList<>();
        mRef = ref.child(Constants.DB_CARDS_REF);
        mRef.addChildEventListener(new BinderChildEventListener());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_binder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserCard userCard = mUserCards.get(position);
        holder.mCardNameView.setText(userCard.getName());
        holder.mCardAmountView.setText(String.valueOf(userCard.getQty()));
        holder.mCardSetView.setText(userCard.getSet());
        holder.mCardPriceView.setText(String.format(Locale.getDefault(), "%.2f", userCard.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mUserCards.size();
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
            mCardNameView = view.findViewById(R.id.binder_card_name);
            mCardAmountView = view.findViewById(R.id.binder_card_amount);
            mCardPriceView = view.findViewById(R.id.binder_card_price);
            mCardSetView = view.findViewById(R.id.binder_card_set);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserCard userCard = mUserCards.get(getAdapterPosition());
            mCallback.onEdit(userCard);
        }
    }

    private class BinderChildEventListener implements ChildEventListener {
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
                    mUserCards.add(0, card);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            UserCard updatedBinder = dataSnapshot.getValue(UserCard.class);
            for (UserCard c : mUserCards) {
                if (c.getKey().equals(key)) {
                    c.setValues(updatedBinder);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (UserCard c : mUserCards) {
                if (c.getKey().equals(key)) {
                    mUserCards.remove(c);
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
