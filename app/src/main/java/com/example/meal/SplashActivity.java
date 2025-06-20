package com.example.meal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView appName = findViewById(R.id.app_name);
        ImageView homeImage = findViewById(R.id.home_image);

        // Fade-in animation for text
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(2000);
        appName.startAnimation(fadeIn);

        // Scale animation for image
        Animation scaleAnimation = new ScaleAnimation(
                0.5f, 1.0f, 0.5f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        homeImage.startAnimation(scaleAnimation);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000); // 3-second delay
    }
}
