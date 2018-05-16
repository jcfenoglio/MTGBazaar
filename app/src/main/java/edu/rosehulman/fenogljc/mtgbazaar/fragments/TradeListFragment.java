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
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.adapters.TradeListAdapter;
import edu.rosehulman.fenogljc.mtgbazaar.models.Trade;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnTradeSelectedListener}
 * interface.
 */
public class TradeListFragment extends Fragment implements TradeListAdapter.Callback{

    private OnTradeSelectedListener mListener;
    private TradeListAdapter mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TradeListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTradeSelectedListener) {
            mListener = (OnTradeSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTradeSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity context = (MainActivity) getContext();

        context.setTitle(R.string.nav_item_trades);

        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_trade_list, container, false);
        view.setLayoutManager(new LinearLayoutManager(context));

        DatabaseReference mUserData = context.getmUserData();

        mAdapter = new TradeListAdapter(mListener, this,  mUserData);
        view.setAdapter(mAdapter);

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditTradeDialog(null);
            }
        });
        fab.show();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.addDBListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.removeDBListener();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void showAddEditTradeDialog(final Trade trade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(trade == null ? R.string.new_trade_dialog_title : R.string.edit_trade_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.add_binder_popup, null, false);
        builder.setView(view);
        final EditText editTitleText = view.findViewById(R.id.add_binder_name);

        if (trade != null) {
            editTitleText.setText(trade.getName());

            builder.setNeutralButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showDeleteConfirmationDialog(trade);
                }
            });
        } else {
            SimpleDateFormat df = new SimpleDateFormat("MM.dd.yy hh:mm", Locale.getDefault());
            editTitleText.setText(df.format(Calendar.getInstance().getTime()));
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTitleText.getText().toString();
                if (trade != null) {
                    mAdapter.update(trade, title);
                } else {
                    mAdapter.add(new Trade(title));
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    private void showDeleteConfirmationDialog(final Trade trade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.delete_dialog_title);

        View view = getLayoutInflater().inflate(R.layout.delete_confirmation_popup, null, false);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.remove(trade);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();
    }

    @Override
    public void onEdit(Trade trade) {
        showAddEditTradeDialog(trade);
    }

    public interface OnTradeSelectedListener {
        void onTradeSelected(Trade trade);
    }
}
