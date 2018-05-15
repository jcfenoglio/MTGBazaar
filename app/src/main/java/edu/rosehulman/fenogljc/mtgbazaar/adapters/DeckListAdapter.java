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

import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.DeckListFragment.OnDeckSelectedListener;
import edu.rosehulman.fenogljc.mtgbazaar.models.Deck;

/**
 *
 */
public class DeckListAdapter extends RecyclerView.Adapter<DeckListAdapter.ViewHolder> {

    private OnDeckSelectedListener mListener;
    private List<Deck> mDecks;
    private DatabaseReference mRefDecks;
    private DeckListChildEventListener mDBListener;
    private Callback mCallback;

    public DeckListAdapter(OnDeckSelectedListener listener, Callback callback, DatabaseReference ref) {
        mListener = listener;
        mDecks = new ArrayList<>();
        mRefDecks = ref.child(Constants.DB_DECKS_REF);
        mDBListener = new DeckListChildEventListener();
        mCallback = callback;
    }

    public void remove(Deck Deck) {
        mRefDecks.child(Deck.getKey()).removeValue();
    }

    public void add(Deck Deck) {
        mRefDecks.push().setValue(Deck);
    }

    public void update(Deck Deck, String newName) {
        Deck.setName(newName);
        mRefDecks.child(Deck.getKey()).setValue(Deck);
    }

    public void addDBListener() {
        mRefDecks.addChildEventListener(mDBListener);
    }

    public void removeDBListener() {
        mRefDecks.removeEventListener(mDBListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_deck_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Deck Deck = mDecks.get(position);
        holder.mContentView.setText(Deck.getName());
    }

    @Override
    public int getItemCount() {
        return mDecks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id. deck_item_name);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Deck Deck = mDecks.get(getAdapterPosition());
            mListener.onDeckSelected(Deck);
        }

        @Override
        public boolean onLongClick(View v) {
            Deck Deck = mDecks.get(getAdapterPosition());
            mCallback.onEdit(Deck);
            return true;
        }
    }

    public interface Callback {
        void onEdit(Deck Deck);
    }

    protected class DeckListChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Deck deck = dataSnapshot.getValue(Deck.class);
            deck.setKey(dataSnapshot.getKey());
            mDecks.add(0, deck);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Deck updatedDeck = dataSnapshot.getValue(Deck.class);
            for (Deck b : mDecks) {
                if (b.getKey().equals(key)) {
                    b.setValues(updatedDeck);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Deck b : mDecks) {
                if (b.getKey().equals(key)) {
                    mDecks.remove(b);
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
