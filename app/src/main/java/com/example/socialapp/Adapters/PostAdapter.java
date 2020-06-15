package com.example.socialapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.Activities.Main.MainActivity;
import com.example.socialapp.Activities.Post.PostDetailActivity;
import com.example.socialapp.Models.Post;
import com.example.socialapp.Models.User;
import com.example.socialapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context context;
    List<Post> posts;



    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.nameOfAccount.setText(posts.get(position).getUserName());
        holder.tvTitle.setText(posts.get(position).getTitle());
        holder.tvDescription.setText(posts.get(position).getDescription());
        holder.tvDate.setText(posts.get(position).getDate());
        Glide.with(context).load(posts.get(position).getUserImg()).into(holder.imageAccount);
        Glide.with(context).load(posts.get(position).getPicture()).into(holder.postImg);


//        holder.nameOfAccount.setText(firebaseUser.getDisplayName());
//        holder.tvTitle.setText(posts.get(position).getTitle());
//        holder.tvDescription.setText(posts.get(position).getDescription());
//        holder.tvDate.setText(posts.get(position).getDate());
//        Glide.with(context).load(firebaseUser.getPhotoUrl()).into(holder.imageAccount);
//        Glide.with(context).load(posts.get(position).getPicture()).into(holder.postImg);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageAccount;
        private TextView nameOfAccount;
        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView postImg;
        private TextView tvDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageAccount = itemView.findViewById(R.id.item_post_user_photo_imageView);
            nameOfAccount = itemView.findViewById(R.id.item_post_userName_TextView);
            postImg = itemView.findViewById(R.id.item_post_imageView);
            tvTitle = itemView.findViewById(R.id.item_post_title_textView);
            tvDescription = itemView.findViewById(R.id.item_post_des_textView);
            tvDate = itemView.findViewById(R.id.item_post_date_textView);

            //ini

            // Send Data To PostDetailActivity
            itemView.setOnClickListener(v -> {

                Intent postDetailActivity = new Intent(context, PostDetailActivity.class);
                int position = getAdapterPosition();


                postDetailActivity.putExtra("postKey", posts.get(position).getPostKey());
                postDetailActivity.putExtra("userPhoto", Glide.with(context).load(posts.get(position).getUserImg()).toString());
                postDetailActivity.putExtra("userName", posts.get(position).getUserName());
                postDetailActivity.putExtra("postDate",posts.get(position).getDate());
                postDetailActivity.putExtra("postImg", posts.get(position).getPicture());
                postDetailActivity.putExtra("Title", posts.get(position).getTitle());
                postDetailActivity.putExtra("Description", posts.get(position).getDescription());
                context.startActivity(postDetailActivity);


//                postDetailActivity.putExtra("postKey", posts.get(position).getPostKey());
//                postDetailActivity.putExtra("userPhoto", Glide.with(context).load(firebaseUser.getPhotoUrl()).toString());
//                postDetailActivity.putExtra("userName", firebaseUser.getDisplayName());
//                postDetailActivity.putExtra("postDate",posts.get(position).getDate());
//                postDetailActivity.putExtra("postImg", posts.get(position).getPicture());
//                postDetailActivity.putExtra("Title", posts.get(position).getTitle());
//                postDetailActivity.putExtra("Description", posts.get(position).getDescription());
//                context.startActivity(postDetailActivity);
//


            });
        }
    }

}
