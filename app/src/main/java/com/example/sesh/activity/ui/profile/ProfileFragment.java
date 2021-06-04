package com.example.sesh.activity.ui.profile;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sesh.R;
import com.example.sesh.models.UserInfo;
import com.example.sesh.service.ApiCoreService;
import com.example.sesh.service.ApiEndPoints;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private final String TAG = "ProfileFragment----->";

    private ImageView img;
    private TextView userName;
    private TextView email;
    private TextView firstName;
    private TextView lastName;
    private FloatingActionButton addPhotoBtn;

    private ApiEndPoints api;
    private SharedPreferences settings;
    private UserInfo user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = ApiCoreService.getInstance().getEndPoints();
        settings = PreferenceManager.getDefaultSharedPreferences(getContext());


        api.getMyInfo("Bearer " + settings.getString(getString(R.string.sp_access_token), ""))
                .enqueue(new Callback<UserInfo>() {
                    @Override
                    public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                        if (response.code() == 200) {
                            user = response.body();
                            userName.setText(user.getUsername());
                            firstName.setText(user.getFirstName());
                            lastName.setText(user.getLastName());
                            api.getUserImage(user.getId())
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.code() == 200) {
                                                InputStream is = response.body().byteStream();
                                                Bitmap bmp = BitmapFactory.decodeStream(is);
                                                img.setImageBitmap(bmp);
                                            } else {
                                                img.setImageResource(R.drawable.img_default_avatar);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.d(TAG, t.getMessage());
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Error code - " + Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfo> call, Throwable t) {
                        Log.d(TAG, t.getMessage());
                    }
                });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        img = v.findViewById(R.id.profile_user_photo);
        userName = v.findViewById(R.id.profile_UserName);
        email = v.findViewById(R.id.profile_email);
        firstName = v.findViewById(R.id.profile_firstName);
        lastName = v.findViewById(R.id.profile_lastName);
        addPhotoBtn = v.findViewById(R.id.profile_addPhoto_bytton);

        addPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TedBottomPicker.with(getActivity())
                        .showTitle(false)
                        .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                            @Override
                            public void onImageSelected(Uri uri) {

                                File imgFile = new File(uri.getPath());

                                RequestBody requestBody = RequestBody.create(imgFile, MediaType.parse("multipart/form-data"));

                                MultipartBody.Part body = MultipartBody.Part.createFormData("img", imgFile.getName(), requestBody);

                                Log.d(TAG, uri.getPath());
                                ApiCoreService.getInstance()
                                        .getEndPoints()
                                        .setUserPhoto("Bearer " + settings.getString(getString(R.string.sp_access_token), ""), body)
                                        .enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                if (response.code() != 200) {
                                                    Toast.makeText(getContext(), "Failure to uploading img", Toast.LENGTH_LONG);
                                                } else img.setImageURI(uri);
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Log.d(TAG, "Error to uploading img!!!");
                                                Toast.makeText(getContext(), "Failure to uploading img", Toast.LENGTH_LONG);
                                            }
                                        });
                            }
                        });
            }
        });

        return v;
    }

}