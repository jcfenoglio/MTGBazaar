package edu.rosehulman.fenogljc.mtgbazaar.models;


import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Card implements Serializable {

    private float cmc;
    private List<String> color;
    private String manaCost;
    private String power;
    private List<String> rarity;
    private List<String> sets;
    private List<String> subtype;
    private List<String> supertype;
    private String text;
    private String toughness;
    private List<String> type;
    private String key;
    private List<String> multiverseid;
    private List<String> tcgplayerid;
    private List<String> collectornumber;

    public List<String> getTcgplayerid() {
        return tcgplayerid;
    }

    public void setTcgplayerid(List<String> tcgplayerid) {
        this.tcgplayerid = tcgplayerid;
    }

    public List<String> getMultiverseid() {
        return multiverseid;
    }

    public void setMultiverseid(List<String> multiverseid) {
        this.multiverseid = multiverseid;
    }

    public List<String> getCollectornumber() {
        return  collectornumber;
    }

    public void setCollectornumber(List<String> collectornumber) {
        this.collectornumber = collectornumber;
    }

    public float getCmc() {
        return cmc;
    }

    public void setCmc(float cmc) {
        this.cmc = cmc;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public List<String> getRarity() {
        return rarity;
    }

    public void setRarity(List<String> rarity) {
        this.rarity = rarity;
    }

    public List<String> getSets() {
        return sets;
    }

    public void setSets(List<String> sets) {
        this.sets = sets;
    }

    @Exclude
    public List<String> getLanguages() {
        ArrayList<String> languages = new ArrayList<>();
        languages.add("english");
        return languages;
    }

    public List<String> getSubtype() {
        return subtype;
    }

    public void setSubtype(List<String> subtype) {
        this.subtype = subtype;
    }

    public List<String> getSupertype() {
        return supertype;
    }

    public void setSupertype(List<String> supertype) {
        this.supertype = supertype;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getToughness() {
        return toughness;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Card newCard) {
        setCmc(newCard.getCmc());
        setColor(newCard.getColor());
        setManaCost(newCard.getManaCost());
        setPower(newCard.getPower());
        setRarity(newCard.getRarity());
        setSets(newCard.getSets());
        setSubtype(newCard.getSubtype());
        setSupertype(newCard.getSupertype());
        setText(newCard.getText());
        setToughness(newCard.getToughness());
        setType(newCard.getType());
    }
}
