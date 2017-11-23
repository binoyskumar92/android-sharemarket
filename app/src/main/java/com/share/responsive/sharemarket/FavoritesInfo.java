package com.share.responsive.sharemarket;

/**
 * Created by Binoy on 11/22/2017.
 */

public class FavoritesInfo {
    private String symbol;
    private String price;
    private String change;
    private String changeperc;

    public FavoritesInfo(String symbol, String price, String change, String changeperc) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changeperc = changeperc;
    }
    public FavoritesInfo() {
        this.symbol = "";
        this.price = "";
        this.change = "";
        this.changeperc = "";
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangeperc() {
        return changeperc;
    }

    public void setChangeperc(String changeperc) {
        this.changeperc = changeperc;
    }
}
