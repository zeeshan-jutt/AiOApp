package com.converter.aiofinal;


import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.Viewholder> {
    Context allmembers;
    ArrayList<Users> usersArrayList;



    public MembersAdapter(AllMembers allMembers, ArrayList<Users> usersArrayList) {
        this.allmembers = allMembers;
        this.usersArrayList = usersArrayList;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(allmembers).inflate(R.layout.memberitems,parent, false);
        return new Viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Users users = usersArrayList.get(position);
        holder.user_name.setText(users.name);
        holder.service.setText(users.service);
        Picasso.get().load(users.imageUri).into(holder.user_img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(allmembers, Details.class);
                intent.putExtra("name", users.getName());
                intent.putExtra("ReciverImage", users.getImageUri());
                intent.putExtra("phone", users.getPhone());
                intent.putExtra("adress", users.getAddress());
                intent.putExtra("service", users.getService());
//                intent.putExtra("emailId", users.getEmailId());
                allmembers.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class Viewholder extends RecyclerView.ViewHolder {

            CircleImageView user_img;
            TextView user_name;
            TextView service;

            public Viewholder(@NonNull View itemView) {
                super(itemView);
                user_img = itemView.findViewById(R.id.user_img);
                user_name = itemView.findViewById(R.id.user_name);
                service = itemView.findViewById(R.id.serviceName);
            }
        }

    }
