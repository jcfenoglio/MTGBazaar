package edu.rosehulman.fenogljc.mtgbazaar.adapters;

import android.content.Context;
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

import edu.rosehulman.fenogljc.mtgbazaar.Trade;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.Trade;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.TradeListFragment;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.TradeListFragment.OnTradeSelectedListener;
import edu.rosehulman.fenogljc.mtgbazaar.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnTradeSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TradeListAdapter extends RecyclerView.Adapter<TradeListAdapter.ViewHolder> {

    private final List<Trade> mValues;
    private final TradeListFragment.OnTradeSelectedListener mListener;
    private final Context mContext;
    private DatabaseReference mTradesRef;

    public TradeListAdapter(Context context, OnTradeSelectedListener listener, DatabaseReference databaseReference) {
        mValues = new ArrayList<>();
        mListener = listener;
        mContext = context;
        mTradesRef = databaseReference;
        mTradesRef.addChildEventListener(new TradeChildEventListener());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trade_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getTradeName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onTradeFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public Trade mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }

    private class TradeChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Trade trade = dataSnapshot.getValue(Trade.class);
            trade.setKey(dataSnapshot.getKey());
            mValues.add(0, trade);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Trade updatedTrade = dataSnapshot.getValue(Trade.class);
            for (Trade t : mValues) {
                if (t.getKey().equals(key)) {
                    t.setValues(updatedTrade);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Trade t : mValues) {
                if (t.getKey().equals(key)) {
                    mValues.remove(t);
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
            Log.e("mtgBazaar", databaseError.getMessage());
        }
    }
}
