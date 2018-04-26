package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.fenogljc.mtgbazaar.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.BinderListAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBinderSelectedListener}
 * interface.
 */
public class BinderListFragment extends Fragment {

    private OnBinderSelectedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BinderListFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((Activity) getContext()).setTitle(R.string.nav_item_binders);

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_binder_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference mUserBindersData = ((MainActivity) getContext()).getmUserData().child(Constants.DB_BINDERS_REF);

        BinderListAdapter adapter = new BinderListAdapter(getContext(), mListener, mUserBindersData);
        view.setAdapter(adapter);
        return view;
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
        void onBinderSelected(Binder binder);
    }
}
