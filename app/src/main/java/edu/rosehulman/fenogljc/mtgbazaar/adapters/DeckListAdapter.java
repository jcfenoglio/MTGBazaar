package edu.rosehulman.fenogljc.mtgbazaar.adapters;

import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderListFragment;

/**
 *
 */
public class DeckListAdapter extends BinderListAdapter{


    public DeckListAdapter(BinderListFragment.OnBinderSelectedListener listener, Callback callback, DatabaseReference ref) {
        super(listener, callback, ref);
    }
}
