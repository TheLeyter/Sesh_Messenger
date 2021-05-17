package com.example.sesh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenPair {
    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    public TokenPair(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
