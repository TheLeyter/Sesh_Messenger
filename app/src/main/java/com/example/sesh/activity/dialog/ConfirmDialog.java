package com.example.sesh.activity.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sesh.R;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class ConfirmDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private OtpView otpView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d("ConfirmDialog-------->","onCreateDialog");
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity(),R.style.ConfirmAlertDialog)
                .setView(R.layout.confirm_dialog);

        return alert.create();
    }
    

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ConfirmDialog-------->","onCreateView");
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.confirm_dialog,container,false);


        otpView = v.findViewById(R.id.otp_view);

        if(otpView!=null){
            Log.d("ConfirmDialog-------->","otpView ok!");
        }
        else{
            Log.d("ConfirmDialog-------->","otpView error!");
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ConfirmDialog-------->","onStart");
        if(getView()==null){
            Log.d("ConfirmDialog-------->","NULL!!!!!");
        }
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Toast.makeText(getContext(),otp,Toast.LENGTH_LONG);
                Log.d("Otp Code------>",otp);
            }
        });
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

}
