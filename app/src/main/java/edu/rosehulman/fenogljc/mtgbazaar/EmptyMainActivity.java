package edu.rosehulman.fenogljc.mtgbazaar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmptyMainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 349;
    private boolean persistanceEnabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Constants.getPersistanceEnabled()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Constants.setPersistanceEnabled(true);
        }
        loadCardNamesFromDatabase();
        setContentView(R.layout.activity_empty_main);
        //TODO: CHECK IF TCGPLAYER BEARER TOKEN IS EXPIRED, IF IT IS, GENERATE A NEW ONE
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            moveToMainActivity();
        } else {
            attemptLogin();
        }
    }

    private void loadCardNamesFromDatabase() {
        DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();
        final List<String> mCardNames = new ArrayList<>();
        ValueEventListener mChildEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    mCardNames.add(child.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(Constants.TAG, "onChildChanged: " + databaseError.getMessage());
            }
        };
        mFirebase.child(Constants.DB_CARDS_REF).addListenerForSingleValueEvent(mChildEventListener);
        Constants.setCardNames(mCardNames);
    }



    
    private void attemptLogin() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .build(),
                RC_SIGN_IN);
    }

    private void moveToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                moveToMainActivity();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);
                Log.e(Constants.TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void showSnackbar(int msg) {
        Snackbar.make(findViewById(R.id.add_deck_name), getText(msg), Snackbar.LENGTH_INDEFINITE).show();
    }
}
