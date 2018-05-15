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

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UserCard} and makes a call to the
 * specified .
 * TODO: Replace the implementation with code for your data type.
 */
public class DeckAdapter extends BinderAdapter {

    public DeckAdapter(Callback callback, DatabaseReference ref) {
        super(callback, ref);
    }
}
