package com.example.sesh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sesh.R;
import com.example.sesh.models.UserInfo;
import com.example.sesh.service.ApiCoreService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity---->";

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView menuAvatar;
    private TextView menuNickName;
    private TextView menuFullName;
    private DrawerLayout drawer;

    private SharedPreferences settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        settings = PreferenceManager.getDefaultSharedPreferences(this);


        View headerUser = navigationView.getHeaderView(0);
        menuNickName = (TextView)headerUser.findViewById(R.id.menu_nick_name);
        menuFullName = (TextView)headerUser.findViewById(R.id.menu_full_user_name);

        ApiCoreService.getInstance()
                .getEndPoints()
                .getMyInfo("Bearer "+ settings.getString(getString(R.string.sp_access_token)," "))
                .enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        Log.d(TAG,response.toString());
                        Log.d(TAG,response.body().toString());
                        if(response.body().getAvatar()==null){
                            Toast.makeText(MainActivity.this,"Avatar null",Toast.LENGTH_LONG).show();
                        }
                        menuNickName.setText(response.body().getUsername());
                        menuFullName.setText(response.body().getFirstName() +" " + response.body().getLastName());
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void dialogClickYes(){
        ApiCoreService.getInstance()
                .getEndPoints()
                .signOut("Bearer " + settings.getString(getString(R.string.sp_refresh_token),""))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d(TAG,Integer.toString(response.code()));

                        if (response.code()!=200){
                            Log.d(TAG,Integer.toString(response.code()));
                            return;
                        }
                        else{
                            settings.edit().putBoolean(getString(R.string.sp_is_login),false);
                            settings.edit().remove(getString(R.string.sp_refresh_token)).apply();

                            Intent logOut = new Intent(MainActivity.this,SignIn.class);
                            MainActivity.this.startActivity(logOut);
                            MainActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d(TAG,t.getMessage());
                    }
                });
    }

    public void onClickSignOut(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you exit from "+menuNickName.getText()+" account?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(TAG,"Click Yes");

                        ApiCoreService.getInstance()
                                .getEndPoints()
                                .signOut("Bearer " + settings.getString(getString(R.string.sp_refresh_token),""))
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        Log.d(TAG,Integer.toString(response.code()));

                                        if (response.code()!=200){
                                            Log.d(TAG,Integer.toString(response.code()));
                                            return;
                                        }
                                        else{
                                            settings.edit().putBoolean(getString(R.string.sp_is_login),false);
                                            settings.edit().remove(getString(R.string.sp_refresh_token)).apply();

                                            Intent logOut = new Intent(MainActivity.this,SignIn.class);
                                            MainActivity.this.startActivity(logOut);
                                            MainActivity.this.finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d(TAG,t.getMessage());
                                    }
                                });


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        drawer.closeDrawers();

    }
}