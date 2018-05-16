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
import edu.rosehulman.fenogljc.mtgbazaar.models.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderListFragment.OnBinderSelectedListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Binder} and makes a call to the
 * specified {@link OnBinderSelectedListener}.
 */
public class BinderListAdapter extends RecyclerView.Adapter<BinderListAdapter.ViewHolder> {

    private OnBinderSelectedListener mListener;
    private List<Binder> mBinders;
    private DatabaseReference mRefBinders;
    private BinderListChildEventListener mDBListener;
    private Callback mCallback;

    public BinderListAdapter(OnBinderSelectedListener listener, Callback callback, DatabaseReference ref) {
        mListener = listener;
        mBinders = new ArrayList<>();
        mRefBinders = ref.child(Constants.DB_BINDERS_REF);
        mDBListener = new BinderListChildEventListener();
        mCallback = callback;
    }

    public void remove(Binder binder) {
        Log.d(Constants.TAG, "remove: " + binder.getKey());
        mRefBinders.child(binder.getKey()).removeValue();
    }

    public void add(Binder binder) {
        mRefBinders.push().setValue(binder);
    }

    public void update(Binder binder, String newName) {
        binder.setName(newName);
        mRefBinders.child(binder.getKey()).setValue(binder);
    }

    public void addDBListener() {
        mBinders.clear();
        mRefBinders.addChildEventListener(mDBListener);
    }

    public void removeDBListener() {
        mRefBinders.removeEventListener(mDBListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_binder_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Binder binder = mBinders.get(position);
        holder.mContentView.setText(binder.getName());
    }

    @Override
    public int getItemCount() {
        return mBinders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.binder_item_name);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Binder binder = mBinders.get(getAdapterPosition());
            mListener.onBinderSelected(binder);
        }

        @Override
        public boolean onLongClick(View v) {
            Binder binder = mBinders.get(getAdapterPosition());
            mCallback.onEdit(binder);
            return true;
        }
    }

    public interface Callback {
        void onEdit(Binder binder);
    }

    protected class BinderListChildEventListener implements ChildEventListener {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Binder binder = dataSnapshot.getValue(Binder.class);
            binder.setKey(dataSnapshot.getKey());
            mBinders.add(0, binder);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String key = dataSnapshot.getKey();
            Binder updatedBinder = dataSnapshot.getValue(Binder.class);
            for (Binder b : mBinders) {
                if (b.getKey().equals(key)) {
                    b.setValues(updatedBinder);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String key = dataSnapshot.getKey();
            for (Binder b : mBinders) {
                if (b.getKey().equals(key)) {
                    mBinders.remove(b);
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
