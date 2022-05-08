package com.example.loginregister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class signup extends AppCompatActivity {

    TextInputEditText textInputLayoutUsername, textInputLayoutPassword;
    Button buttonSignUp;
    TextView loginText;
    ProgressBar progressSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textInputLayoutUsername = findViewById(R.id.usernameSignUp);
        textInputLayoutPassword = findViewById(R.id.passwordSignUp);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        loginText = findViewById(R.id.loginText);
        progressSignUp = findViewById(R.id.progressSignUp);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
            }
        });

    }

    public void letTheUserLoggedIn(View view){
        if(!isConnected(signup.this)){
            showCustomDialog();
        }
        final String student_id, pwd;
        student_id = String.valueOf(textInputLayoutUsername.getText());
        pwd = String.valueOf(textInputLayoutPassword.getText());

                if (!student_id.equals("") && !pwd.equals("")) {
                    progressSignUp.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "student_id";
                            field[1] = "pwd";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = student_id;
                            data[1] = pwd;
                            PutData putData = new PutData("http:/192.168.1.6/LoginRegister/signup.php", "GET", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressSignUp.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success")){
                                        Toast.makeText(signup.this, result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(signup.this, login.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(signup.this, result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else{
                    Toast.makeText(signup.this, "All fields required!!!", Toast.LENGTH_SHORT).show();
                }
    }

    private boolean isConnected(signup sign){
        ConnectivityManager connectivityManager = (ConnectivityManager) sign.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo conWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo conMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((conWifi != null && conWifi.isConnected()) || (conMobile != null && conMobile.isConnected())){
            return true;
        }else{
            return false;
        }
    }

    private void showCustomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
        builder.setMessage("Please Connect to the internet to proceed!!")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), signup.class));
                finish();
            }
        });
        builder.show();
    }
}