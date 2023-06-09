package com.example.wallety.model.server;

import com.example.wallety.model.Transaction;
import com.google.gson.annotations.SerializedName;

public class TransactionRequest {

    @SerializedName("transaction")
    private Transaction transaction;

    public TransactionRequest(Transaction transaction) {
        this.transaction = transaction;
    }
}
