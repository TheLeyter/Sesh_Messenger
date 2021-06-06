package com.example.sesh.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.sesh.App;
import com.example.sesh.R;
import com.example.sesh.models.TokenPair;
import com.example.sesh.service.ApiCoreService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends AppCompatActivity {

    private final String TAG = "SplashScreen----->";

    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        overridePendingTransition(R.anim.in,R.anim.out);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settingsEditor = settings.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!settings.getBoolean(getString(R.string.sp_is_login),false)){
                    Intent intent = new Intent(SplashScreen.this,SignIn.class);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();
                }
                else if(ApiCoreService.isOnline(SplashScreen.this)) {
                        ApiCoreService.getInstance()
                                .getEndPoints()
                                .getAccessToken("Bearer " + settings.getString(getString(R.string.sp_refresh_token), ""))
                                .enqueue(new Callback<TokenPair>() {
                                    @Override
                                    public void onResponse(Call<TokenPair> call, Response<TokenPair> response) {
                                        if (response.code() != 200) {
                                            Intent intent = new Intent(SplashScreen.this, SignIn.class);
                                            SplashScreen.this.startActivity(intent);
                                            SplashScreen.this.finish();
                                        } else {
//                                            settingsEditor.putString(getString(R.string.sp_refresh_token),response.body().getRefreshToken());
//                                            settingsEditor.putString(getString(R.string.sp_access_token),response.body().getAccessToken());

                                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                            SplashScreen.this.startActivity(intent);
                                            SplashScreen.this.finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<TokenPair> call, Throwable t) {
                                        Log.d(TAG, t.getMessage());
                                    }
                                });
                    }
                else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();
                }
                }
        },500);
    }
}