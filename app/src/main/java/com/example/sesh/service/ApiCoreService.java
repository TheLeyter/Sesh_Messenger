package com.example.sesh.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.RefreshTokenInterceptor;

public class ApiCoreService {
    private static  ApiCoreService mInstance;
    private static String BASE_URL = "http://192.168.1.101:5000/";
    private Retrofit mRetrofit;
    private OkHttpClient client;

    private ApiCoreService(){
        client = new OkHttpClient.Builder()
                .addInterceptor(new RefreshTokenInterceptor())
                .build();

        mRetrofit = new  Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static ApiCoreService getInstance(){
        if(mInstance == null){
            mInstance = new ApiCoreService();
        }
        return mInstance;
    }

    public ApiEndPoints getEndPoints(){
        return mRetrofit.create(ApiEndPoints.class);
    }
}
