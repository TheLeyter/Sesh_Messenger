package com.example.sesh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("registerDate")
    @Expose
    private String registerDate;

    @SerializedName("confirm")
    @Expose
    private boolean confirm;



    public User(long id, String Username, String email, String firstName, String lastName, String Password, String avatar, String registerDate, boolean confirm){
        this.id = id;
        this.username = Username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = Password;
        this.avatar = avatar;
        this.registerDate = registerDate;
        this.confirm = confirm;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String Username){
        this.username = Username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }


    public String getPassword(){
        return this.password;
    }

    public void setPassword(String Password){
        this.password = Password;
    }

    public boolean getConfirm(){
        return this.confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", registerDate='" + registerDate + '\'' +
                ", confirm=" + confirm +
                '}';
    }
}
