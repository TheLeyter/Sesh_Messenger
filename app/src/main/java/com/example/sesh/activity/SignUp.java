package com.example.sesh.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sesh.R;
import com.example.sesh.activity.dialog.ConfirmDialog2;
import com.example.sesh.models.SignUpUser;
import com.example.sesh.service.ApiCoreService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity implements ConfirmDialog2.DialogCompleteListener{

    private final String TAG = "SignUp----->";

    private SharedPreferences settings;
    private SharedPreferences.Editor settingsEditor;

    private SignUpUser user;

    private ConfirmDialog2 dialog;

    private EditText userName;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.in,R.anim.out);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settingsEditor = settings.edit();

        userName = (EditText)findViewById(R.id.EditTextLogin_SignUp);
        email = (EditText)findViewById(R.id.EditTextEmail_SignUp);
        firstName = (EditText)findViewById(R.id.editTextFirstName);
        lastName = (EditText)findViewById(R.id.editTextLastName);
        password = (EditText)findViewById(R.id.EditTextPassword_SignUp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.in,R.anim.out);
    }

    public void signUp_click(View view) {
        user = new SignUpUser(userName.getText().toString(),email.getText().toString(),firstName.getText().toString(),lastName.getText().toString(),password.getText().toString());

        ApiCoreService.getInstance()
                .getEndPoints()
                .signUp(user)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        switch(response.code()){
                            case 400:
                                Toast.makeText(SignUp.this,"Bad request",Toast.LENGTH_LONG).show();
                                break;
                            case 462:
                                Toast.makeText(SignUp.this,"Error Username",Toast.LENGTH_LONG).show();
                                break;
                            case 461:
                                Toast.makeText(SignUp.this,"Error Email",Toast.LENGTH_LONG).show();
                                break;
                            case 463:
                                Toast.makeText(SignUp.this,"Error Password",Toast.LENGTH_LONG).show();
                                break;
                            case 466:
                                Toast.makeText(SignUp.this,"Error Last Name",Toast.LENGTH_LONG).show();
                                break;
                            case 467:
                                Toast.makeText(SignUp.this,"Error First Name",Toast.LENGTH_LONG).show();
                                break;
                            case 470:
                                String confirmToken;
                                try {
                                    confirmToken = response.errorBody().string();
                                } catch (IOException e) {
                                    Toast.makeText(SignUp.this,"Confirm token error!",Toast.LENGTH_LONG);
                                    break;
                                }
                                Log.d(TAG,confirmToken);
                                dialog = new ConfirmDialog2(SignUp.this,confirmToken);
                                dialog.show();
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
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

        Intent main = new Intent(SignUp.this,MainActivity.class);
        main.putExtra("Tokens",new String[]{refreshToken,accessToken});
        dialog.dismiss();
        SignUp.this.startActivity(main);
        SignUp.this.finish();
    }
}