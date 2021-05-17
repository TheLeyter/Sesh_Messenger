package com.example.sesh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSignIn {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    public UserSignIn(String Username, String Password){
        this.username = Username;
        this.password = Password;
    }

    public String getUsername(){return this.username;}

    public void setUsername(String Username){this.username = Username;}

    public String getPassword(){return this.password;}

    public void setPassword(String Password){this.password = Password;}
}
