package edu.rosehulman.fenogljc.mtgbazaar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderFragment;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.BinderListFragment;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.DeckFragment;
import edu.rosehulman.fenogljc.mtgbazaar.fragments.DeckListFragment;
import edu.rosehulman.fenogljc.mtgbazaar.models.Binder;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;
import edu.rosehulman.fenogljc.mtgbazaar.models.Deck;
import edu.rosehulman.fenogljc.mtgbazaar.models.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BinderListFragment.OnBinderSelectedListener, DeckListFragment.OnDeckSelectedListener, BinderFragment.OnCardSelectedListener {

    private DatabaseReference mFirebase;
    private DatabaseReference mUserData;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebase.child(Constants.DB_USERS_REF).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    Log.d(Constants.TAG, "user does not exist");
                    mFirebase.child(Constants.DB_USERS_REF).child(mUser.getUid()).setValue(new User(mUser.getDisplayName()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mUserData = mFirebase.child(Constants.DB_USERS_REF).child(mUser.getUid());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new BinderListFragment());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity.this, EmptyMainActivity.class));
                            finish();
                        }
                    });
        }
        return false;
    }

    public DatabaseReference getmUserData() {
        return mUserData;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment switchTo = null;
        switch (item.getItemId()) {
            case R.id.nav_binders:
                switchTo = new BinderListFragment();
                break;
            case R.id.nav_decks:
                switchTo = new DeckListFragment();
                break;
            case R.id.nav_trade:
//                switchTo = new TradeListFragment();
                break;
            case R.id.nav_search:
//                switchTo = new SearchFragment();
                break;
        }

        if (switchTo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, switchTo);
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }

            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBinderSelected(Binder binder) {
        Log.d(Constants.TAG, "Binder selected: " + binder.getName());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, BinderFragment.newInstance(binder));
        ft.addToBackStack("binder_list_fragment");
        ft.commit();
    }

    @Override
    public void onDeckSelected(Deck item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, DeckFragment.newInstance(item));
        ft.addToBackStack("deck_list_fragment");
        ft.commit();
    }

    @Override
    public void onCardSelected(UserCard userCard) {

    }
}
