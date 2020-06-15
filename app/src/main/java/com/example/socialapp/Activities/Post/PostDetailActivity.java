package com.example.socialapp.Activities.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialapp.Activities.Utilities.Utilities;
import com.example.socialapp.Adapters.CommentAdapter;
import com.example.socialapp.Fragment.Profile.EditProfileFragment;
import com.example.socialapp.Models.Comment;
import com.example.socialapp.Models.User;
import com.example.socialapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostDetailActivity extends AppCompatActivity implements View.OnClickListener {

    static String COMMENT_KEY = "Comment";
    String PostKey;
    ImageView imgUserPost, postImg, imgUserComment;
    TextView tvUserName, tvDate, tvTitle, tvDes;
    EditText editComment;
    Button btnAddComment;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        hidStatueBar();
        iniView();
        getData();
    }


    private void hidStatueBar() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
    }


    private void iniView() {

        RvComment = findViewById(R.id.post_detail_rv);

        imgUserPost = findViewById(R.id.post_detail_user_photo_imageView);
        postImg = findViewById(R.id.post_detail_imageView);
        imgUserComment = findViewById(R.id.post_detail_user_comment_imageView);
        tvUserName = findViewById(R.id.post_detail_userName_TextView);
        tvDate = findViewById(R.id.post_detail_date_textView);
        tvTitle = findViewById(R.id.post_detail_title_textView);
        tvDes = findViewById(R.id.post_detail_des_textView);
        editComment = findViewById(R.id.post_detail_write_comment_editText);
        btnAddComment = findViewById(R.id.post_detail_add_comment_button);


        btnAddComment.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(getApplicationContext(), comments);
        RvComment.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RvComment.setAdapter(commentAdapter);



    }

    private void getData() {

        // get Post Id
        PostKey = getIntent().getExtras().getString("postKey");

        String userImg = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userImg).into(imgUserPost);

        String userName = getIntent().getExtras().getString("userName");
        tvUserName.setText(userName);

        String date = getIntent().getExtras().getString("postDate");
        tvDate.setText(date);

        String postImage = getIntent().getExtras().getString("postImg");
        Glide.with(this).load(postImage).into(postImg);

        String title = getIntent().getExtras().getString("Title");
        tvTitle.setText(title);

        String description = getIntent().getExtras().getString("Description");
        tvDes.setText(description);

        // Set Comment User Img
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgUserComment);

        iniRvComment();


    }


    @Override
    public void onClick(View v) {

        btnAddComment.setVisibility(View.INVISIBLE);

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
        String comment_content = editComment.getText().toString();
        String uName = firebaseUser.getDisplayName();
        String uImg = Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgUserComment).toString();
        String commentDate = Utilities.getCurrentDate();

        Comment comment = new Comment(comment_content, uName, uImg, commentDate);

//        comment.setContent(comment_content);
//        comment.setUserName(uName);
//        comment.setUserImg(uImg);
//        comment.setCommentDate(commentDate);

        commentRef.setValue(comment).addOnSuccessListener(aVoid -> {

            showMessage("Comment Added");
            editComment.setText("");
            btnAddComment.setVisibility(View.VISIBLE);


        }).addOnFailureListener(e -> {

            showMessage("Fail To Add Comment : " + e.getMessage());
        });

        // update show your comment
        iniRvComment();
    }


    private void iniRvComment() {


        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snap : dataSnapshot.getChildren()){

                    Comment comment = snap.getValue(Comment.class);
                    comments.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
