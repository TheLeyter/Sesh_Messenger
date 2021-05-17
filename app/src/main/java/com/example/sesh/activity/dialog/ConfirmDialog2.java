package com.example.sesh.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sesh.R;
import com.example.sesh.activity.MainActivity;
import com.example.sesh.activity.SignIn;
import com.example.sesh.models.TokenPair;
import com.example.sesh.service.ApiCoreService;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ConfirmDialog2 extends Dialog {

    private OtpView otp;
    private TextView errorText;
    private String token;

    public DialogCompleteListener completeListener;

    public interface DialogCompleteListener{
        void onDialogComplete(String refreshToken,String accessToken);
    }

    public ConfirmDialog2(@NonNull Context context,String token) {
        super(context, R.style.ConfirmAlertDialog);
        this.token = token;
        this.setContentView(R.layout.confirm_dialog);

        completeListener = (DialogCompleteListener) context;

        otp = findViewById(R.id.otp_view);
        errorText = findViewById(R.id.confirm_error_text);
        otp.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Log.d("ConfirmDialog2----->",otp);
                ApiCoreService.getInstance()
                        .getEndPoints()
                        .confirmAcc("Bearer "+token,otp)
                        .enqueue(new Callback<TokenPair>() {

                            @Override
                            public void onResponse(Call<TokenPair> call, Response<TokenPair> response) {

                                switch (response.code()){
                                    case 468:
                                        ConfirmDialog2.this.cancel();
                                        break;
                                    case 469:
                                        errorText.setText("Code not match!");
                                        break;
                                    case 200:
                                        errorText.setText(" ");
                                        completeListener.onDialogComplete(response.body().getRefreshToken(),response.body().getAccessToken());
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<TokenPair> call, Throwable t) {
                                Log.d("ConfirmDialog2----->",t.getMessage());
                            }
                        });
            }
        });
    }

}
