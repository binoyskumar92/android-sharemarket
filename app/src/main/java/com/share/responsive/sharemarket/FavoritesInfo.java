package com.share.responsive.sharemarket;

import java.util.Comparator;

/**
 * Created by Binoy on 11/22/2017.
 */

public class FavoritesInfo {
    private String symbol;
    private String price;
    private String change;
    private String changeperc;
    private static boolean descending;

    public FavoritesInfo(String symbol, String price, String change, String changeperc) {
        this.symbol = symbol;
        this.price = price;
        this.change = change;
        this.changeperc = changeperc;
        this.descending=false;
    }
    public FavoritesInfo() {
        this.symbol = "";
        this.price = "";
        this.change = "";
        this.changeperc = "";
        this.descending=false;
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

    public static void setDescending(boolean value){
        descending = value;
    }
    public static boolean  isDescending(){
        return descending;
    }

    /*Comparator for sorting the list by Favorties symbol*/
    public static Comparator<FavoritesInfo> SymbolComparator = new Comparator<FavoritesInfo>() {

        public int compare(FavoritesInfo s1, FavoritesInfo s2) {
            String symbol1 = s1.getSymbol().toUpperCase();
            String symbol2 = s2.getSymbol().toUpperCase();

            if(isDescending()) {
                return symbol2.compareTo(symbol1);
            }else{
                return symbol1.compareTo(symbol2);
            }
        }};
}
