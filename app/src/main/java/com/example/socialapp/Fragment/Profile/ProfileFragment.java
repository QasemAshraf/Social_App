package com.example.socialapp.Fragment.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialapp.Models.User;
import com.example.socialapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment implements View.OnClickListener{




    private ImageView imageAccount;
    private TextView nameOfAccount, tvEmail;
    private Button  btnEdit;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpView(view);
        return view;
    }


    private void setUpView(View view){


        imageAccount = view.findViewById(R.id.profile_edit_image);
        nameOfAccount = view.findViewById(R.id.profileName_TextView);
        tvEmail = view.findViewById(R.id.profileEmail_textView);
        btnEdit = view.findViewById(R.id.profileEditButton);


        btnEdit.setOnClickListener(this);

        // ini Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.profileEditButton:
//                startActivity(new Intent(getActivity(), EditProfileFragment.class));
                EditProfileFragment editProfileFragment= new EditProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_profile, editProfileFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    private void getProfileInfo(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("Users");
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}


