package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.BinderAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.models.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.models.Card;
import edu.rosehulman.fenogljc.mtgbazaar.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCardSelectedListener}
 * interface.
 */
public class BinderFragment extends Fragment implements BinderAdapter.Callback{

    private static final String ARG_BINDER = "binder";
    private Binder mBinder;
    private OnCardSelectedListener mListener;
    private BinderAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BinderFragment() {
    }

    @SuppressWarnings("unused")
    public static BinderFragment newInstance(Binder binder) {
        BinderFragment fragment = new BinderFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BINDER, binder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mBinder = getArguments().getParcelable(ARG_BINDER);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCardSelectedListener) {
            mListener = (OnCardSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCardSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity context = (MainActivity) getContext();

        context.setTitle(mBinder.getName());

        View view = inflater.inflate(R.layout.fragment_binder, container, false);

        // TODO: add functionality to search and add buttons

        RecyclerView recyclerView = view.findViewById(R.id.binder_card_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference mUserData = context.getmUserData().child(Constants.DB_BINDERS_REF).child(mBinder.getKey());

        mAdapter = new BinderAdapter(mListener, this, mUserData);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onEdit(Card card) {
        // TODO: make this work

    }

    public interface OnCardSelectedListener {
        void onCardSelected(Card card);
    }
}
