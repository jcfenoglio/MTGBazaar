package edu.rosehulman.fenogljc.mtgbazaar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rosehulman.fenogljc.mtgbazaar.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderListFragment.OnBinderSelectedListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Binder} and makes a call to the
 * specified {@link OnBinderSelectedListener}.
 */
public class BinderListAdapter extends RecyclerView.Adapter<BinderListAdapter.ViewHolder> {

    private final Context mContext;
    private final OnBinderSelectedListener mListener;
    private final List<Binder> mBinders;
    private DatabaseReference mRefBinders;

    public BinderListAdapter(Context context, OnBinderSelectedListener listener, DatabaseReference ref) {
        mContext = context;
        mListener = listener;
        mBinders = new ArrayList<>();
        mRefBinders = ref;
        mRefBinders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mBinders.add(new Binder(dataSnapshot.getKey()));
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
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_binder_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinder = mBinders.get(position);
        holder.mContentView.setText(mBinders.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mBinders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mContentView;
        public Binder mBinder;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mContentView = (TextView) view.findViewById(R.id.binder_item_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            if (null != mListener) {
                mListener.onBinderSelected(this.mBinder);
            }
        }
    }
}
