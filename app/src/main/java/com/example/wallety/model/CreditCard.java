package com.example.wallety.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class CreditCard {
    @SerializedName("holderName")
    private String holderName;

    @SerializedName("year")
    private String year;

    @SerializedName("month")
    private String month;

    @SerializedName("cardNum")
    private String cardNum;

    @SerializedName("cvvNum")
    private String cvv;

    public CreditCard(String holderName, String year, String month, String cardNum, String cvv) {
        this.holderName = holderName;
        this.year = year;
        this.month = month;
        this.cardNum = cardNum;
        this.cvv = cvv;
    }

    static final String HOLDER_NAME = "holderName";

    static final String YEAR = "year";

    static final String MONTH = "month";

    static final String CARD_NUM = "cardNum";

    static final String CVV = "cvv";

    public String getHolderName() {
        return holderName;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getCardNum() {
        return cardNum;
    }

    public String getCvv() {
        return cvv;
    }


    public static CreditCard fromJson(Map<String, Object> json) {
        String holderName = (String) json.get(HOLDER_NAME);
        String year = (String) json.get(YEAR);
        String month = (String) json.get(MONTH);
        String cardNum = (String) json.get(CARD_NUM);
        String cvvNum = (String) json.get(CVV);

        return new CreditCard(holderName, year, month, cardNum, cvvNum);
    }

    public Map<String, Object> toJson() {
        Map<String, Object> json = new HashMap<>();
        json.put(HOLDER_NAME, getHolderName());
        json.put(YEAR, getYear());
        json.put(MONTH, getMonth());
        json.put(CARD_NUM, getCardNum());
        json.put(CVV, getCvv());

        return json;
    }
}