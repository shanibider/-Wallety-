package com.example.wallety.model;

public class Transactions {

    int image;
    private String source;
    private String date;
    private String sum;


    public Transactions(int image, String source, String date, String sum) {
        this.image = image;
        this.source = source;
        this.date = date;
        this.sum = sum;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }
}
