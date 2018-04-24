package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.rosehulman.fenogljc.mtgbazaar.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.BinderListAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBinderSelectedListener}
 * interface.
 */
public class BinderListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnBinderSelectedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BinderListFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BinderListFragment newInstance(int columnCount) {
        BinderListFragment fragment = new BinderListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_binder_list, container, false);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(getContext()));
        BinderListAdapter adapter = new BinderListAdapter(new ArrayList<Binder>(), mListener);
        view.setAdapter(adapter);
        return view;
    }


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
    public interface OnBinderSelectedListener {
        // TODO: Update argument type and name
        void onBinderSelected(Binder binder);
    }
}
