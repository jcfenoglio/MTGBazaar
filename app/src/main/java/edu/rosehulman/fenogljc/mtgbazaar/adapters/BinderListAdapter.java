package edu.rosehulman.fenogljc.mtgbazaar.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.fenogljc.mtgbazaar.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderListFragment;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderListFragment.OnBinderSelectedListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Binder} and makes a call to the
 * specified {@link OnBinderSelectedListener}.
 */
public class BinderListAdapter extends RecyclerView.Adapter<BinderListAdapter.ViewHolder> {

    private final List<Binder> mBinders;
    private final Context mContext;

    public BinderListAdapter(ArrayList<Binder> binders, Context context) {
        mContext = context;
        mBinders = binders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_binder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mBinders.get(position);
        holder.mIdView.setText(mBinders.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mContext) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    ((OnBinderSelectedListener) mContext).onBinderSelected(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBinders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Binder mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.binder_item_number);
            mContentView = (TextView) view.findViewById(R.id.binder_content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
