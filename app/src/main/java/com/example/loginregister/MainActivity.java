package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView wuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wuser = findViewById(R.id.wuser);
        String user = getIntent().getStringExtra("student_id");
        wuser.setText(user);

    }
}