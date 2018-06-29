package com.sunset.trojanos.sunsetapp;


import com.google.gson.annotations.SerializedName;


public class Response2 {

    @SerializedName("results")
    private Results results;

    @SerializedName("status")
    private String status;

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "Response{" +
                        "results = '" + results + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}