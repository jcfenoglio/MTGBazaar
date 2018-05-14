package edu.rosehulman.fenogljc.mtgbazaar.models;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private String name;
    private List<String> sets = new ArrayList<>();
    private List<String> languages = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSets() {
        sets.add("set name");
        sets.add("second set name");
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }

    public List<String> getLanguages() {
        languages.add("english")
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
}
