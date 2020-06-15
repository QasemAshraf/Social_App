package com.example.socialapp.Activities.Main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialapp.Activities.Auth.Login.LoginActivity;
import com.example.socialapp.Activities.Auth.Register.RegisterActivity;
import com.example.socialapp.Activities.Utilities.Utilities;
import com.example.socialapp.Fragment.Profile.ProfileFragment;
import com.example.socialapp.Fragment.Settings.SettingsFragment;
import com.example.socialapp.Fragment.home.HomeFragment;
import com.example.socialapp.Models.Post;
import com.example.socialapp.Models.User;
import com.example.socialapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private StorageReference mStorageRef;
    private Dialog popAddPost;

    private ImageView popupProfileImg, popupPostImg, popupAddBtn;
    private EditText popupTitle, popupDescription;
    private ProgressBar popupClickProgress;

    static int PerReqCode = 2;
    static int GALLERY_PICK = 2;
    private Uri pickedImgUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Ini Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        // Ini Popup

        iniPopup();
        setupPopupImgClick();

        // Ini FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> popAddPost.show());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNaveHeader();

        // set the HomeFragment as the default one

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }


    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // Ini widgets
        popupProfileImg = popAddPost.findViewById(R.id.popup_imageView_profile);
        popupTitle = popAddPost.findViewById(R.id.popup_title_editText);
        popupDescription = popAddPost.findViewById(R.id.popup_des_editText);
        popupPostImg = popAddPost.findViewById(R.id.popup_imageView_post);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add_post_imageView);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        //Load Current User Profile Photo
        Glide.with(MainActivity.this).load(firebaseUser.getPhotoUrl()).into(popupProfileImg);

        // Add Post Click Listener

        popupAddBtn.setOnClickListener(v -> {

            popupAddBtn.setVisibility(View.INVISIBLE);
            popupClickProgress.setVisibility(View.VISIBLE);


            if (   !popupTitle.getText().toString().isEmpty()
                && !popupDescription.getText().toString().isEmpty()
                && pickedImgUri != null ) {

                uploadPost();

            }else {
                showMessage("Please input all fields");
                popupAddBtn.setVisibility(View.VISIBLE);
                popupClickProgress.setVisibility(View.INVISIBLE);
            }

        });

    }


    private void setupPopupImgClick() {

        popupPostImg.setOnClickListener(v -> {

            if (Build.VERSION.SDK_INT >= 22){

                checkAccessImagesPermission();

            }else {
                openGallery();
            }
        });
    }

    private void checkAccessImagesPermission(){
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PerReqCode);
        }else {
            openGallery();
        }
    }

    private void openGallery() {

        // TODo Open gallery By Intent

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PerReqCode){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }else {
                showMessage("Permission denied!");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            try {
                assert data != null;
                pickedImgUri = data.getData();
                InputStream imgStream = this.getContentResolver().openInputStream(pickedImgUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imgStream);
                popupPostImg.setImageBitmap(selectedImage);

            }catch (Exception e){

                showMessage("Something went wrong " + e.getMessage());

            }

        }else {
            showMessage("You haven't Picked Image");
        }


//        if (resultCode == RESULT_OK && requestCode == GALLERY_PICK && data !=null){
//            // The User has Successfully Picked an image
//            pickedImgUri = data.getData();
//            popupPostImg.setImageURI(pickedImgUri);
//
//        }
    }


    private void uploadPost() {

        String imgPath = UUID.randomUUID().toString() + ".jpg";
        mStorageRef.child("PostImages").child(imgPath)
                .putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {

            mStorageRef.child("PostImages").child(imgPath).getDownloadUrl().addOnSuccessListener(uri -> {


                        final String imageDownloadLink = uri.toString();
                        final String title = popupTitle.getText().toString();
                        final String description = popupDescription.getText().toString();
                        final String UPId = firebaseUser.getUid();
                        final String name = firebaseUser.getDisplayName();
                        final String userImg = Glide.with(this).load(firebaseUser.getPhotoUrl()).toString();
//
//                        post.setUserPostId(UPId);
//                        post.setUserName(name);
//                        post.setUserImg(userImg);
//                        post.setTitle(title);
//                        post.setDate(Utilities.getCurrentDate());
//                        post.setDescription(description);
//                        post.setPicture(imageDownloadLink);

                        Post post = new Post(name, userImg, UPId, title, description, imageDownloadLink, Utilities.getCurrentDate());

                        // Add post to firebase database
                        addPostToDB(post);
                    });

                }).addOnFailureListener(exception -> {

                    showMessage(exception.getMessage());
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);


                });
    }

    // Add Post data to FirebaseDatabase

    private void addPostToDB(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postRef = database.getReference().child("Posts");

        // Add unique Key to Post
        String postRefKey = postRef.push().getKey();
        post.setPostKey(postRefKey);
        assert postRefKey != null;
        postRef.child(postRefKey).setValue(post).addOnSuccessListener(aVoid -> {

            showMessage("Post Added");
            popupClickProgress.setVisibility(View.INVISIBLE);
            popupAddBtn.setVisibility(View.VISIBLE);
            popAddPost.dismiss();

        }).addOnFailureListener(e -> {

            showMessage("Fail To Add Post : " + e.getMessage());
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
//            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home){
            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new HomeFragment()).commit();

        }else if (id == R.id.nav_profile){
            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new  ProfileFragment()).commit();

        }else if (id == R.id.nav_settings){
            getSupportActionBar().setTitle("Settings");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SettingsFragment()).commit();
        }else if (id == R.id.nav_signOut){
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        return true;
    }

    public void updateNaveHeader(){

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.navUserName);
        TextView navUserEmail = headerView.findViewById(R.id.navUserMail);
        ImageView navUserPhoto = headerView.findViewById(R.id.navUserPhoto);


        navUserName.setText(firebaseUser.getDisplayName());
        navUserEmail.setText(firebaseUser.getEmail());
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(navUserPhoto);
//        try {
//            navUserName.setText(user.getUserName());
//            navUserEmail.setText(user.getUserEmail());
//            Glide.with(this).load(user.getUserPhoto()).into(navUserPhoto);
//        }catch (Exception e){
//
//            showMessage("Error : " + e.getMessage());
//        }

    }


    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}


