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

import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderFragment.OnCardSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UserCard} and makes a call to the
 * specified {@link OnCardSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BinderAdapter extends RecyclerView.Adapter<BinderAdapter.ViewHolder> {

    private List<UserCard> mUserCards;
    private OnCardSelectedListener mListener;
    private DatabaseReference mRefBinder;
    private Callback mCallback;

    public BinderAdapter(OnCardSelectedListener listener, Callback callback, DatabaseReference ref) {
        mListener = listener;
        mUserCards = new ArrayList<>();
        mRefBinder = ref;
        mRefBinder.child(Constants.DB_CARDS_REF).addChildEventListener(new BinderChildEventListener());
        mCallback = callback;
    }

    public void remove(UserCard userCard) {
        mRefBinder.child(userCard.getKey()).removeValue();
    }

    public void add(UserCard userCard) {
        mRefBinder.push().setValue(userCard);
    }

    public void update(UserCard userCard, String newName) {
        //TODO: edit userCard dialog needs a lot more
        userCard.setName(newName);
        mRefBinder.child(userCard.getKey()).setValue(userCard);
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
        holder.mCardAmountView.setText(userCard.getQty());
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

    public interface Callback {
        void onEdit(UserCard userCard);
    }

    private class BinderChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            UserCard userCard = dataSnapshot.getValue(UserCard.class);
            userCard.setKey(dataSnapshot.getKey());
            mUserCards.add(0, userCard);
            notifyDataSetChanged();
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
