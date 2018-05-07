package edu.rosehulman.fenogljc.mtgbazaar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.fenogljc.mtgbazaar.models.Card;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderFragment;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderFragment.OnCardSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Card} and makes a call to the
 * specified {@link OnCardSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BinderAdapter extends RecyclerView.Adapter<BinderAdapter.ViewHolder> {

    private List<Card> mCards;
    private OnCardSelectedListener mListener;
    private DatabaseReference mRefBinder;
    private Callback mCallback;

    public BinderAdapter(OnCardSelectedListener listener, Callback callback, DatabaseReference ref) {
        mListener = listener;
        mCards = new ArrayList<>();
        mRefBinder = ref;
        mRefBinder.addChildEventListener(new BinderChildEventListener());
        mCallback = callback;
    }

    public void remove(Card card) {
        mRefBinder.child(card.getKey()).removeValue();
    }

    public void add(Card card) {
        mRefBinder.push().setValue(card);
    }

    public void update(Card card, String newName) {
        //TODO: edit card dialog needs a lot more
        card.setName(newName);
        mRefBinder.child(card.getKey()).setValue(card);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_binder_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = mCards.get(position);
        holder.mCardNameView.setText(card.getName());
        holder.mCardAmountView.setText(card.getQty());
        holder.mCardSetView.setText(card.getSet());
        holder.mCardPriceView.setText(String.format("%.2f", card.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
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
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Card card = mCards.get(getAdapterPosition());
            mListener.onCardSelected(card);
        }

        @Override
        public boolean onLongClick(View v) {
            Card card = mCards.get(getAdapterPosition());
            mCallback.onEdit(card);
            return true;
        }
    }

    public interface Callback {
        void onEdit(Card card);
    }

    private class BinderChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Card card = dataSnapshot.getValue(Card.class);
            card.setKey(dataSnapshot.getKey());
            mCards.add(0, card);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
