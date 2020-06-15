package com.example.socialapp.Activities.Auth.Register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.socialapp.Activities.Auth.Login.LoginActivity;
import com.example.socialapp.Activities.Main.MainActivity;
import com.example.socialapp.Models.User;
import com.example.socialapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userName, userEmail, userPass, userPass2;
    private Button btnReg, btnLogin;
    private ProgressBar regProgressBar;
    private ImageView imgUserPhoto;

    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Ini View
        imgUserPhoto = findViewById(R.id.reg_imageView_profile);
        userName = findViewById(R.id.regName);
        userPass = findViewById(R.id.regPass);
        userPass2 = findViewById(R.id.regPass2);
        userEmail = findViewById(R.id.regMail);
        btnLogin = findViewById(R.id.goToLogin_btn);

        regProgressBar = findViewById(R.id.regProgressBar);
        btnReg = findViewById(R.id.regBtn);

        regProgressBar.setVisibility(View.INVISIBLE);

        // FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        // hide ActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();

    }

    private void createUserAccount(User user) {

        mAuth.createUserWithEmailAndPassword(user.getUserEmail(), user.getPass1())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String id = firebaseUser.getUid();
                        user.setUserID(id);
                        saveUserToDataBase(id, user);
                        showMessage("Account Created ");
                        updateUI();

                    } else {

                        showMessage("Account Creation Failed" + Objects.requireNonNull(task.getException()).getMessage());
                        btnReg.setVisibility(View.VISIBLE);
                        regProgressBar.setVisibility(View.INVISIBLE);

                    }

                });

    }

    private void saveUserToDataBase(String id, User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("Users").child(id);
        user.setPass1(null);
        user.setPass2(null);
        userRef.setValue(user);
        showMessage("Success");
        finish();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.regBtn:
                btnReg.setVisibility(View.INVISIBLE);
                regProgressBar.setVisibility(View.VISIBLE);

                final String photo = imgUserPhoto.toString();
                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPass.getText().toString();
                final String password2 = userPass2.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {

                    //Something Is Wrong : All Fields Must Be Field
                    showMessage("Please Verify All Fields");
                    btnReg.setVisibility(View.VISIBLE);
                    regProgressBar.setVisibility(View.INVISIBLE);


                } else if (!password.equals(password2)){
                showMessage("Should be Password equal confirm Password");
                btnReg.setVisibility(View.VISIBLE);
                regProgressBar.setVisibility(View.INVISIBLE);
            } else {

                    // TODO : VALIDATE
                User user = new User(photo, name, email, password, password2);
//
//                    User user = new User();
//                    user.setUserName(name);
//                    user.setUserEmail(email);
//                    user.setUserPhoto(photo);
//                    user.setPass1(password);
//                    user.setPass2(password2);

                    //Everything Is OK You Can Create User Account Now
                    createUserAccount(user);
                }
                break;

            case R.id.goToLogin_btn:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;


        }

    }

    private void updateUI() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    // Simple Method To Show Toast Message
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
