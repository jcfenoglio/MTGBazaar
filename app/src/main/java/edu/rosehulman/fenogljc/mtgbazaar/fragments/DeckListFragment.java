package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.DeckListAdapter;

/**
 * A fragment representing a list of Items.
 *
 */
public class DeckListFragment extends BinderListFragment {

    private OnBinderSelectedListener mListener;
    private DeckListAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBinderSelectedListener) {
            mListener = (OnBinderSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBinderSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity context = (MainActivity) getContext();

        context.setTitle(R.string.nav_item_decks);

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_deck_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference mUserData = context.getmUserData();

        mAdapter = new DeckListAdapter(this.mListener, this,  mUserData);
        view.setAdapter(mAdapter);

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditBinderDialog(null);
            }
        });
        fab.show();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.addDBListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.removeDBListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
