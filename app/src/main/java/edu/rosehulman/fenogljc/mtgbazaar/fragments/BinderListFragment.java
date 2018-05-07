package edu.rosehulman.fenogljc.mtgbazaar.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.BinderListAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.models.Binder;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBinderSelectedListener}
 * interface.
 */
public class BinderListFragment extends Fragment implements BinderListAdapter.Callback{

    private OnBinderSelectedListener mListener;
    private BinderListAdapter mAdapter;

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

        MainActivity context = (MainActivity) getContext();

        context.setTitle(R.string.nav_item_binders);

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_binder_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference mUserData = context.getmUserData();

        mAdapter = new BinderListAdapter(mListener, this,  mUserData);
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

    private void showAddEditBinderDialog(final Binder binder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(binder == null ? R.string.new_binder_dialog_title : R.string.edit_binder_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.add_binder_popup, null, false);
        builder.setView(view);
        final EditText editTitleText = view.findViewById(R.id.add_binder_name);

        if (binder != null) {
            editTitleText.setText(binder.getName());

            builder.setNeutralButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDeleteConfirmationDialog(binder);
                }
            });
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTitleText.getText().toString();
                if (binder != null) {
                    mAdapter.update(binder, title);
                } else {
                    mAdapter.add(new Binder(title));
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    private void showDeleteConfirmationDialog(final Binder binder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.delete_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.delete_confirmation_popup, null, false);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.remove(binder);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onEdit(Binder binder) {
        showAddEditBinderDialog(binder);
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
