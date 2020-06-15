package com.example.socialapp.Fragment.Profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.socialapp.Activities.Main.MainActivity;
import com.example.socialapp.Models.User;
import com.example.socialapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment implements View.OnClickListener {



    private ImageView imgProfile;
    private EditText editName, editEmail;
    private Button btnEditPhoto, btnSave;
    private ProgressBar btnProgressBar;

    private User user;

    static int PerReqCode = 1;
    static int GALLERY_PICK = 1;

    private Uri pickedImgUri;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private StorageReference mStorageRef;


    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setUpView(view);
        return view;
    }

    private void setUpView(View view) {
        imgProfile = view.findViewById(R.id.profile_edit_image);
        editName = view.findViewById(R.id.profile_edit_NameEditText);
//        editEmail = view.findViewById(R.id.profile_Edit_Email_EditText);
        btnEditPhoto = view.findViewById(R.id.profileEitPhotoButton);
        btnSave = view.findViewById(R.id.profileSaveButton);
        btnProgressBar = view.findViewById(R.id.edit_profile_progressBar);

        btnProgressBar.setVisibility(View.INVISIBLE);
        btnEditPhoto.setOnClickListener(this);
        btnSave.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();


    }


    private void checkAndRequestForPermission() {

        int permission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PerReqCode);
        }else {
            openGallery();
        }

    }

    private void openGallery() {
        // TODO : open gallery By Intent

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_PICK);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PerReqCode){

            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                openGallery();

            }else {showMessage("permission denied");}
        }
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK ){

            try {

                pickedImgUri = data.getData();
                InputStream imgStream = getActivity().getContentResolver().openInputStream(pickedImgUri);
                Bitmap selectImage = BitmapFactory.decodeStream(imgStream);
                imgProfile.setImageBitmap(selectImage);

            }catch (Exception e){ showMessage("Something went wrong " + e.getMessage()); }

        }else { showMessage("You haven't Picked Image"); }
    }



    // Update User Name And Photo
    private void updateUserInfo() {
        // Upload User Photo To Firebase Storage And Get Uri
//        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("users_Photos");
//        final StorageReference imageFilePath = mStorageRef.child(pickedImgUri.getLastPathSegment());
//

        String imgEditPath = UUID.randomUUID().toString() + ".jpg";

        mStorageRef.child("userImages").child(imgEditPath)
                .putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
             //Image Upload Successfully
            showMessage("Image Upload Successfully");

            mStorageRef.child("userImages").child(imgEditPath)
                    .getDownloadUrl().addOnSuccessListener(uri -> {

               final String name = editName.getText().toString();
               final String newPhotoProfile = uri.getLastPathSegment();
//            String email = editEmail.getText().toString();

                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(Uri.parse(newPhotoProfile))
                        .build();

                firebaseUser.updateProfile(profileUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // User Info Updated Successfully
                                showMessage("Edit Complete");
                                updateUI();
                            }
                        });

            });
        });




                }


        // Upload User Photo To Firebase Storage And Get Uri
//        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("users_Photos");
//        final StorageReference imageFilePath = mStorageRef.child(pickedImgUri.getLastPathSegment());
//
//        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
//            // Image Upload Successfully
//            showMessage("Image Upload Successfully");
//
//            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {

//                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
//                        .setDisplayName(name)
//                        .setPhotoUri(uri)
//                        .build();
//
//                firebaseUser.updateProfile(profileUpdate)
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful()) {
//                                // User Info Updated Successfully
//                                showMessage("Edit Complete");
//                                updateUI();
//                            }
//                        });
//
//            });
//        });


    private void updateUI() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    // Simple Method To Show Toast Message
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.profileSaveButton:

                btnSave.setVisibility(View.INVISIBLE);
                btnProgressBar.setVisibility(View.VISIBLE);

                updateUserInfo();
//                btnEditPhoto.setVisibility(View.VISIBLE);
//                btnProgressBar.setVisibility(View.INVISIBLE);
                break;
            case R.id.profileEitPhotoButton:
                checkAndRequestForPermission();
                break;
        }

    }
}
