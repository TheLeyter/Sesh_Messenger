package com.example.sesh.service;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.sesh.App;
import com.example.sesh.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.RefreshTokenInterceptor;

public class ApiCoreService {
    private static  ApiCoreService mInstance;
    private static String BASE_URL = App.getContext().getString(R.string.base_auth_api_url);
    private Retrofit mRetrofit;
    private OkHttpClient client;
    private Gson gson;

    private ApiCoreService(){
        client = new OkHttpClient.Builder()
                .addInterceptor(new RefreshTokenInterceptor())
                .build();

        gson = new GsonBuilder().setLenient().create();

        mRetrofit = new  Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
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

    public static boolean isOnline(Context context){

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            Toast.makeText(context,"Online mode!",Toast.LENGTH_LONG).show();
            return true;
        }

        Toast.makeText(context,"Offline mode!",Toast.LENGTH_LONG).show();
        return false;

    }
}
