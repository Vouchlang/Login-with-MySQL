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

    TextInputEditText textInputLayoutFullname, textInputLayoutUsername, textInputLayoutPassword;
    Button buttonSignUp;
    TextView loginText;
    ProgressBar progressSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textInputLayoutFullname = findViewById(R.id.fullnameSignUp);
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
        final String fullname, username, password;
                fullname = String.valueOf(textInputLayoutFullname.getText());
                username = String.valueOf(textInputLayoutUsername.getText());
                password = String.valueOf(textInputLayoutPassword.getText());

                if (!fullname.equals("") && !username.equals("") && !password.equals("")) {
                    progressSignUp.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[3];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "password";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = fullname;
                            data[1] = username;
                            data[2] = password;
                            PutData putData = new PutData("http://192.168.1.243/LoginRegister/signup.php", "POST", field, data);
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