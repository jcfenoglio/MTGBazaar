package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.fenogljc.mtgbazaar.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.Card;
import edu.rosehulman.fenogljc.mtgbazaar.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCardSelectedListener}
 * interface.
 */
public class BinderFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_BINDER = "binder";
    // TODO: Customize parameters
    private Binder mBinder;

    private OnCardSelectedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BinderFragment() {
    }

    // TODO: Customize parameter initialization
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_binder_card_list, container, false);


        return view;
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnCardSelectedListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Card item);
    }
}
