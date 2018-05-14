package edu.rosehulman.fenogljc.mtgbazaar.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Locale;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;
import edu.rosehulman.fenogljc.mtgbazaar.MainActivity;
import edu.rosehulman.fenogljc.mtgbazaar.R;
import edu.rosehulman.fenogljc.mtgbazaar.models.Card;
import edu.rosehulman.fenogljc.mtgbazaar.models.UserCard;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardFragment extends Fragment {


    private ImageView mImageView;

    public CardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View layoutView = inflater.inflate(R.layout.fragment_card, container, false);

        final MainActivity context = (MainActivity) getContext();

        context.setTitle(getString(R.string.nav_item_search));

        layoutView.findViewById(R.id.pricing_layout).setVisibility(View.INVISIBLE);
        layoutView.findViewById(R.id.card_info_layout).setVisibility(View.INVISIBLE);
        layoutView.findViewById(R.id.card_text).setVisibility(View.INVISIBLE);
        layoutView.findViewById(R.id.card_image).setVisibility(View.INVISIBLE);

        final AutoCompleteTextView autoComplete = layoutView.findViewById(R.id.card_search);

        ArrayAdapter myAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, Constants.getCardNames());
        autoComplete.setAdapter(myAdapter);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager inputManager = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(autoComplete.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String cardName = autoComplete.getText().toString();
                FirebaseDatabase.getInstance().getReference().child(Constants.DB_CARDS_REF).child(cardName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Card incomingCard = dataSnapshot.getValue(Card.class);
                        incomingCard.setKey(dataSnapshot.getKey());
                        populateCardInfo(layoutView, incomingCard);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        FloatingActionButton fab = context.findViewById(R.id.fab);
        fab.hide();

        return layoutView;
    }

    private void populateCardInfo(View layoutView, Card incomingCard) {
        TextView cardName = layoutView.findViewById(R.id.card_name);
        TextView cardType = layoutView.findViewById(R.id.card_type);
        TextView cardManaCost = layoutView.findViewById(R.id.card_mana_cost);
        TextView cardText = layoutView.findViewById(R.id.card_text);
        Spinner setSelector = layoutView.findViewById(R.id.pricing_set);
        TextView regPrice = layoutView.findViewById(R.id.regular_price);
        TextView foilPrice = layoutView.findViewById(R.id.foil_price);
        mImageView = layoutView.findViewById(R.id.card_image);

        cardName.setText(incomingCard.getKey());
        cardType.setText(incomingCard.getType().toString() + " - " + incomingCard.getSubtype().toString());
        cardManaCost.setText(incomingCard.getManaCost());
        cardText.setText(incomingCard.getText());
        
        //TODO: get image from multiverse ID
        (new GetImageClass()).execute("https://api.scryfall.com/cards/multiverse/" + incomingCard.getMultiverseId());

        //TODO: FILL SELECTOR AND PRICES
        ArrayAdapter<String> setAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, incomingCard.getSets());
        setSelector.setAdapter(setAdapter);

        regPrice.setText(String.format(Locale.getDefault(), "%.2f", 0.00f));
        foilPrice.setText(String.format(Locale.getDefault(), "%.2f", 0.00f));

        layoutView.findViewById(R.id.pricing_layout).setVisibility(View.VISIBLE);
        layoutView.findViewById(R.id.card_info_layout).setVisibility(View.VISIBLE);
        layoutView.findViewById(R.id.card_text).setVisibility(View.VISIBLE);
        layoutView.findViewById(R.id.card_image).setVisibility(View.VISIBLE);
    }

    class GetImageClass extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urlStrings) {

            String urlString = urlStrings[0];
            Log.d("PICS", "doInBackground: " + urlString);
            URL url;
            Bitmap bmp = null;
            try {
                // Fetch the card info from scryfall
                url = new URL(urlString);
                InputStream is = url.openStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);

                // Get the url for the image and download that image
                String imageURL = json.getJSONObject("image_uris").getString("normal");
                url = new URL(imageURL);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        private String readAll(BufferedReader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageView.setImageBitmap(bitmap);
        }
    }
}
