package com.amigos.tadjir.adapter;

import static com.amigos.tadjir.ColorChanger.setNewColor;
import static com.amigos.tadjir.ColorChanger.setNewTextColor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amigos.tadjir.ColorChanger;
import com.amigos.tadjir.Model.HomeModel;
import com.amigos.tadjir.R;
import com.amigos.tadjir.fragments.PLUS;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private static List<HomeModel> list;
    Context context;

    public HomeAdapter(List<HomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_items,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        holder.userNameTv.setText(list.get(position).getUserName());
        holder.timeTv.setText(list.get(position).getTimestamp());
holder.cityt.setText(list.get(position).getCity());
        String count=list.get(position).getLikeCount();

        if (Float.parseFloat(count)>0){


            holder.likeCounttv.setText(count+" Star");
        }
        else
            holder.likeCounttv.setVisibility(View.GONE);






        Random random=new Random();
        int color=Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256))    ;
        Glide.with(context.getApplicationContext()).load(list.get(position).getProfileImage()).placeholder(R.drawable.ic_person)
                .timeout(6500).into(holder.profileImage);
        Glide.with(context.getApplicationContext()).load(list.get(position).getPostImage()).placeholder(new ColorDrawable(color))
                .timeout(7000).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HomeHolder extends RecyclerView.ViewHolder{
private CircleImageView profileImage;
private TextView userNameTv,timeTv,likeCounttv,cityt;
private ImageView imageView;
private ImageButton likeBtn,commentBtn,shareBtn;

        public HomeHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.profileImage);
            imageView=itemView.findViewById(R.id.imageView);
            userNameTv=itemView.findViewById(R.id.nameTv);



            timeTv=itemView.findViewById(R.id.timeTv);
            likeBtn=itemView.findViewById(R.id.likeBtn);
            likeCounttv=itemView.findViewById(R.id.likeCountTv);
            commentBtn=itemView.findViewById(R.id.commentBtn);
            shareBtn=itemView.findViewById(R.id.shareBtn);
cityt=itemView.findViewById(R.id.city);
    //       setNewTextColor("#ff68AE", userNameTv,timeTv,cityt);

imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(itemView.getContext(), PLUS.class);

         intent.putExtra("id", list.get(getAdapterPosition()).getUid());

        itemView.getContext().startActivity(intent);
    }
});
        }
    }
}
