package com.example.socialapp.Activities.Auth.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.socialapp.Activities.Main.MainActivity;
import com.example.socialapp.Activities.Auth.Register.RegisterActivity;
import com.example.socialapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgPhoto;
    private EditText loginEmail, loginPass;
    private Button btnLogin, btnSignInSignOut;
    private ProgressBar loginProgressBar;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ini view
        imgPhoto = findViewById(R.id.login_imageView_profile);
        loginEmail = findViewById(R.id.loginMail);
        loginPass = findViewById(R.id.loginPass);
        btnLogin = findViewById(R.id.loginBtn);
        btnSignInSignOut = findViewById(R.id.signInSignOutButton);
        loginProgressBar = findViewById(R.id.loginProgressBar);

        loginProgressBar.setVisibility(View.INVISIBLE);

        btnSignInSignOut.setOnClickListener(this);

        btnLogin.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        // hide ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    // if you want to cancel Splash use it to check if user have account and login

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            updateUI();
//        }
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.signInSignOutButton:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.loginBtn:

                loginProgressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                final String email = loginEmail.getText().toString();
                final String password = loginPass.getText().toString();

                if (email.isEmpty() || password.isEmpty() ) {
                    showMessage("Please Verify All Field");
                    loginProgressBar.setVisibility(View.INVISIBLE);
                    btnLogin.setVisibility(View.VISIBLE);
                } else {
                    signIn(email, password);
                }
                break;
        }
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        updateUI();

                    } else {
                        loginProgressBar.setVisibility(View.INVISIBLE);
                        btnLogin.setVisibility(View.VISIBLE);
                        showMessage(Objects.requireNonNull(task.getException()).getMessage());

                    }

                });
    }

    private void updateUI() {

        startActivity(new Intent(LoginActivity.this, MainActivity.class));

    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    // if you want to hide Statue Bar
    private void hideStatueBar() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }


}


