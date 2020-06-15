package com.example.socialapp.Activities.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Window;
import android.view.WindowManager;

import com.example.socialapp.Activities.Auth.Register.RegisterActivity;
import com.example.socialapp.Activities.Main.MainActivity;
import com.example.socialapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        new Handler().postDelayed(() -> {

            if (firebaseUser !=null){

                startActivity(new Intent(SplashActivity.this, MainActivity.class));

            }else {

                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));

            }
        },2000);

        hideStatueBar();

    }

    // if you want to hide Statue Bar
    private void hideStatueBar() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

}
