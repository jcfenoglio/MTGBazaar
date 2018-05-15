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
import edu.rosehulman.fenogljc.mtgbazaar.fragments.TradeListFragment.OnTradeSelectedListener;
import edu.rosehulman.fenogljc.mtgbazaar.models.Trade;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnTradeSelectedListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TradeListAdapter extends RecyclerView.Adapter<TradeListAdapter.ViewHolder> {

    private OnTradeSelectedListener mListener;
    private List<Trade> mTrades;
    private DatabaseReference mRefTrades;
    private TradeListChildEventListener mDBListener;
    private Callback mCallback;

    public TradeListAdapter(OnTradeSelectedListener listener, Callback callback, DatabaseReference ref) {
        mListener = listener;
        mTrades = new ArrayList<>();
        mRefTrades = ref.child(Constants.DB_TRADES_REF);
        mDBListener = new TradeListChildEventListener();
        mCallback = callback;
    }

    public void remove(Trade trade) {
        mRefTrades.child(trade.getKey()).removeValue();
    }

    public void add(Trade trade) {
        mRefTrades.push().setValue(trade);
    }

    public void update(Trade trade, String newName) {
        trade.setName(newName);
        mRefTrades.child(trade.getKey()).setValue(trade);
    }

    public void addDBListener() {
        mTrades.clear();
        mRefTrades.addChildEventListener(mDBListener);
    }

    public void removeDBListener() {
        mRefTrades.removeEventListener(mDBListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_trade_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Trade trade = mTrades.get(position);
        holder.mContentView.setText(trade.getName());
    }

    @Override
    public int getItemCount() {
        return mTrades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.trade_item_name);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trade trade = mTrades.get(getAdapterPosition());
            mListener.onTradeSelected(trade);
        }

        @Override
        public boolean onLongClick(View v) {
            Trade trade = mTrades.get(getAdapterPosition());
            mCallback.onEdit(trade);
            return true;
        }
    }

    public interface Callback {
        void onEdit(Trade trade);
    }

    private class TradeListChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Trade trade = dataSnapshot.getValue(Trade.class);
            trade.setKey(dataSnapshot.getKey());
            mTrades.add(0, trade);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Trade updatedTrade = dataSnapshot.getValue(Trade.class);
            for (Trade t : mTrades) {
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
            for (Trade t : mTrades) {
                if (t.getKey().equals(key)) {
                    mTrades.remove(t);
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
