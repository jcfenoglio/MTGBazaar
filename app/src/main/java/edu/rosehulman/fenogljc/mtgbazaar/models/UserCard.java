package edu.rosehulman.fenogljc.mtgbazaar.models;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import edu.rosehulman.fenogljc.mtgbazaar.Callback;
import edu.rosehulman.fenogljc.mtgbazaar.Constants;

public class UserCard {

    private Card card;
    private String set;
    private int qty = 1;
    private float price = 0.0f;
    private String language = Constants.LANG_EN;
    private boolean foil = false;
    private String condition = Constants.COND_ARRAY[0];
    private String key;
    private String name;

    public UserCard () {}

    public UserCard(String cardName) {
        setName(cardName);
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public int getQty() { return qty; }

    public void setQty(int qty) { this.qty = qty; }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isFoil() {
        return foil;
    }

    public void setFoil(boolean foil) {
        this.foil = foil;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Exclude
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setValues(UserCard newUserCard) {
        setSet(newUserCard.getSet());
        setQty(newUserCard.getQty());
        setPrice(newUserCard.getPrice());
        setLanguage(newUserCard.getLanguage());
        setFoil(newUserCard.isFoil());
    }

    public void setCardFromName(final Callback callback) {
        //TODO: find card from name
        final UserCard userCard = this;
        FirebaseDatabase.getInstance().getReference().child(Constants.DB_CARDS_REF).child(userCard.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Card incomingCard = dataSnapshot.getValue(Card.class);
                incomingCard.setKey(dataSnapshot.getKey());
                userCard.setCard(incomingCard);
                if (userCard.getSet() == null) {
                    userCard.setSet(userCard.getCard().getSets().get(0));
                }
                Log.d(Constants.TAG, "onDataChange: " + getName());
                callback.onCardFound(userCard);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void getPrices() {

    }

    private class GetPriceClass extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urlStrings) {
            // http://api.tcgplayer.com/pricing/product/
            String urlString = urlStrings[0];
            Log.d("PICS", "doInBackground: " + urlString);
            URL url;
            JSONObject json = null;
            try {
                // Fetch the card info from scryfall
                url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Authorization", "bearer H_mieexY5sEtZh70XUm-VwQNLqwNe2RShoAQvInduyKPx4zhAopEdlpCObd2wFZCe7OSsGxN0Wzllo1DSNJR1JPEVegi0h94u_5wXIFCt6-xEQT4tR-QwVAhl0GrFrHN09AHmp3b93gIwvSTYQ2bx_j3a-WSHNzT5JvWjhSBUCPQ1lqUOARmq9d_Dr3pjtmLE6hfa6wBTPV_e3tpcZxGN4JK9iAsu1HBLHX_0zHC8XLKQI9GYUmaE0GZVEWU4_H4mutxgp4563Ktp16_7jPlk45HVmtfxxYn6uwC2Rt9EAajNo-k7TTwhrGyrdrS_akbGVbZtXY0ouZw-KbQ3nlE3VWY814");
                InputStream is = urlConnection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                json = new JSONObject(jsonText);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
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
        protected void onPostExecute(JSONObject j) {
            Log.d(Constants.TAG, "onPostExecute: " + j.toString());
            try {
                //TODO: Determine which is foil and which is not
                double foilPrice = j.getJSONArray("results").getJSONObject(0).getDouble("marketPrice");
                Log.d(Constants.TAG, "onPostExecute: " + foilPrice);
                //mFoilPrice.setText(String.format(Locale.getDefault(), "$%.2f", foilPrice));
            } catch (Exception e) {
                e.printStackTrace();
                //mFoilPrice.setText("N/A");
            }
            try {
                double regPrice = j.getJSONArray("results").getJSONObject(1).getDouble("marketPrice");
                Log.d(Constants.TAG, "onPostExecute: " + regPrice);
                //mRegPrice.setText(String.format(Locale.getDefault(), "$%.2f", regPrice));
            } catch (Exception e) {
                e.printStackTrace();
                //mRegPrice.setText("N/A");
            }

        }
    }
}
