package com.example.wallety.model.server;

import com.example.wallety.model.Transaction;
import com.google.gson.annotations.SerializedName;

public class TransactionRequest {

    @SerializedName("transaction")
    private Transaction transaction;
    private String accessToken;


    public TransactionRequest(Transaction transaction, String accessToken) {
        this.transaction = transaction;
        this.accessToken = accessToken;
    }
}
