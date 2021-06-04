package com.example.sesh.service;

import com.example.sesh.activity.SignUp;
import com.example.sesh.models.Post;
import com.example.sesh.models.SignUpUser;
import com.example.sesh.models.TokenPair;
import com.example.sesh.models.User;
import com.example.sesh.models.UserInfo;
import com.example.sesh.models.UserSignIn;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiEndPoints {
    final String baseControler = "api/v1/account/";


    @POST("api/v1/account/signin")
    public Call<TokenPair> signIn(@Body UserSignIn user);

    @POST("api/v1/account/signup")
    public Call<String> signUp(@Body SignUpUser user);

    @GET("api/v1/account/getmyinfo")
    public Call<UserInfo> getMyInfo(@Header("Authorization") String token);

    @GET("api/v1/account/verification/{code}")
    public Call<TokenPair> confirmAcc(@Header("Authorization") String token,@Path("code") String code);

    @GET("api/v1/account/validtoken")
    public Call<String> validRefToken(@Header("Authorization") String token);

    @GET("api/v1/account/signout")
    public Call<String> signOut(@Header("Authorization") String token);

    @GET(baseControler+"accesstoken")
    public Call<TokenPair> getAccessToken(@Header("Authorization") String token);

    @GET("api/v1/img/getuserphoto/{id}")
    public Call<ResponseBody> getUserImage(@Path("id")long id);

    @POST("api/v1/img/setuserphoto")
    public Call<String> setUserPhoto(@Header("Authorization") String token,@Part MultipartBody.Part image);

}
