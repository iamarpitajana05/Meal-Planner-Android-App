package com.example.meal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button btnAction;
    TextView tvToggle;
    DBHelper db;
    boolean isLoginMode = true; // Track login or register mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnAction = findViewById(R.id.btnAction);
        tvToggle = findViewById(R.id.tvToggle);
        db = new DBHelper(this);

        btnAction.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            if (isLoginMode) {
                // Login
                if (db.checkUser(username, password)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Register
                if (db.registerUser(username, password)) {
                    Toast.makeText(LoginActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    isLoginMode = true;
                    btnAction.setText("Login");
                    tvToggle.setText("New user? Register here");
                } else {
                    Toast.makeText(LoginActivity.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvToggle.setOnClickListener(view -> {
            isLoginMode = !isLoginMode;
            btnAction.setText(isLoginMode ? "Login" : "Register");
            tvToggle.setText(isLoginMode ? "New user? Register here" : "Already have an account? Login here");
        });
    }
}
