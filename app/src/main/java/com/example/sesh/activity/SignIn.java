package com.example.sesh.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sesh.R;
import com.example.sesh.activity.dialog.ConfirmDialog2;
import com.example.sesh.models.TokenPair;
import com.example.sesh.models.UserSignIn;
import com.example.sesh.service.ApiCoreService;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignIn extends AppCompatActivity implements ConfirmDialog2.DialogCompleteListener {
    private final String TAG = "SignIn----->";

    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    private boolean passwordHide = true;
    private EditText passwordEdit;
    private EditText loginEdit;

    private ConfirmDialog2 dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);

        overridePendingTransition(R.anim.in,R.anim.out);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settingsEditor = settings.edit();
        passwordEdit = (EditText) findViewById(R.id.EditTextPassword_SignUp);
        loginEdit = (EditText) findViewById(R.id.EditTextEmail_SignUp);

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    passwordEdit.setText(result);
                    passwordEdit.setSelection(result.length());
                }
            }
        });

        loginEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    loginEdit.setText(result);
                    loginEdit.setSelection(result.length());
                }
            }
        });
    }


    public void onHideButtonClick(View view) {
        ImageButton eyeButton = (ImageButton) findViewById(view.getId());
        if(passwordHide){
            passwordHide = false;
            eyeButton.setImageDrawable(getDrawable(R.drawable.eye_0ff));
            passwordEdit.setTransformationMethod(null);
        }
        else{
            passwordHide = true;
            eyeButton.setImageDrawable(getDrawable(R.drawable.ic_baseline_remove_red_eye_24));
            passwordEdit.setTransformationMethod(new PasswordTransformationMethod());
        }
    }

    public void onClickSignUp(View view) {
        Intent signUp = new Intent(this,SignUp.class);
        this.startActivity(signUp);
        overridePendingTransition(R.anim.in,R.anim.out);
    }

    public void onLoginClick(View view) {
        ApiCoreService.getInstance()
                .getEndPoints()
                .signIn(new UserSignIn(loginEdit.getText().toString(),passwordEdit.getText().toString()))
                .enqueue(new Callback<TokenPair>() {
                    @Override
                    public void onResponse(Call<TokenPair> call, Response<TokenPair> response) {
                        switch (response.code()){
                            case 462:
                                Toast.makeText(SignIn.this,"Username",Toast.LENGTH_LONG).show();
                                break;
                            case 463:
                                Toast.makeText(SignIn.this,"Password",Toast.LENGTH_LONG).show();
                                break;
                            case 470:
                                String confirmToken;
                                try {
                                    confirmToken = response.errorBody().string();
                                } catch (IOException e) {
                                    Toast.makeText(SignIn.this,"Confirm error!",Toast.LENGTH_LONG);
                                    break;
                                }
                                Log.d(TAG,confirmToken);
                                dialog = new ConfirmDialog2(SignIn.this,confirmToken);
                                dialog.show();
                                break;
                            case 200:
                                Log.d(TAG,response.body().getAccessToken() + "\n" + response.body().getRefreshToken());
                                settingsEditor.putString(getString(R.string.sp_refresh_token),response.body().getRefreshToken());
                                settingsEditor.putString(getString(R.string.sp_access_token),response.body().getAccessToken());
                                settingsEditor.putBoolean(getString(R.string.sp_is_login),true);
                                settingsEditor.apply();
                                Intent main = new Intent(SignIn.this,MainActivity.class);
                                main.putExtra("Tokens",new String[]{response.body().getRefreshToken(),response.body().getAccessToken()});
                                SignIn.this.startActivity(main);
                                SignIn.this.finish();
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<TokenPair> call, Throwable t) {
                        Log.d(TAG,t.getMessage());
                    }
                  });
    }

    @Override
    public void onDialogComplete(String refreshToken, String accessToken) {
        Log.d(TAG,refreshToken + " | " + accessToken);

        settingsEditor.putString(getString(R.string.sp_refresh_token),refreshToken);
        settingsEditor.putString(getString(R.string.sp_access_token),accessToken);
        settingsEditor.apply();

        Intent main = new Intent(SignIn.this,MainActivity.class);
        main.putExtra("Tokens",new String[]{refreshToken,accessToken});
        dialog.dismiss();
        SignIn.this.startActivity(main);
        SignIn.this.finish();
    }
}