package com.example.socialapp.Adapters;

import android.content.Context;
import android.media.Image;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialapp.Models.Comment;
import com.example.socialapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<Comment> comments;
    private FirebaseAuth mAuth;


    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {


        Glide.with(context).load(comments.get(position).getUserImg()).into(holder.imgUser);
        holder.tvName.setText(comments.get(position).getUserName());
        holder.tvContent.setText(comments.get(position).getContent());
        holder.tvDate.setText(comments.get(position).getCommentDate());


//        Glide.with(context).load(firebaseUser.getPhotoUrl()).into(holder.imgUser);
//        holder.tvName.setText(firebaseUser.getDisplayName());
//        holder.tvContent.setText(comments.get(position).getContent());
//        holder.tvDate.setText(comments.get(position).getCommentDate());


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView imgUser;
        TextView tvName, tvContent, tvDate;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.comment_user_imageView);
            tvName = itemView.findViewById(R.id.comment_user_name_textView);
            tvContent = itemView.findViewById(R.id.comment_content_textView);
            tvDate = itemView.findViewById(R.id.comment_date_textView);


        }
    }

}
