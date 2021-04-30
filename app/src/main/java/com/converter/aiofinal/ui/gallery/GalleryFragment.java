package com.converter.aiofinal.ui.gallery;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.converter.aiofinal.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GalleryFragment extends Fragment {

    CircleImageView profile_image;
    TextView profile_name, profile_email,profile_phn,profile_add,profile_serv;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selctedImageUri;
    String emailId;
    ProgressDialog progressDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        profile_image = root.findViewById(R.id.profile_image);
        profile_name =root. findViewById(R.id.profile_name);
        profile_email = root.findViewById(R.id.profile_email);
        profile_phn = root.findViewById(R.id.profile_phn);
        profile_add =root. findViewById(R.id.profile_add);
        profile_serv = root.findViewById(R.id.profile_serv);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        DatabaseReference reference=database.getReference().child("Users").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("Upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                emailId=snapshot.child("emailId").getValue().toString();//email is public now
                String name = snapshot.child("name").getValue().toString();
                String phn=snapshot.child("phone").getValue().toString();
                String addrs=snapshot.child("address").getValue().toString();
                String serv=snapshot.child("service").getValue().toString();
                String imageURI=snapshot.child("imageUri").getValue().toString();

//Setting Data After Retrieving
                profile_name.setText(name);
                profile_serv.setText(serv);
                profile_email.setText(emailId);
                profile_phn.setText(phn);
                profile_add.setText(addrs);
                Picasso.get().load(imageURI).into(profile_image);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }
}