package utils;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.sesh.App;
import com.example.sesh.R;
import com.example.sesh.activity.SignIn;
import com.example.sesh.models.TokenPair;
import com.example.sesh.service.ApiCoreService;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;


public class RefreshTokenInterceptor implements Interceptor {

    private final String TAG = "HttpInterceptor----->";

    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;
    private TokenPair tokenPair;

    public RefreshTokenInterceptor() {
        settings = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        settingsEditor = settings.edit();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        String refreshToken = settings.getString(App.getContext().getString(R.string.sp_refresh_token),"");
        Request req = chain.request();
        req.newBuilder().addHeader("User-Agent","Android");
        Response res = chain.proceed(req);


        if(res.code()==401){

            ApiCoreService.getInstance()
                    .getEndPoints()
                    .getAccessToken("Bearer "+refreshToken)
                    .enqueue(new Callback<TokenPair>() {
                        @Override
                        public void onResponse(Call<TokenPair> call, retrofit2.Response<TokenPair> response) {

                            if(response.code()==401){
                                settingsEditor.putBoolean(App.getContext().getString(R.string.sp_is_login),false);
                                Intent intent = new Intent(App.getContext(), SignIn.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                App.getContext().startActivity(intent);
                            }
                            else if(response.code()==200){
                                tokenPair = response.body();
                                settingsEditor.putString(App.getContext().getString(R.string.sp_refresh_token),response.body().getRefreshToken());
                                settingsEditor.putString(App.getContext().getString(R.string.sp_access_token),response.body().getAccessToken());
                            }
                            else{
                                Toast.makeText(App.getContext(),"Server internal error",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TokenPair> call, Throwable t) {
                            Log.d(TAG,t.getMessage());
                        }
                    });

            req = req.newBuilder()
                    .header("Authorization","Bearer "+settings.getString(App.getContext().getString(R.string.sp_access_token),""))
                    .build();
            try {
                if(res != null) res.close();

            } catch (Exception exception){

            }

            res = chain.proceed(req);

        }

        return res;
    }
}
