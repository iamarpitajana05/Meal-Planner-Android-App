package com.example.meal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    // 5 Minutes = 300,000 milliseconds
    private static final long IDLE_TIME = 1 * 60 * 1000;
    private Handler logoutHandler = new Handler(Looper.getMainLooper());
    private Runnable logoutRunnable = () -> performLogout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetTimer();
    }

    // This handles the "if user is touching/scrolling" requirement
    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        resetTimer();
    }

    private void resetTimer() {
        logoutHandler.removeCallbacks(logoutRunnable);
        logoutHandler.postDelayed(logoutRunnable, IDLE_TIME);
    }

    public void performLogout() {
        // Clear session flag
        SharedPreferences pref = getSharedPreferences("MealPrefs", MODE_PRIVATE);
        pref.edit().putBoolean("isLoggedIn", false).apply();

        Toast.makeText(this, "Session Expired due to inactivity", Toast.LENGTH_LONG).show();

        // Open login page and clear history
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logoutHandler.removeCallbacks(logoutRunnable);
    }
}