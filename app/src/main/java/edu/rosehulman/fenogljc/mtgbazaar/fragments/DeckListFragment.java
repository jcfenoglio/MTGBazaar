package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.DeckListAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.models.Deck;

/**
 * A fragment representing a list of Items.
 *
 */
public class DeckListFragment extends Fragment implements DeckListAdapter.Callback {

    private OnDeckSelectedListener mListener;
    private DeckListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeckListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDeckSelectedListener) {
            mListener = (OnDeckSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDeckSelectedListener");
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

        mAdapter = new DeckListAdapter(mListener, this, mUserData);
        view.setAdapter(mAdapter);

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDeckDialog(null);
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

    public void showAddEditDeckDialog(final Deck deck) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(deck == null ? R.string.new_deck_dialog_title : R.string.edit_deck_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.add_deck_popup, null, false);
        builder.setView(view);
        final EditText editTitleText = view.findViewById(R.id.add_deck_name);

        final Spinner formatSpinner = view.findViewById(R.id.edit_card_set);
        ArrayAdapter<String> formatAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Constants.FORMAT_ARRAY);
        formatSpinner.setAdapter(formatAdapter);

        if (deck != null) {
            editTitleText.setText(deck.getName());

            builder.setNeutralButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDeleteConfirmationDialog(deck);
                }
            });
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTitleText.getText().toString();
                String format = formatSpinner.getSelectedItem().toString();
                if (deck != null) {
                    mAdapter.update(deck, title, format);
                } else {
                    mAdapter.add(new Deck(title, format));
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    private void showDeleteConfirmationDialog(final Deck deck) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.delete_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.delete_confirmation_popup, null, false);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.remove(deck);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    @Override
    public void onEdit(Deck deck) {
        showAddEditDeckDialog(deck);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDeckSelectedListener {
        void onDeckSelected(Deck deck);
    }
}