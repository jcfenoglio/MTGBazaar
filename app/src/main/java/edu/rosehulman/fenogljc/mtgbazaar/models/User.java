package edu.rosehulman.fenogljc.mtgbazaar.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;

    @SuppressWarnings("unused")
    public User () {}

    public User(String name) {
        this.username = name;
    }
}
