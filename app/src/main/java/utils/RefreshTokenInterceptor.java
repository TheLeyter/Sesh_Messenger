package utils;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.sesh.App;
import com.example.sesh.R;
import com.example.sesh.models.TokenPair;
import com.example.sesh.service.ApiCoreService;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;


public class RefreshTokenInterceptor implements Interceptor {

    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    public RefreshTokenInterceptor() {

        context = App.getContext();
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        settingsEditor = settings.edit();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        String refreshToken = settings.getString(context.getString(R.string.sp_refresh_token),"");
        Request req = chain.request();
        Response res = chain.proceed(req);

        if(res.code()==401){
            ApiCoreService.getInstance()
                    .getEndPoints()
                    .getAccessToken("Bearer "+refreshToken)
                    .enqueue(new Callback<TokenPair>() {
                        @Override
                        public void onResponse(Call<TokenPair> call, retrofit2.Response<TokenPair> response) {
                            if(response.code()==401){
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<TokenPair> call, Throwable t) {

                        }
                    });
        }

        return null;
    }
}
